package fingerprint.gameplay.loader;

import com.google.common.eventbus.Subscribe;

import fingerprint.gameplay.map.gameworld.GameWorld;
import fingerprint.states.events.PlayerOutlookEvent;
import fingerprint.states.events.SelectPlayableWorldEvent;

public class PreGamePlayStateLoader {
    private GameWorld world;
    private boolean characterDone = false;
    private boolean on = true;
    
    public PreGamePlayStateLoader() {
        
    }
    public GameWorld getWorld() {
        return world;
    }
    public boolean isCharacterDone() {
        return characterDone;
    }
    public void setCharacterDone(boolean characterDone) {
        this.characterDone = characterDone;
    }
    public void setWorld(GameWorld world) {
        this.world = world;
    }
    public void reset(){
        world = null;
        characterDone = false;
    }
    public boolean isOn() {
        return on;
    }
    public void setOn(boolean on) {
        this.on = on;
    }
    @Subscribe
    public void listenSelectPlayableWorldEvent(SelectPlayableWorldEvent event){
        world = event.getWorld();
    }
    @Subscribe
    public void listenPlayerOutlookEvent(PlayerOutlookEvent event){
        if(event.isOnWorld()){
            characterDone = true;
        }else{
            //TODO: scrape data here
            characterDone = true;
        }
    }
}
