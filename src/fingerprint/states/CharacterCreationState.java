package fingerprint.states;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.map.gameworld.CharacterSaveFile;
import fingerprint.inout.GameFileHandler;
import fingerprint.mainmenus.CharacterCreationController;
import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.SelectCharacterEvent;
import fingerprint.states.menu.enums.CharacterClass;

public class CharacterCreationState extends BasicGameState{
    private static final Logger logger = Logger.getLogger(CharacterCreationState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private GameFileHandler fileHandler;
    @Inject private InputManager inputManager;
    private CharacterCreationController controller;
    
    private TextField characterName;
    private CharacterClass selectedClass;
    
    
    private boolean textMode = false;
    private boolean drawBadFileNameText = false;
    
    public CharacterCreationState() {
        controller = new CharacterCreationController();
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        Font font = new Font("Arial Bold", Font.BOLD, 32);
        TrueTypeFont ttf = new TrueTypeFont(font, true);
       characterName = new TextField(gc, ttf, 200, 520, 300, 34);
       characterName.setBackgroundColor(Color.gray);
       characterName.setBorderColor(Color.darkGray);
       selectedClass = CharacterClass.MAGE;
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        renderingManager.drawWorldCreation(graphics,gc,selectedClass,controller.getSelectedRow(),controller.getSelectedColumn(),characterName,drawBadFileNameText);

    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        //TODO: redo everything
        
        //TEMPFIX
        Input input = gc.getInput();
        if(inputManager.isKeyBindPressed(KeyBindAction.D,true)){
            menuPressed();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.UP,true)){
            controller.up();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.DOWN,true)){
            controller.down();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.RIGHT,true)){
            controller.right();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.LEFT,true)){
            controller.left();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT,true)){
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_SELECTION_ID));
        }
        checkIfFileInputClose();
        checkIfFileNameValid();
    }
    private void menuPressed(){
        switch(controller.getSelectedRow()){
        case 0:
            selectedClass = CharacterClass.values()[controller.getSelectedColumn()];
            break;
        case 2:
            if(controller.getSelectedColumn() == 0){
                String filename = characterName.getText();
                
                //Save character
                eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_SELECTION_ID));
                /*
                if(fileHandler.initiateWorld(filename,selectedClass)){
                    //Success
                    GameWorld createdWorld = fileHandler.loadWorldGameFile(filename);
                    if(createdWorld == null){
                        logger.log(Level.WARNING,"World load failed.");
                        return;
                    }
                    logger.log(Level.FINEST,"World Loaded!");
                    eventBus.post(new SelectCharacterEvent(createdWorld));
                    logger.log(Level.FINEST,"Going into character creationscreen");
                    
                }else{
                    //World creation failed
                    logger.log(Level.WARNING,"World creation failed.");
                }
                */
            }else if(controller.getSelectedColumn() == 1){
                //cancel
                eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_SELECTION_ID));
            }
            break;
        }
    }
    private void checkIfFileInputClose(){
        if(controller.getSelectedRow() != 1){
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
