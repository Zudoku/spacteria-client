package fingerprint.states;

import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GamePlayState extends BasicGameState{

    private static final Logger logger = Logger.getLogger(GamePlayState.class.getName());
    
    @Override
    public void init(GameContainer arg0, StateBasedGame arg1)
            throws SlickException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
            throws SlickException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
            throws SlickException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getID() {
        return State_IDs.GAME_PLAY_ID;
    }

}
