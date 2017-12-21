/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.states;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.inout.GameFileHandler;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.GiveSocketInfoEvent;
import io.socket.client.Socket;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created Dec 20, 2017
 * @author arska
 */
public class ThrophiesState extends BasicGameState {
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private GameFileHandler fileHandler;
    @Inject private InputManager inputManager;
    private Socket socket;

    @Override
    public int getID() {
        return State_IDs.THROPHIES_ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT,true)){
            eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.MAIN_MENU_ID));
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
        }
    }

    @Subscribe
    public void listenInitGameInfoEvent(GiveSocketInfoEvent event){
        if(event.getState() != getID()) {
            return;
        }

        this.socket = event.getSocket();
    }
}
