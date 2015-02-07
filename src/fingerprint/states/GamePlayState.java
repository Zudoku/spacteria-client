package fingerprint.states;

import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.gameplay.map.gameworld.GameWorld;
import fingerprint.gameplay.map.gameworld.GameWorldContainer;
import fingerprint.rendering.RenderingManager;

public class GamePlayState extends BasicGameState{

    private static final Logger logger = Logger.getLogger(GamePlayState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    
    private GameWorldContainer worldContainer;
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
       
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        renderingManager.drawGamePlay(graphics, null);
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        
        
    }
    public void setGameWorld(GameWorld world){
        worldContainer.setWorld(world);
    }

    @Override
    public int getID() {
        return State_IDs.GAME_PLAY_ID;
    }

}
