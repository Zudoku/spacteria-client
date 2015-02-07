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

import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.PlayerOutlookEvent;

public class CharacterCreationState extends BasicGameState{
    private static final Logger logger = Logger.getLogger(CharacterCreationState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    public CharacterCreationState() {
        
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        renderingManager.drawCharacterCreation(graphics);
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        Input input = gc.getInput();
        if(input.isKeyPressed(Keyboard.KEY_SPACE)){
            eventBus.post(new PlayerOutlookEvent(false));
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.GAME_PLAY_ID));
        }
    }

    @Override
    public int getID() {
        return State_IDs.CHARACTER_SCREEN_ID;
    }
}
