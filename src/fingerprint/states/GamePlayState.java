package fingerprint.states;

import java.util.logging.Logger;

import fingerprint.gameplay.objects.events.RenderLootBagEvent;
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
import fingerprint.gameplay.map.gameworld.GameWorldContainer;
import fingerprint.gameplay.objects.events.DeleteEntityEvent;
import fingerprint.gameplay.objects.events.NewLootBagSpawnedEvent;
import fingerprint.gameplay.objects.lootbag.LootBag;
import fingerprint.gameplay.objects.player.DummyCharacter;
import fingerprint.gameplay.objects.projectiles.NewProjectileSpawnedEvent;
import fingerprint.gameplay.objects.projectiles.SpawnProjectileEvent;
import fingerprint.inout.GameFileHandler;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.networking.NetworkEvents;
import fingerprint.networking.events.CorrectNPCPositionEvent;
import fingerprint.networking.events.PlayerJoinedEvent;
import fingerprint.networking.events.PlayerLeftEvent;
import fingerprint.networking.events.RefreshRoomDescEvent;
import fingerprint.networking.events.UpdatePositionEvent;
import fingerprint.rendering.GamePlayRenderingInformation;
import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.InitGameInfoEvent;
import fingerprint.states.events.SaveAndExitWorldEvent;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.util.logging.Level;
import org.json.JSONException;
import org.json.JSONObject;


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

    private boolean initialized = false;
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        //GameLauncher.injector.injectMembers(worldContainer); //dirty trick
        eventBus.register(this);
        inputManager.setInput(gc.getInput());
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        if(!initialized){
            return;
        }
        GamePlayRenderingInformation gri = new GamePlayRenderingInformation();
        
        gri.setCameraRotation(worldContainer.getCameraAngle());
        gri.setMyName(worldContainer.getMyName());
        gri.setLevel(worldContainer.getMyLevel());
        gri.setExperience(worldContainer.getMyExp());
        gri.setMyStats(worldContainer.getMyStats());
        gri.setMapName(worldContainer.getMapName());
        gri.setCharClass(worldContainer.getMyClass());
        gri.setLootToRender(worldContainer.getLootToRender());
        //worldContainer.
        renderingManager.drawGamePlay(graphics,debugInfo, gri);
        //renderingManager.drawDebugGamePlay(graphics);

        worldContainer.setLootToRender(null);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        if(!initialized){
            return;
        }
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

        RoomDescription description = event.getDescription();
        event.getMyCharacter().setX(description.getMapDescription().getStartX());
        event.getMyCharacter().setY(description.getMapDescription().getStartY());
        
        mySocket = event.getSocket();
        
        changeRoom(description);
        event.getMyCharacter().init();
        worldContainer.setMyCharacter(event.getMyCharacter(),myID);

        
        initializeSocketToGamePlayMode();
        initialized = true;
    }
    
    private void changeRoom(RoomDescription description){
        DummyCharacter ourOwn = null;
        //Remove our own dummy
        for(DummyCharacter dp : description.getPlayers()){
            if(dp.getId().substring(2).equals(myID)){
                ourOwn = dp;
                
            }
        }
        
        description.getPlayers().remove(ourOwn);
        
        worldContainer.setCurrentRoom(description, myID);
        worldContainer.setPlayerCoords(description.getMapDescription().getStartX(), description.getMapDescription().getStartY());
        try{
            renderingManager.setMap(description.getMapDescription());
        } catch(Exception e){
            logger.log(Level.SEVERE, null, e);
        }
    }
    
    
    private void initializeSocketToGamePlayMode(){
        mySocket.off(NetworkEvents.SERVER_DISPLAYROOMLIST);
        mySocket.off(NetworkEvents.SERVER_JOINROOM);
        
        mySocket.on(NetworkEvents.SERVER_PLAYERJOINEDMYGAME, args -> {
            String payload = args[0].toString();
            PlayerJoinedEvent event = new PlayerJoinedEvent((DummyCharacter) gson.fromJson(payload, DummyCharacter.class));
            eventBus.post(event);
        }).on(NetworkEvents.SERVER_PLAYERLEFTMYGAME, args -> {
            eventBus.post(gson.fromJson(args[0].toString(), PlayerLeftEvent.class));
        }).on(NetworkEvents.SERVER_CORRECTNPCPOSITION, args -> {
            eventBus.post(gson.fromJson(args[0].toString(), CorrectNPCPositionEvent.class));
        }).on(NetworkEvents.SERVER_PROJECTILE_SPAWNED, args -> {
            eventBus.post(gson.fromJson(args[0].toString(), NewProjectileSpawnedEvent.class));
        }).on(NetworkEvents.SERVER_GAMEOBJECT_DESPAWNED, args -> {
            eventBus.post(gson.fromJson(args[0].toString(), DeleteEntityEvent.class));
        }).on(NetworkEvents.SERVER_REFRESH_ROOM_DESC, args -> {
            RefreshRoomDescEvent event = gson.fromJson(args[0].toString(), RefreshRoomDescEvent.class);
            changeRoom(event.getDesc());
        }).on(NetworkEvents.SERVER_LOOTBAG_SPAWNED, args -> {
            eventBus.post(gson.fromJson(args[0].toString(), NewLootBagSpawnedEvent.class));
        });
        
    }
    
    @Subscribe
    public void listenSaveAndExitWorldEvent(SaveAndExitWorldEvent event){
        gameFileHandler.saveCharacterFile(null);
        
        eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
    }
    
    @Subscribe
    public void listenUpdatePositionEvent(UpdatePositionEvent event){
        try {
            mySocket.emit(NetworkEvents.CLIENT_UPDATE_POSITION, new JSONObject(gson.toJson(event, UpdatePositionEvent.class)));
        } catch (JSONException ex) {
            Logger.getLogger(GamePlayState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Subscribe
    public void listenUpdatePositionEvent(SpawnProjectileEvent event){
        try {
            mySocket.emit(NetworkEvents.CLIENT_SPAWN_PROJECTILE, new JSONObject(gson.toJson(event, SpawnProjectileEvent.class)));
        } catch (JSONException ex) {
            Logger.getLogger(GamePlayState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Subscribe
    public void listenRenderLootBagEvent(RenderLootBagEvent event){
        if(worldContainer != null){
            worldContainer.setLootToRender(event.getLootBag());
        }
    }
}
