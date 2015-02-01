package fingerprint.states;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
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

import fingerprint.mainmenus.GameWorldInfoContainer;
import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;

public class WorldSelectionState extends BasicGameState {
    private static final Logger logger = Logger.getLogger(WorldSelectionState.class.getName());
    private GameWorldInfoContainer currentSelectionWorld;
    private List<GameWorldInfoContainer> availableWorlds = new ArrayList<>();
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    
    private int selection = 0;
    private File[] savedGames;
    
    public WorldSelectionState() {
        
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
                      { return filename.endsWith(".world"); }
        } );
        for(File file:savedGames){
            GameWorldInfoContainer gwic = new GameWorldInfoContainer();
            gwic.worldTitle = "World " + file.getName();
            gwic.worldTitle = gwic.worldTitle.replaceFirst("[.][^.]+$", ""); //remove file extension
            availableWorlds.add(gwic);
        }
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        if(selection == 0){
            currentSelectionWorld.moreLeft = false;
        }else{
            currentSelectionWorld.moreLeft = true;
        }
        if(selection == availableWorlds.size() -1){
            currentSelectionWorld.moreRight = false;
        }else{
            currentSelectionWorld.moreRight = true;
        }
        renderingManager.drawWorldSelection(graphics,currentSelectionWorld);

    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        Input input = gc.getInput();
        if(input.isKeyPressed(Keyboard.KEY_LEFT)){
            if(selection == 0){
                return;
            }else{
                selection--;
                currentSelectionWorld = availableWorlds.get(selection);
            }
        }
        if(input.isKeyPressed(Keyboard.KEY_RIGHT)){
            if(selection == availableWorlds.size() -1){
                
                return;
            }else{
                selection++;
                currentSelectionWorld = availableWorlds.get(selection);
            }
        }
        if(input.isKeyPressed(Keyboard.KEY_SPACE)){
            selectWorld(selection);
        }

    }
    public void selectWorld(int world){
        if(selection == 0){
            //generate new world
        }else{
            File loadableSave = savedGames[selection-1];
            
        }
        eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_SCREEN_ID));
    }

    @Override
    public int getID() {
        return State_IDs.WORLD_SELECTION_ID;
    }

}
