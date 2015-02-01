package fingerprint.states;


import java.util.logging.Logger;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
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

public class MainMenuState extends BasicGameState{
    private static final Logger logger = Logger.getLogger(MainMenuState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    public MainMenuState() {
        
        
    }
    
    @Override
    public void init(GameContainer arg0, StateBasedGame arg1)
            throws SlickException {
        
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame arg1, Graphics graphics)
            throws SlickException {
        renderingManager.drawMainMenu(graphics);
        
    }

    @Override
    public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
            throws SlickException {
        Input input = arg0.getInput();
        if(input.isKeyPressed(Keyboard.KEY_SPACE)){
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.WORLD_SELECTION_ID));
        }
    }

    @Override
    public int getID() {
        return State_IDs.MAIN_MENU_ID;
    }

}
