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
import fingerprint.gameplay.map.gameworld.GameWorld;
import fingerprint.inout.GameFileHandler;
import fingerprint.mainmenus.WorldCreationController;
import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.SelectPlayableWorldEvent;
import fingerprint.states.menu.enums.GameDifficulty;

public class WorldCreationState extends BasicGameState{
    private static final Logger logger = Logger.getLogger(WorldCreationState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private GameFileHandler fileHandler;
    @Inject private InputManager inputManager;
    private WorldCreationController controller;
    
    private TextField saveName ;
    private GameDifficulty selectedDifficulty;
    
    
    private boolean textMode = false;
    private boolean drawBadFileNameText = false;
    
    public WorldCreationState() {
        controller = new WorldCreationController();
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        Font font = new Font("Arial Bold", Font.BOLD, 32);
        TrueTypeFont ttf = new TrueTypeFont(font, true);
       saveName = new TextField(gc, ttf, 200, 520, 300, 34);
       saveName.setBackgroundColor(Color.gray);
       saveName.setBorderColor(Color.darkGray);
       selectedDifficulty = GameDifficulty.REGULAR;
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        renderingManager.drawWorldCreation(graphics,gc,selectedDifficulty,controller.getSelectedRow(),controller.getSelectedColumn(),saveName,drawBadFileNameText);

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
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.WORLD_SELECTION_ID));
        }
        checkIfFileInputClose();
        checkIfFileNameValid();
    }
    private void menuPressed(){
        switch(controller.getSelectedRow()){
        case 0:
            selectedDifficulty = GameDifficulty.values()[controller.getSelectedColumn()];
            break;
        case 2:
            if(controller.getSelectedColumn() == 0){
                String filename = saveName.getText();
                if(fileHandler.initiateWorld(filename,selectedDifficulty)){
                    //Success
                    GameWorld createdWorld = fileHandler.loadWorldGameFile(filename);
                    if(createdWorld == null){
                        logger.log(Level.WARNING,"World load failed.");
                        return;
                    }
                    logger.log(Level.FINEST,"World Loaded!");
                    eventBus.post(new SelectPlayableWorldEvent(createdWorld));
                    logger.log(Level.FINEST,"Going into character creationscreen");
                    eventBus.post(new ChangeStateEvent(getID(), State_IDs.GAME_PLAY_ID));
                }else{
                    //World creation failed
                    logger.log(Level.WARNING,"World creation failed.");
                }
                
            }else if(controller.getSelectedColumn() == 1){
                //cancel
                eventBus.post(new ChangeStateEvent(getID(), State_IDs.WORLD_SELECTION_ID));
            }
            break;
        }
    }
    private void checkIfFileInputClose(){
        if(controller.getSelectedRow() != 1){
            saveName.setAcceptingInput(false);
            saveName.setFocus(false);
        }else{
            saveName.setFocus(true);
            saveName.setConsumeEvents(false);
            saveName.setAcceptingInput(true);
        }
    }
    private void checkIfFileNameValid(){
        drawBadFileNameText = !GameFileHandler.validateFileName(saveName.getText());
    }
    @Override
    public int getID() {
        return State_IDs.WORLD_CREATION_ID;
    }

}
