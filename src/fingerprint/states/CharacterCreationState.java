package fingerprint.states;

import java.awt.Font;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.inout.GameFileHandler;
import fingerprint.mainmenus.CharacterInfoContainer;
import fingerprint.mainmenus.GenericGridController;
import fingerprint.networking.NetworkEvents;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.GiveSocketInfoEvent;
import fingerprint.states.menu.enums.CharacterClass;
import io.socket.client.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CharacterCreationState extends BasicGameState{
    private static final Logger logger = Logger.getLogger(CharacterCreationState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private GameFileHandler fileHandler;
    @Inject private InputManager inputManager;
    private int phase = 1;
    
    private Socket socket;
    
    private TextField characterNameTextField;
    private String selectedCharacterName = "";
    
    
    private boolean textMode = false;
    private boolean drawBadFileNameText = false;
    private String naggingText = "Hey hey";
    
    public CharacterCreationState() {

    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        Font font = new Font("Arial Bold", Font.BOLD, 32);
        TrueTypeFont ttf = new TrueTypeFont(font, true);
        characterNameTextField = new TextField(gc, ttf, 200, 420, 300, 34);
        characterNameTextField.setBackgroundColor(Color.gray);
        characterNameTextField.setBorderColor(Color.darkGray);
        characterNameTextField.setAcceptingInput(false);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        renderingManager.drawCharacterCreation(graphics,gc,phase,characterNameTextField, naggingText);

    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        characterNameTextField.setText("");
        phase = 1;
        cleanUpSocket();
    }
    
    

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();

        if(inputManager.isKeyBindPressed(KeyBindAction.MENU,true)){
            menuPressed();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT,true)){
            eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.CHARACTER_SELECTION_ID));
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_SELECTION_ID));
        }
        checkIfFileInputClose();
        checkIfFileNameValid();
    }
    private void menuPressed(){
        switch(phase){
        case 1:
            selectedCharacterName = characterNameTextField.getText();
            phase++;

            break;
        case 2:
            JSONObject payload = new JSONObject();
            try {
                payload.put("charactername", selectedCharacterName);
            } catch (JSONException ex) {
                Logger.getLogger(CharacterCreationState.class.getName()).log(Level.SEVERE, null, ex);
            }
            socket.emit(NetworkEvents.CLIENT_CREATE_CHARACTER, payload);
            break;
        }
    }
    private void checkIfFileInputClose(){
        if(phase != 1){
            characterNameTextField.setAcceptingInput(false);
            characterNameTextField.setFocus(false);
        }else{
            characterNameTextField.setFocus(true);
            characterNameTextField.setConsumeEvents(false);
            characterNameTextField.setAcceptingInput(true);
        }
    }
    private void checkIfFileNameValid(){
        drawBadFileNameText = !GameFileHandler.validateFileName(characterNameTextField.getText());
    }
    
    @Subscribe
    public void listenInitGameInfoEvent(GiveSocketInfoEvent event){
        if(event.getState() != getID()) {
            return;
        }

        this.socket = event.getSocket();
        this.initializeSocketToCharactercreationMode();
    }
    
    @Override
    public int getID() {
        return State_IDs.CHARACTER_CREATION_ID;
    }

    private void cleanUpSocket() {
        socket.off(NetworkEvents.SERVER_CHARACTERLIST);
    }

    private void initializeSocketToCharactercreationMode() {
        socket.on(NetworkEvents.SERVER_CHARACTER_CREATED, args -> {
            eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.CHARACTER_SELECTION_ID));
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_SELECTION_ID));
        }).on(NetworkEvents.SERVER_BAD_CHARACTERNAME, args -> {
            phase = 1;
            checkIfFileInputClose();
            
            JSONObject payload = (JSONObject) args[0];
            if(payload.has("info")){
                try {
                    naggingText = payload.getString("info");
                } catch (JSONException ex) {
                    Logger.getLogger(CharacterCreationState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
}
