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
import fingerprint.mainmenus.GenericGridController;
import fingerprint.networking.NetworkEvents;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.rendering.util.ConnectionRenderingInformation;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.GiveSocketInfoEvent;
import fingerprint.states.menu.enums.CharacterClass;
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
    //private static final String serveraddrs = "http://192.168.1.141:3590";
    // private static final String serveraddrs = "http://127.0.0.1:3590";
    private static final String serveraddrs = "http://www.spacteria.com:3590";
    
    private String lastMessageFromServer = "";
    public static String SOCKETSTATUS;
    
    private String versionString  = "";
    private String versionChangelog = "";

    public LoginState() {
        controller = new GenericGridController(Arrays.asList(0,0), Arrays.asList(0,1));
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
            IO.Options options = new IO.Options();

            socket = IO.socket(serveraddrs, options);

            socket.on(Socket.EVENT_CONNECT, (Object... args) -> {
                SOCKETSTATUS = "CONNECTED";
            }).on(Socket.EVENT_DISCONNECT, (Object... args) -> {
                eventBus.post(new ChangeStateEvent(getID(), State_IDs.LOGIN_SCREEN_ID));
                SOCKETSTATUS = "NO CONNECTION (S)";
            }).on(Socket.EVENT_CONNECTING, (Object... args) -> {
                SOCKETSTATUS = "TRYING TO CONNECT";
            }).on(Socket.EVENT_CONNECT_ERROR, (Object... args) -> {
                SOCKETSTATUS = "NO CONNECTION (E)";
            }).on(Socket.EVENT_CONNECT_TIMEOUT, (Object... args) -> {
                SOCKETSTATUS = "NO CONNECTION (T)";
            }).on(Socket.EVENT_RECONNECT, (Object... args) -> {
                SOCKETSTATUS = "CONNECTED";
            }).on(Socket.EVENT_RECONNECTING, (Object... args) -> {
                SOCKETSTATUS = "TRYING TO CONNECT";
            }).on(Socket.EVENT_RECONNECT_ATTEMPT, (Object... args) -> {
                SOCKETSTATUS = "TRYING TO CONNECT";
            }).on(Socket.EVENT_RECONNECT_ERROR, (Object... args) -> {
                SOCKETSTATUS = "NO CONNECTION (RE)";
            }).on(Socket.EVENT_RECONNECT_FAILED, (Object... args) -> {
                SOCKETSTATUS = "NO CONNECTION (RF)";
            }).on(NetworkEvents.SERVER_LOGIN_SUCCESS, (Object... args) -> {
                eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.MAIN_MENU_ID));
                eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
                cleanUpSocket();
            }).on(NetworkEvents.SERVER_LOGIN_FAIL, (Object... args) -> {
                try {
                    JSONObject payload = (JSONObject) args[0];
                    String reason = payload.getString("reason");
                    lastMessageFromServer = reason;
                    logger.log(Level.SEVERE, Arrays.toString(args));
                } catch (JSONException ex) {
                    Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).on(NetworkEvents.SERVER_VERSION_DATA, (Object... args) -> {
                try {
                    JSONObject payload = (JSONObject) args[0];
                    versionString = payload.getString("version");
                    versionChangelog = payload.getString("changelog");
                } catch (JSONException ex) {
                    Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            socket.connect();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        ConnectionRenderingInformation info = new ConnectionRenderingInformation(socket, serveraddrs, lastMessageFromServer, SOCKETSTATUS);
        info.setVersion(versionString);
        info.setChangelog(versionChangelog);
        renderingManager.drawLogin(grphcs, gc, usernameTextField, passwordTextField, controller, info);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        
        if(inputManager.isKeyBindPressed(KeyBindAction.MENU,true)){
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
        
        if(inputManager.isKeyBindPressed(KeyBindAction.DEBUG_TOGGLE, true)) {
            socket.disconnect();
            socket.close();
            initializeSocketToLoginMode();
            System.out.println("Refreshing socket!!!");
            usernameTextField.setTextColor(Color.magenta);
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
    
    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        passwordTextField.setAcceptingInput(false);
        usernameTextField.setAcceptingInput(false);
    }

}
