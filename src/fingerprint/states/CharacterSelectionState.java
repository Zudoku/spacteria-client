package fingerprint.states;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fingerprint.gameplay.objects.player.StatContainer;
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
import fingerprint.gameplay.map.gameworld.CharacterSaveFile;
import fingerprint.gameplay.objects.events.DeleteEntityEvent;
import fingerprint.gameplay.objects.player.DummyCharacter;
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.gameplay.objects.projectiles.NewProjectileSpawnedEvent;
import fingerprint.inout.FileUtil;
import fingerprint.inout.GameFileHandler;
import fingerprint.mainmenus.CharacterInfoContainer;
import fingerprint.mainmenus.CharacterSelectionController;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.networking.NetworkEvents;
import fingerprint.networking.events.CorrectNPCPositionEvent;
import fingerprint.networking.events.PlayerJoinedEvent;
import fingerprint.networking.events.PlayerLeftEvent;
import fingerprint.networking.events.RefreshRoomDescEvent;
import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.GiveSocketInfoEvent;
import fingerprint.states.events.SelectCharacterEvent;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CharacterSelectionState extends BasicGameState {
    private static final Logger logger = Logger.getLogger(CharacterSelectionState.class.getName());
    
    private CharacterInfoContainer currentSelectionChar;
    private List<CharacterInfoContainer> availableChars = new ArrayList<>();
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private GameFileHandler fileHandler;
    @Inject private InputManager inputManager;
    @Inject private Gson gson;
    private CharacterSelectionController controller;
    
    private File[] savedChars;
    private Socket socket;
    
    public CharacterSelectionState() {
        controller = new CharacterSelectionController();
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        CharacterInfoContainer createNewWorld = new CharacterInfoContainer();
        createNewWorld.setIsCreateNewCharDummy(true);
        availableChars.add(createNewWorld);
        currentSelectionChar = createNewWorld;
        
        controller.setFilesAmount(0);
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        
        renderingManager.drawWorldSelection(graphics,currentSelectionChar);

    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        if(inputManager.isKeyBindPressed(KeyBindAction.LEFT,true)){
            controller.left();
            currentSelectionChar = availableChars.get(controller.getSelection());
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.RIGHT,true)){
            controller.right();
            currentSelectionChar = availableChars.get(controller.getSelection());
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.D,true)){
            selectCharacter();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT,true)){
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
        }
        currentSelectionChar.setMoreLeft(controller.getMoreLeft());
        currentSelectionChar.setMoreRight(controller.getMoreRight());

    }
    public void selectCharacter(){
        if(controller.getSelection() == 0){
            //TODO: FIX THIS 
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_CREATION_ID));
        }else{
            JSONObject payload = new JSONObject();
            try {
                payload.put("charID", currentSelectionChar.getPlayerData().getUniqueid());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit(NetworkEvents.CLIENT_LOAD_CHARACTER, payload);
        }
        
    }

    @Subscribe
    public void listenInitGameInfoEvent(GiveSocketInfoEvent event){
        if(event.getState() != getID()) {
            return;
        }

        this.socket = event.getSocket();
        this.initializeSocketToCharacterSelectionMode();
        this.requestForCharacters();
    }
    
    
    private void initializeSocketToCharacterSelectionMode() {
        socket.on(NetworkEvents.SERVER_CHARACTERLIST, args -> {
            try {
                logger.log(Level.SEVERE, args[0].toString());
                JSONArray charsPayload =((JSONObject)args[0]).getJSONArray("chars");
                for(int y = 0; y < charsPayload.length(); y++){
                    try {
                        JSONObject charToAdd = charsPayload.getJSONObject(y);
                        try{
                            GCharacter character = gson.fromJson(charToAdd.toString(), GCharacter.class);
                            CharacterInfoContainer cic = new CharacterInfoContainer();
                            cic.setPlayerData(character);
                            availableChars.add(cic);
                        } catch(Exception e){
                            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, e);
                        }


                    } catch (JSONException ex) {
                        Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                controller.setFilesAmount(availableChars.size() - 1);
            } catch (JSONException ex) {
                Logger.getLogger(CharacterSelectionState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).on(NetworkEvents.SERVER_CHARACTERLOAD_SUCCESS, args -> {
            try {
                JSONObject charToAdd = ((JSONObject) args[0]).getJSONObject("character");
                GCharacter character = gson.fromJson(charToAdd.toString(), GCharacter.class);
                JSONObject statsToAdd = ((JSONObject) args[0]).getJSONObject("stats");
                StatContainer stats = gson.fromJson(statsToAdd.toString(), StatContainer.class);

                character.getStatManager().setStats(stats);

                eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.SERVERLIST_ID));
                eventBus.post(new SelectCharacterEvent(character));
                eventBus.post(new ChangeStateEvent(getID(), State_IDs.SERVERLIST_ID));
                cleanUpSocket();
            } catch (JSONException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        });
    }
    
    private void requestForCharacters(){
        socket.emit(NetworkEvents.CLIENT_CHARACTERLIST_REQUEST, "");
    }
    
    @Override
    public int getID() {
        return State_IDs.CHARACTER_SELECTION_ID;
    }
    
    private void cleanUpSocket() {
        socket.off(NetworkEvents.SERVER_CHARACTERLIST);
    }

}
