package fingerprint.states;

import java.util.logging.Logger;

import fingerprint.gameplay.objects.events.*;
import fingerprint.gameplay.objects.events.gui.*;
import fingerprint.networking.events.*;
import fingerprint.rendering.gui.event.RenderItemDescriptionEvent;
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
import fingerprint.gameplay.items.GameItem;
import fingerprint.gameplay.map.gameworld.GameWorldContainer;
import fingerprint.gameplay.objects.lootbag.GameItemWrapper;
import fingerprint.gameplay.objects.lootbag.LootBag;
import fingerprint.gameplay.objects.player.DummyCharacter;
import fingerprint.gameplay.objects.projectiles.NewProjectileSpawnedEvent;
import fingerprint.gameplay.objects.projectiles.SpawnProjectileEvent;
import fingerprint.inout.GameFileHandler;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.networking.NetworkEvents;
import fingerprint.rendering.util.GamePlayRenderingInformation;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.rendering.gui.event.EquipmentClickEvent;
import fingerprint.rendering.gui.event.InventoryClickEvent;
import fingerprint.rendering.gui.event.LootBagClickEvent;
import fingerprint.sound.PlaySoundEvent;
import fingerprint.sound.SoundEffect;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.InitGameInfoEvent;
import fingerprint.states.events.SaveAndExitWorldEvent;
import io.socket.client.Socket;

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

    private GameItem itemToRenderHover = null;
    
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
        gri.setMyName(worldContainer.getMyName() );
        gri.setLevel(worldContainer.getMyLevel());
        gri.setExperience(worldContainer.getMyExp());
        gri.setMyStats(worldContainer.getMyStats());
        gri.setMapName(worldContainer.getMapName());
        gri.setCharClass(worldContainer.getMyClass());
        gri.setLootToRender(worldContainer.getLootToRender());
        gri.setEquipmentToRender(worldContainer.getCharacterEquipment());
        gri.setInventoryToRender(worldContainer.getInventoryToRender());
        gri.setPortalToRender(worldContainer.getPortalToRender());
        gri.setHoverGameItem(itemToRenderHover);
        //worldContainer.
        renderingManager.drawGamePlay(graphics, gc, debugInfo, gri);
        //renderingManager.drawDebugGamePlay(graphics);
        
        if(!worldContainer.isThereWasLoot()) {
            worldContainer.setLootToRender(null);
        } else {
            worldContainer.setThereWasLoot(false);
        }

        if(!worldContainer.isThereWasPortal()) {
            worldContainer.setPortalToRender(null);
        } else {
            worldContainer.setThereWasPortal(false);
        }
        
        itemToRenderHover = null;
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
        inputManager.setUseGUIInputHandler(true);
        initialized = true;
    }
    
    private void  changeRoom(RoomDescription description){
        DummyCharacter ourOwn = null;
        //Remove our own dummy
        for(DummyCharacter dp : description.getPlayers()){
            if(dp.getId().equals(myID)){
                ourOwn = dp;
                
            }
        }
        
        description.getPlayers().remove(ourOwn);
        eventBus.post(new PlaySoundEvent(SoundEffect.TELEPORT));
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
        }).on(NetworkEvents.SERVER_UPDATE_LOOTBAG_STATUS, args -> {
            eventBus.post(gson.fromJson(args[0].toString(), ModifyLootBagEvent.class));
        }).on(NetworkEvents.SERVER_UPDATE_CHARACTER_STATUS, args -> {
            ModifyCharacterEvent event = gson.fromJson(args[0].toString(), ModifyCharacterEvent.class);
            worldContainer.characterStatusUpdate(event);
        }).on(NetworkEvents.SERVER_LOAD_NEW_MAP, args -> {
            LoadNewMapEvent event = gson.fromJson(args[0].toString(), LoadNewMapEvent.class);
            gameFileHandler.saveTilemapFile(event);
            mySocket.emit(NetworkEvents.CLIENT_MAP_LOADED, new JSONObject());
        });
        
    }
    
    @Subscribe
    public void listenSaveAndExitWorldEvent(SaveAndExitWorldEvent event){
        gameFileHandler.saveTilemapFile(null);
        
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
    public void listenSpawnProjectileEvent(SpawnProjectileEvent event){
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
            worldContainer.setThereWasLoot(true);
        }
    }


    @Subscribe
    public void listenRenderPortalEvent(RenderEnterPortalEvent event){
        if(worldContainer != null){
            worldContainer.setPortalToRender(event.getPortal());
            worldContainer.setThereWasPortal(true);
        }
    }
    
    @Subscribe
    public void listenLootBagClickEvent(LootBagClickEvent event) {
        LootBag loot = worldContainer.getLootToRender();
        if(loot != null) {
            if(loot.getItems().size() > event.getIndex()) {
                try {
                    if(loot.getItems().get(event.getIndex()).getUniqueid() == -1) {
                        //TEMP FIXME TODO: 
                        return;
                    }
                    LootItemEvent payload = new LootItemEvent(event.getIndex(), loot.getGuid(), loot.getItems().get(event.getIndex()).getUniqueid(),
                            loot.getItems().get(event.getIndex()).getAmount());
                    mySocket.emit(NetworkEvents.CLIENT_LOOT_ITEM, new JSONObject(gson.toJson(payload, LootItemEvent.class)));
                } catch (JSONException ex) {
                    Logger.getLogger(GamePlayState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Subscribe
    public void listenEnterPortalEvent(EnterPortalEvent event) {
        try {
            mySocket.emit(NetworkEvents.CLIENT_ENTER_PORTAL, new JSONObject(gson.toJson(event, EnterPortalEvent.class)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Subscribe
    public void listenEquipmentClickEvent(EquipmentClickEvent event) {
        GameItem item = worldContainer.getCharacterEquipment().getItem(event.getIndex());
        if(item != null && event.isRightclick()) {
            try {
                mySocket.emit(NetworkEvents.CLIENT_UNEQUIP_ITEM, new JSONObject(gson.toJson(new UnEquipItemEvent(event.getIndex()), UnEquipItemEvent.class)));
            } catch (JSONException ex) {
                Logger.getLogger(GamePlayState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    @Subscribe
    public void listenInventoryClickEvent(InventoryClickEvent event) {
        GameItemWrapper item = worldContainer.getInventoryToRender().getItem(event.getIndex());
        if (item != null && event.isRightclick()) {
            try {
                mySocket.emit(NetworkEvents.CLIENT_EQUIP_ITEM, new JSONObject(gson.toJson(new EquipItemEvent(event.getIndex()), EquipItemEvent.class)));
            } catch (JSONException ex) {
                Logger.getLogger(GamePlayState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (item != null && event.getMousebutton() == 2) {
            try {
                mySocket.emit(NetworkEvents.CLIENT_DROP_ITEM, new JSONObject(gson.toJson(new DropItemEvent(event.getIndex()), DropItemEvent.class)));
            } catch (JSONException ex) {
                Logger.getLogger(GamePlayState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Subscribe
    public void listenRenderItemDescriptionEvent(RenderItemDescriptionEvent event) {

        GameItemWrapper wrapper;
        switch (event.getFrom()) {
            case EQUIPMENT:
                itemToRenderHover = worldContainer.getCharacterEquipment().getItem(event.getIndex());
                break;

            case LOOTBAG:
                if(worldContainer.getLootToRender() != null) {
                    wrapper = worldContainer.getLootToRender().getItems().get(event.getIndex());
                    if (wrapper != null) {
                        itemToRenderHover = wrapper.getData();
                    }
                }
                break;

            case INVENTORY:
                wrapper = worldContainer.getInventoryToRender().getItem(event.getIndex() + 1);
                if (wrapper != null) {
                    itemToRenderHover = wrapper.getData();
                }
                break;
        }
    }
}
