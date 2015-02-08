package fingerprint.states;

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

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.map.gameworld.GameWorld;
import fingerprint.gameplay.map.gameworld.GameWorldContainer;
import fingerprint.rendering.RenderingManager;

public class GamePlayState extends BasicGameState{

    private static final Logger logger = Logger.getLogger(GamePlayState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private InputManager inputManager;
    
    private GameWorldContainer worldContainer;
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
       worldContainer = new GameWorldContainer();
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        renderingManager.drawGamePlay(graphics, worldContainer.getTileMapToDraw());
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        if(inputManager.isKeyBindDown(KeyBindAction.DOWN,true)){
            renderingManager.setScreenStartY(renderingManager.getScreenStartY() + 1);
        }
        
    }
    public void setGameWorld(GameWorld world){
        worldContainer.setWorld(world);
    }

    @Override
    public int getID() {
        return State_IDs.GAME_PLAY_ID;
    }

}
