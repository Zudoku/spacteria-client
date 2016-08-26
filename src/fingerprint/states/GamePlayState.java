package fingerprint.states;

import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.map.gameworld.GameWorldContainer;
import fingerprint.gameplay.objects.player.DummyPlayer;
import fingerprint.inout.GameFileHandler;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.networking.NetworkEvents;
import fingerprint.networking.events.PlayerJoinedEvent;
import fingerprint.networking.events.PlayerLeftEvent;
import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.InitGameInfoEvent;
import fingerprint.states.events.SaveAndExitWorldEvent;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.util.logging.Level;


public class GamePlayState extends BasicGameState{

    private static final Logger logger = Logger.getLogger(GamePlayState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private InputManager inputManager;
    @Inject private GameFileHandler gameFileHandler;
    
    @Inject private GameWorldContainer worldContainer;
    @Inject private Gson gson;

    private boolean debugInfo = true;
    
    private String myID = "";
    
    private Socket mySocket;
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        //GameLauncher.injector.injectMembers(worldContainer); //dirty trick
        eventBus.register(this);
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        renderingManager.drawGamePlay(graphics,debugInfo);
        //renderingManager.drawDebugGamePlay(graphics);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        worldContainer.updateWorld(inputManager,delta);
    }
    

    @Override
    public int getID() {
        return State_IDs.GAME_PLAY_ID;
    }
    
    @Subscribe
    public void listenInitGameInfoEvent(InitGameInfoEvent event){
        myID = event.getMyID();
        event.getMyCharacter().setX(event.getDescription().getMapDescription().getStartX());
        event.getMyCharacter().setY(event.getDescription().getMapDescription().getStartY());
        worldContainer.setMyCharacter(event.getMyCharacter(),myID);
        
        RoomDescription description = event.getDescription();
        logger.log(Level.SEVERE, myID);
        DummyPlayer ourOwn = null;
        //Remove our own dummy
        for(DummyPlayer dp : description.getPlayers()){
            logger.log(Level.SEVERE, dp.getId());
            if(dp.getId().substring(2).equals(myID)){
                ourOwn = dp;
                
            }
        }
        mySocket = event.getSocket();
        description.getPlayers().remove(ourOwn);
        worldContainer.setCurrentRoom(event.getDescription());
        try{
            renderingManager.setMap(event.getDescription().getMapDescription());
        } catch(Exception e){
            logger.log(Level.SEVERE, null, e);
        }
        
        initializeSocketToGamePlayMode();
    }
    
    
    private void initializeSocketToGamePlayMode(){
        mySocket.off(NetworkEvents.SERVER_DISPLAYROOMLIST);
        mySocket.off(NetworkEvents.SERVER_JOINROOM);
        
        mySocket.on(NetworkEvents.SERVER_PLAYERJOINEDMYGAME, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                String payload = args[0].toString();
                PlayerJoinedEvent event = new PlayerJoinedEvent((DummyPlayer) gson.fromJson(payload, DummyPlayer.class));
                eventBus.post(event);
            }

        }).on(NetworkEvents.SERVER_PLAYERLEFTMYGAME, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                eventBus.post(gson.fromJson(args[0].toString(), PlayerLeftEvent.class));
            }

        });
        
    }
    
    @Subscribe
    public void listenSaveAndExitWorldEvent(SaveAndExitWorldEvent event){
        gameFileHandler.saveCharacterFile(null);
        
        eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
    }


}
