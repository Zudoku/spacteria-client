package fingerprint.states;

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
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.inout.GameFileHandler;
import fingerprint.mainmenus.CharacterInfoContainer;
import fingerprint.mainmenus.CharacterSelectionController;
import fingerprint.networking.NetworkEvents;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.GiveSocketInfoEvent;
import fingerprint.states.events.SelectCharacterEvent;
import io.socket.client.Socket;
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
    
    private Socket socket;
    
    private int deleteCounter = 0;
    
    public CharacterSelectionState() {
        controller = new CharacterSelectionController();
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame caller)
            throws SlickException {
        CharacterInfoContainer createNewCharacter = new CharacterInfoContainer();
        createNewCharacter.setIsCreateNewCharDummy(true);
        availableChars.clear();
        availableChars.add(createNewCharacter);
        currentSelectionChar = createNewCharacter;
        
        controller.setFilesAmount(0);
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame caller, Graphics graphics)
            throws SlickException {
        
        renderingManager.drawCharacterSelection(graphics,currentSelectionChar, availableChars, deleteCounter);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame caller, int delta)
            throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        if(inputManager.isKeyBindPressed(KeyBindAction.LEFT,true) || inputManager.isKeyBindPressed(KeyBindAction.UP,true)){
            controller.left();
            currentSelectionChar = availableChars.get(controller.getSelection());
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.RIGHT,true) || inputManager.isKeyBindPressed(KeyBindAction.DOWN,true)){
            controller.right();
            currentSelectionChar = availableChars.get(controller.getSelection());
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.MENU,true)){
            selectCharacter();
        }
        if(inputManager.isKeyBindDown(KeyBindAction.DEBUG_TOGGLE,false) 
                && inputManager.isKeyBindDown(KeyBindAction.SKIP,false 
                && !currentSelectionChar.isIsCreateNewCharDummy())){
            deleteCounter += delta;
            if(deleteCounter > 2500){
                try {
                    JSONObject payload = new JSONObject();
                    payload.put("characterID", currentSelectionChar.getPlayerData().getUniqueid());
                    socket.emit(NetworkEvents.CLIENT_DELETE_CHARACTER, payload);
                    deleteCounter = 0;
                } catch (JSONException ex) {
                    Logger.getLogger(CharacterSelectionState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            deleteCounter = 0;
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT,true)){
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
        }
        currentSelectionChar.setMoreLeft(controller.getMoreLeft());
        currentSelectionChar.setMoreRight(controller.getMoreRight());

    }
    public void selectCharacter(){
        if(controller.getSelection() == 0){
            eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.CHARACTER_CREATION_ID));
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
    public void listenGiveSocketInfoEvent(GiveSocketInfoEvent event){
        if(event.getState() != getID()) {
            return;
        }

        CharacterInfoContainer createNewWorld = new CharacterInfoContainer();
        createNewWorld.setIsCreateNewCharDummy(true);
        availableChars.clear();
        availableChars.add(createNewWorld);
        
        this.socket = event.getSocket();
        this.initializeSocketToCharacterSelectionMode();
        this.requestForCharacters();
        new Thread(() -> {
            try {
                Thread.sleep(500);
                this.requestForCharacters();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();

    }
    
    
    private void initializeSocketToCharacterSelectionMode() {
        socket.on(NetworkEvents.SERVER_CHARACTERLIST, args -> {
            try {
                logger.log(Level.SEVERE, args[0].toString());
                JSONArray charsPayload =((JSONObject)args[0]).getJSONArray("chars");
                List<CharacterInfoContainer> charactersToAdd = new ArrayList<>();
                for(int y = 0; y < charsPayload.length(); y++){
                    try {
                        JSONObject charToAdd = charsPayload.getJSONObject(y);
                        try{
                            GCharacter character = gson.fromJson(charToAdd.toString(), GCharacter.class);
                            CharacterInfoContainer cic = new CharacterInfoContainer();
                            cic.setPlayerData(character);
                            charactersToAdd.add(cic);
                        } catch(Exception e){
                            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, e);
                        }


                    } catch (JSONException ex) {
                        Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                CharacterInfoContainer createNewCharacter = new CharacterInfoContainer();
                createNewCharacter.setIsCreateNewCharDummy(true);
                availableChars.clear();
                availableChars.add(createNewCharacter);
                availableChars.addAll(charactersToAdd);
                controller.setFilesAmount(availableChars.size() - 1);
                if(controller.getSelection() == 0){
                    controller.right();
                }
                currentSelectionChar = availableChars.get(controller.getSelection());
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
        socket.off(NetworkEvents.SERVER_CHARACTERLOAD_SUCCESS);
    }

}
