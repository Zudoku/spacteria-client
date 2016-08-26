/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.states;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.map.gameworld.CharacterSaveFile;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.mainmenus.serverlist.ServerListController;
import fingerprint.networking.NetworkEvents;
import fingerprint.rendering.RenderingManager;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.InitGameInfoEvent;
import fingerprint.states.events.SaveAndExitWorldEvent;
import fingerprint.states.events.SelectCharacterEvent;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 *
 * @author arska
 */
public class ServerListState extends BasicGameState{
    
    private static final Logger logger = Logger.getLogger(ServerListState.class.getName());
    
    private Socket socket;
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private InputManager inputManager;
    @Inject private Gson gson;
    
    private CharacterSaveFile myCharacter;
    
    private boolean identified = false;
    private ServerListController controller;
    private List<RoomDescription> rooms = new ArrayList<>();

    public ServerListState() {
        controller = new ServerListController();
    }
    
    @Override
    public int getID() {
        return State_IDs.SERVERLIST_ID;
    }

    @Override
    public void init(GameContainer gc,final StateBasedGame sbg) throws SlickException {
        try {
            socket = IO.socket("http://192.168.1.141:3590");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    
                }

            }).on(NetworkEvents.SERVER_DISPLAYROOMLIST, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //
                    rooms.clear();
                    JSONArray roomsPayload = (JSONArray)args[0];
                    for(int y = 0; y < roomsPayload.length(); y++){
                        try {
                            JSONObject roomToAdd = roomsPayload.getJSONObject(y);
                            try{
                                RoomDescription description = gson.fromJson(roomToAdd.toString(), RoomDescription.class);
                                rooms.add(description);
                            } catch(Exception e){
                                Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, e);
                            }
                            
                            
                        } catch (JSONException ex) {
                            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    controller.setRoomAmount(rooms.size());
                    
                }

            }).on(NetworkEvents.SERVER_JOINROOM, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //Go to gameplay state
                    JSONObject payload = (JSONObject) args[0];
                    try{
                        RoomDescription roomDescription = gson.fromJson(payload.toString(), RoomDescription.class);
                        eventBus.post(new InitGameInfoEvent(roomDescription, myCharacter.getPlayer(), socket.id(), socket));
                        eventBus.post(new ChangeStateEvent(getID(), State_IDs.GAME_PLAY_ID));
                    } catch(Exception e){
                        Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, e);
                    }
                    
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_SELECTION_ID));
                }

            });
            socket.connect();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        refreshGameList();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        renderingManager.drawServerList(grphcs, rooms, controller.getSelection());
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        //TODO: redo everything
        
        //TEMPFIX
        Input input = gc.getInput();
        if(inputManager.isKeyBindPressed(KeyBindAction.D,true)){
            menuPressed();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.UP,true)){
            controller.up();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.DOWN,true)){
            controller.down();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT,true)){
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.CHARACTER_SELECTION_ID));
        }
    }
    
    private void menuPressed(){
        int selection = controller.getSelection();
        if(selection == 0){
            socket.emit(NetworkEvents.CLIENT_MAKENEWROOM, new JSONObject());
        }else {
            JSONObject args = new JSONObject();
            try {
                args.put("name", rooms.get(selection-1).getName());
            } catch (JSONException ex) {
                Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
            }
            socket.emit(NetworkEvents.CLIENT_ASKTOJOINGAME, args);
        }
    }
    
    private void refreshGameList(){
        socket.emit(NetworkEvents.CLIENT_REFRESHROOMLIST,"");
    }
    @Subscribe
    public void listenSelectCharacterEvent(SelectCharacterEvent event){
        myCharacter = event.getCharacterInfo();
        identifyToServer();
    }
    
    public void identifyToServer(){
        JSONObject identifyObject = new JSONObject();
        try {
            identifyObject.put("type", "game-client");
            GsonBuilder ga=new GsonBuilder();
            Gson gson = ga.create();
            JSONObject playerData = new JSONObject(gson.toJson(myCharacter.getPlayer()));
            identifyObject.put("player", playerData);
        } catch (JSONException ex) {
            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
        }

        socket.emit(NetworkEvents.CLIENT_IDENTIFY, identifyObject, new Ack() {
            @Override
            public void call(Object... args) {
                identified = true;
            }
        });
    }
}
