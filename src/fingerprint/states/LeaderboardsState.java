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
import fingerprint.networking.NetworkEvents;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.rendering.manager.UIRenderingUtil;
import static fingerprint.states.LoginState.SOCKETSTATUS;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.GiveSocketInfoEvent;
import io.socket.client.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created Dec 20, 2017
 * @author arska
 */
public class LeaderboardsState extends BasicGameState {
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    @Inject private InputManager inputManager;
    
    private Socket socket;
    
    private boolean leaderboards = true;
    
    private JSONArray characters;
    private JSONArray bosskills;

    @Override
    public int getID() {
        return State_IDs.LEADERBOARDS_ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        grphcs.setColor(Color.yellow);
        grphcs.setFont(UIRenderingUtil.giganticVerdanaFont);
        grphcs.drawString("TOP 10 Characters", 850, 50);
        grphcs.drawString("TOP 10 Boss kills", 100, 50);
        grphcs.setFont(UIRenderingUtil.mediumVerdanaFont);
        
        for(int i = 0; i < 10; i++){
            
            if(characters != null && characters.length() > i){
                try {
                    JSONObject character = characters.getJSONObject(i);
                    grphcs.drawString((i + 1) + ". " + character.getString("name") + " (Level: " + character.getInt("level") + ")", 800, 100 + (i * 50));
                } catch (JSONException ex) {
                    Logger.getLogger(LeaderboardsState.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
            if(bosskills != null && bosskills.length() > i){
                try {
                    JSONObject bosskill = bosskills.getJSONObject(i);
                    grphcs.drawString((i + 1) + ". BOSS:" + bosskill.getString("bossid") + " (Difficulty: " + bosskill.getInt("difficulty") + ", Time: " + bosskill.getInt("killtime") + ")", 50, 100 + (i * 50));
                    grphcs.drawString(bosskill.getJSONArray("participants").toString() + " " + bosskill.getString("achievedat"), 50, 125 + (i * 50));
                } catch (JSONException ex) {
                    Logger.getLogger(LeaderboardsState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
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
        setUpLeaderboardsSocket();
        requestLeaderboards();
    }

    private void setUpLeaderboardsSocket(){
        this.socket.on(NetworkEvents.SERVER_DISPLAYLEADERBOARDS, (Object... args) -> {
            try {
                JSONObject payload = (JSONObject) args[0];
                this.bosskills = payload.getJSONArray("leaderboards");
                this.characters = payload.getJSONArray("characters");
            } catch (JSONException ex) {
                Logger.getLogger(LeaderboardsState.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    private void cleanUpSocket(){
        socket.off(NetworkEvents.SERVER_DISPLAYLEADERBOARDS);
    }
    private void requestLeaderboards(){
        socket.emit(NetworkEvents.CLIENT_REFRESHLEADERBOARDS, new JSONObject());
    }
}
