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
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.core.GameLauncher;
import fingerprint.gameplay.map.gameworld.CharacterSaveFile;
import fingerprint.gameplay.map.gameworld.GameWorldContainer;
import fingerprint.gameplay.objects.player.Player;
import fingerprint.gameplay.objects.player.PlayerContainer;
import fingerprint.inout.GameFileHandler;
import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.SaveAndExitWorldEvent;
import fingerprint.states.menu.enums.GamePlayStateMode;

public class GamePlayState extends BasicGameState{

    private static final Logger logger = Logger.getLogger(GamePlayState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private InputManager inputManager;
    @Inject private GameFileHandler gameFileHandler;
    
    @Inject private GameWorldContainer worldContainer;
    
    private GamePlayStateMode mode = GamePlayStateMode.NORMAL;
    private boolean gamePaused = false;
    private boolean debugInfo = true;
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        //worldContainer = new GameWorldContainer();
        //GameLauncher.injector.injectMembers(worldContainer); //dirty trick
        eventBus.register(this);
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        if(mode == GamePlayStateMode.NORMAL){
            renderingManager.drawGamePlay(graphics,debugInfo);
        }
        if(mode == GamePlayStateMode.DEBUG){
            renderingManager.drawDebugGamePlay(graphics);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        worldContainer.updateWorld(inputManager,delta);
        
        if(inputManager.isKeyBindPressed(KeyBindAction.DEBUG_TOGGLE, true)){
            if(mode == GamePlayStateMode.DEBUG){
                mode = GamePlayStateMode.NORMAL;
            }else if(mode == GamePlayStateMode.NORMAL){
                mode = GamePlayStateMode.DEBUG;
            }
        }
        
        
        
    }
    

    @Override
    public int getID() {
        return State_IDs.GAME_PLAY_ID;
    }
    
    @Subscribe
    public void listenSaveAndExitWorldEvent(SaveAndExitWorldEvent event){
        gameFileHandler.saveCharacterFile(null);
        
        eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
    }


}
