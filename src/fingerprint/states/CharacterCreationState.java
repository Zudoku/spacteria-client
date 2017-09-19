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
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.inout.GameFileHandler;
import fingerprint.mainmenus.GenericGridController;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.menu.enums.CharacterClass;
import java.util.ArrayList;
import java.util.Arrays;

public class CharacterCreationState extends BasicGameState{
    private static final Logger logger = Logger.getLogger(CharacterCreationState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private GameFileHandler fileHandler;
    @Inject private InputManager inputManager;
    private int phase = 1;
    
    private TextField characterName;
    private CharacterClass selectedClass;
    
    
    private boolean textMode = false;
    private boolean drawBadFileNameText = false;
    
    public CharacterCreationState() {

    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        Font font = new Font("Arial Bold", Font.BOLD, 32);
        TrueTypeFont ttf = new TrueTypeFont(font, true);
       characterName = new TextField(gc, ttf, 200, 420, 300, 34);
       characterName.setBackgroundColor(Color.gray);
       characterName.setBorderColor(Color.darkGray);
       characterName.setAcceptingInput(false);
       selectedClass = CharacterClass.MAGE;
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        renderingManager.drawWorldCreation(graphics,gc,selectedClass,phase,characterName,drawBadFileNameText);

    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        selectedClass = CharacterClass.MAGE;
        characterName.setText("");
        phase = 1;
    }
    
    

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();

        if(inputManager.isKeyBindPressed(KeyBindAction.MENU,true)){
            menuPressed();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.LEFT,true)){
            //END ME PLEASE, THIS IMPLEMENTATION SUCKS
            if(phase == 1) {
                if(selectedClass == CharacterClass.KNIGHT){
                    selectedClass = CharacterClass.MAGE;
                    return;
                }
                if(selectedClass == CharacterClass.MAGE){
                    selectedClass = CharacterClass.WARRIOR;
                    return;
                }
                if(selectedClass == CharacterClass.WARRIOR){
                    selectedClass = CharacterClass.KNIGHT;
                    return;
                }
            }
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.RIGHT,true)){
            //END ME PLEASE, THIS IMPLEMENTATION SUCKS
            if(phase == 1) {
                if(selectedClass == CharacterClass.MAGE){
                    selectedClass = CharacterClass.KNIGHT;
                    return;
                }
                if(selectedClass == CharacterClass.WARRIOR){
                    selectedClass = CharacterClass.MAGE;
                    return;
                }
                if(selectedClass == CharacterClass.KNIGHT){
                    selectedClass = CharacterClass.WARRIOR;
                    return;
                }
            }
        }

        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT,true)){
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_SELECTION_ID));
        }
        checkIfFileInputClose();
        checkIfFileNameValid();
    }
    private void menuPressed(){
        switch(phase){
        case 1:
            phase++;
            break;
        case 2:
            String filename = characterName.getText();
            phase++;

            break;
        case 3:
            //eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_SELECTION_ID));
            break;
        }
    }
    private void checkIfFileInputClose(){
        if(phase != 2){
            characterName.setAcceptingInput(false);
            characterName.setFocus(false);
        }else{
            characterName.setFocus(true);
            characterName.setConsumeEvents(false);
            characterName.setAcceptingInput(true);
        }
    }
    private void checkIfFileNameValid(){
        drawBadFileNameText = !GameFileHandler.validateFileName(characterName.getText());
    }
    @Override
    public int getID() {
        return State_IDs.CHARACTER_CREATION_ID;
    }

}
