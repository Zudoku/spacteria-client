package fingerprint.states;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.map.gameworld.CharacterSaveFile;
import fingerprint.inout.GameFileHandler;
import fingerprint.mainmenus.CharacterInfoContainer;
import fingerprint.mainmenus.CharacterSelectionController;
import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.SelectCharacterEvent;

public class CharacterSelectionState extends BasicGameState {
    private static final Logger logger = Logger.getLogger(CharacterSelectionState.class.getName());
    
    private CharacterInfoContainer currentSelectionChar;
    private List<CharacterInfoContainer> availableChars = new ArrayList<>();
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private GameFileHandler fileHandler;
    @Inject private InputManager inputManager;
    private CharacterSelectionController controller;
    
    private File[] savedChars;
    
    public CharacterSelectionState() {
        controller = new CharacterSelectionController();
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        CharacterInfoContainer createNewWorld = new CharacterInfoContainer();
        createNewWorld.setIsCreateNewCharDummy(true);
        availableChars.add(createNewWorld);
        currentSelectionChar = createNewWorld;
        
        File dir = new File("Saves/Characters");
        savedChars= dir.listFiles(new FilenameFilter() {
                 public boolean accept(File characterfile, String filename)
                      { 
                         if(characterfile.isDirectory()){
                             return false;
                         }
                         if(characterfile.getName().endsWith(".character")){
                             return true;
                         } else {
                             return false;
                         }
                      }
        } );
        for(File file:savedChars){
            CharacterInfoContainer gwic = new CharacterInfoContainer();
            gwic.setFilename(file.getName());
            //gwic.worldTitle = "World " + file.getName();
            //gwic.worldTitle = GameFileHandler.removeFileExension(gwic.worldTitle);
            availableChars.add(gwic);
        }
        controller.setFilesAmount(savedChars.length);
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        
        renderingManager.drawWorldSelection(graphics,currentSelectionChar);

    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        if(inputManager.isKeyBindPressed(KeyBindAction.LEFT,true)){
            controller.left();
            currentSelectionChar = availableChars.get(controller.getSelection());
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.RIGHT,true)){
            controller.right();
            currentSelectionChar = availableChars.get(controller.getSelection());
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.D,true)){
            selectCharacter(controller.getSelection());
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT,true)){
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
        }
        currentSelectionChar.setMoreLeft(controller.getMoreLeft());
        currentSelectionChar.setMoreRight(controller.getMoreRight());

    }
    public void selectCharacter(int characterIndex){
        if(controller.getSelection() == 0){
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_CREATION_ID));
        }else{
            File loadableSave = savedChars[controller.getSelection()-1];
            
            String filename = GameFileHandler.removeFileExension(loadableSave.getName());
            CharacterSaveFile loadedGame = fileHandler.loadCharacterSaveFile(filename);
            if(loadedGame == null){
                logger.log(Level.WARNING,"File load was not successful");
                return;
            }
            eventBus.post(new SelectCharacterEvent(loadedGame));
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.SERVERLIST_ID));
        }
        
    }

    @Override
    public int getID() {
        return State_IDs.CHARACTER_SELECTION_ID;
    }

}
