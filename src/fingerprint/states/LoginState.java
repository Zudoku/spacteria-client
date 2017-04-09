/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.states;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.objects.events.DeleteEntityEvent;
import fingerprint.gameplay.objects.player.DummyCharacter;
import fingerprint.gameplay.objects.projectiles.NewProjectileSpawnedEvent;
import fingerprint.mainmenus.GenericGridController;
import fingerprint.networking.NetworkEvents;
import fingerprint.networking.events.CorrectNPCPositionEvent;
import fingerprint.networking.events.PlayerJoinedEvent;
import fingerprint.networking.events.PlayerLeftEvent;
import fingerprint.networking.events.RefreshRoomDescEvent;
import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.GiveSocketInfoEvent;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.awt.Font;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created Feb 13, 2017
 * @author arska
 */
public class LoginState  extends BasicGameState {
    
    private static final Logger logger = Logger.getLogger(LoginState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private InputManager inputManager;

    @Inject private EventBus eventBus;
    
    private Socket socket;
    
    private GenericGridController controller;
    
    private TextField usernameTextField;
    private TextField passwordTextField;
    
    private static final String serveraddrs = "http://192.168.1.141:3590";

    public LoginState() {
        controller = new GenericGridController(Arrays.asList(0,0,0), Arrays.asList(0,1));
    }

    @Override
    public int getID() {
        return State_IDs.LOGIN_SCREEN_ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        Font font = new Font("Arial Bold", Font.BOLD, 32);
        TrueTypeFont ttf = new TrueTypeFont(font, true);
        usernameTextField = new TextField(gc, ttf, 200, 520, 300, 34);
        usernameTextField.setBackgroundColor(Color.gray);
        usernameTextField.setBorderColor(Color.darkGray);
        usernameTextField.setText("testuser");
        passwordTextField = new TextField(gc, ttf, 200, 620, 300, 34);
        passwordTextField.setBackgroundColor(Color.gray);
        passwordTextField.setBorderColor(Color.darkGray);
        passwordTextField.setText("1234567");
        
        initializeSocketToLoginMode();
    }
    
    private void initializeSocketToLoginMode(){
        try {
            socket = IO.socket(serveraddrs);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    eventBus.post(new ChangeStateEvent(getID(), State_IDs.LOGIN_SCREEN_ID));
                }

            }).on(NetworkEvents.SERVER_LOGIN_SUCCESS, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.MAIN_MENU_ID));
                    eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
                    cleanUpSocket();
                }

            }).on(NetworkEvents.SERVER_LOGIN_FAIL, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    logger.log(Level.SEVERE, Arrays.toString(args));
                }

            });
            
            socket.connect();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        renderingManager.drawLogin(grphcs, gc, usernameTextField, passwordTextField, controller);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        
        if(inputManager.isKeyBindPressed(KeyBindAction.D,true) && controller.getSelectedRow() == 2){
            //Try to login to the game
            identifyToServer();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.UP,true)){
            controller.up();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.DOWN,true)){
            controller.down();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.SKIP,true)){
            controller.unlock();
            controller.down();
        }
        
        checkIfFileInputClose();
    }
    
    private void checkIfFileInputClose(){
        if(controller.getSelectedRow() != 0){
            usernameTextField.setAcceptingInput(false);
            usernameTextField.setFocus(false);
            passwordTextField.setConsumeEvents(true);
        }else{
            usernameTextField.setFocus(true);
            usernameTextField.setConsumeEvents(false);
            usernameTextField.setAcceptingInput(true);
        }
        if(controller.getSelectedRow() != 1){
            passwordTextField.setAcceptingInput(false);
            passwordTextField.setFocus(false);
            passwordTextField.setConsumeEvents(true);
        }else{
            passwordTextField.setFocus(true);
            passwordTextField.setConsumeEvents(false);
            passwordTextField.setAcceptingInput(true);
        }
    }
    
    public void identifyToServer(){
        JSONObject identifyObject = new JSONObject();
        try {
            identifyObject.put("type", "game-client");
            GsonBuilder ga=new GsonBuilder();
            Gson gson = ga.create();
            identifyObject.put("username", usernameTextField.getText());
            identifyObject.put("password", passwordTextField.getText());
        } catch (JSONException ex) {
            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
        }

        socket.emit(NetworkEvents.CLIENT_IDENTIFY, identifyObject, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
    }
    
    private void cleanUpSocket() {
        socket.off(NetworkEvents.SERVER_LOGIN_FAIL);
        socket.off(NetworkEvents.SERVER_LOGIN_SUCCESS);
    }

}
