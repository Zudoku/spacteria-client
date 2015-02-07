package fingerprint.states;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.gameplay.map.gameworld.GameWorld;
import fingerprint.inout.GameFileHandler;
import fingerprint.mainmenus.GameWorldInfoContainer;
import fingerprint.mainmenus.WorldSelectionController;
import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.SelectPlayableWorldEvent;

public class WorldSelectionState extends BasicGameState {
    private static final Logger logger = Logger.getLogger(WorldSelectionState.class.getName());
    
    private GameWorldInfoContainer currentSelectionWorld;
    private List<GameWorldInfoContainer> availableWorlds = new ArrayList<>();
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private GameFileHandler fileHandler;
    private WorldSelectionController controller;
    
    private File[] savedGames;
    
    public WorldSelectionState() {
        controller = new WorldSelectionController();
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        GameWorldInfoContainer createNewWorld = new GameWorldInfoContainer();
        createNewWorld.worldTitle = "Create new world";
        createNewWorld.filename = "CREATENEWWORLD";
        availableWorlds.add(createNewWorld);
        currentSelectionWorld = createNewWorld;
        
        File dir = new File("Saves");
        savedGames= dir.listFiles(new FilenameFilter() {
                 public boolean accept(File dir, String filename)
                      { return filename.endsWith(GameFileHandler.WORLD_FILE_EXTENSION); }
        } );
        for(File file:savedGames){
            GameWorldInfoContainer gwic = new GameWorldInfoContainer();
            gwic.worldTitle = "World " + file.getName();
            gwic.worldTitle = GameFileHandler.removeFileExension(gwic.worldTitle);
            availableWorlds.add(gwic);
        }
        controller.setFilesAmount(savedGames.length);
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        
        renderingManager.drawWorldSelection(graphics,currentSelectionWorld);

    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        Input input = gc.getInput();
        if(input.isKeyPressed(Keyboard.KEY_LEFT)){
            controller.left();
            currentSelectionWorld = availableWorlds.get(controller.getSelection());
        }
        if(input.isKeyPressed(Keyboard.KEY_RIGHT)){
            controller.right();
            currentSelectionWorld = availableWorlds.get(controller.getSelection());
        }
        if(input.isKeyPressed(Keyboard.KEY_SPACE)){
            selectWorld(controller.getSelection());
        }
        if(input.isKeyPressed(Keyboard.KEY_ESCAPE)){
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
        }
        currentSelectionWorld.moreLeft = controller.getMoreLeft();
        currentSelectionWorld.moreRight = controller.getMoreRight();

    }
    public void selectWorld(int world){
        if(controller.getSelection() == 0){
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.WORLD_CREATION_ID));
        }else{
            File loadableSave = savedGames[controller.getSelection()-1];
            String filename = GameFileHandler.removeFileExension(loadableSave.getName());
            GameWorld loadedGame = fileHandler.loadWorldGameFile(filename);
            if(loadedGame == null){
                logger.log(Level.WARNING,"File load was not successful");
                return;
            }
            eventBus.post(new SelectPlayableWorldEvent(loadedGame));
            //TODO: check for chara
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.GAME_PLAY_ID));
        }
        
    }

    @Override
    public int getID() {
        return State_IDs.WORLD_SELECTION_ID;
    }

}