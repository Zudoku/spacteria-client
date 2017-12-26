package fingerprint.states;


import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.rendering.util.ConnectionRenderingInformation;
import static fingerprint.states.LoginState.SOCKETSTATUS;
import static fingerprint.states.LoginState.environment;
import static fingerprint.states.LoginState.lastMessageFromServer;
import static fingerprint.states.LoginState.versionChangelog;
import static fingerprint.states.LoginState.versionString;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.CloseProgramEvent;
import fingerprint.states.events.GiveSocketInfoEvent;
import fingerprint.states.menu.enums.MainMenuSelection;
import io.socket.client.Socket;
import org.newdawn.slick.Image;

public class MainMenuState extends BasicGameState{
    private static final Logger logger = Logger.getLogger(MainMenuState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private InputManager inputManager;
    
    private MainMenuSelection selection;
    private Socket socket;
    
    public MainMenuState() {
        
        
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg)
            throws SlickException {
        selection = MainMenuSelection.PLAY;
        Image img = new Image("resources/cursor2.png");
        gc.setMouseCursor(img, 16,16);
        
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame arg1, Graphics graphics)
            throws SlickException {
        ConnectionRenderingInformation info = new ConnectionRenderingInformation(socket, environment.getServerlURL(), lastMessageFromServer, SOCKETSTATUS);
        info.setVersion(versionString);
        info.setChangelog(versionChangelog);
        info.setEnvironment(environment);
        renderingManager.drawMainMenu(graphics,selection, info);
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        if(inputManager.isKeyBindPressed(KeyBindAction.MENU,true)){
            menuPressed();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.UP,true)){
            if(selection.getIndex() > 0){
                selection = MainMenuSelection.values()[selection.getIndex()-1];
            }else{
                selection = MainMenuSelection.values()[MainMenuSelection.values().length-1];
            }
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.DOWN,true)){
            if(selection.getIndex() < MainMenuSelection.values().length-1){
                selection = MainMenuSelection.values()[selection.getIndex()+1];
            }else{
                selection = MainMenuSelection.values()[0];
            }
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT,true)){
            eventBus.post(new CloseProgramEvent(false, true));
        }
        
    }
    private void menuPressed(){
        switch(selection){
        case PLAY:
            eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.CHARACTER_SELECTION_ID));
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_SELECTION_ID));
            break;
            
        case OPTIONS:
            eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.OPTIONS_ID));
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.OPTIONS_ID));
            break;
            
        case LEADERBOARD:
            eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.LEADERBOARDS_ID));
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.LEADERBOARDS_ID));
            break;
            
        case TROPHIES:
            eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.THROPHIES_ID));
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.THROPHIES_ID));
            break;
            
        case EXIT:
            eventBus.post(new CloseProgramEvent(false, true));
            break;
        }
    }
    
    @Subscribe
    public void listenInitGameInfoEvent(GiveSocketInfoEvent event){
        if(event.getState() != getID()) {
            return;
        }

        this.socket = event.getSocket();
    }
    

    @Override
    public int getID() {
        return State_IDs.MAIN_MENU_ID;
    }

}
