package fingerprint.gameplay.loader;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Singleton;

import fingerprint.gameplay.map.gameworld.GameWorld;
import fingerprint.gameplay.objects.player.Player;
import fingerprint.gameplay.objects.player.PlayerBuilder;
import fingerprint.states.events.PlayerOutlookEvent;
import fingerprint.states.events.SelectPlayableWorldEvent;

@Singleton
public class PreGamePlayStateLoader {
    private GameWorld world;
    private Player player;
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
        on = true;
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
        
        if(world.getPlayer() != null){
            player = world.getPlayer();
            characterDone = true;
        }
    }
    public Player getPlayer() {
        return player;
    }
    @Subscribe
    public void listenPlayerOutlookEvent(PlayerOutlookEvent event){
        if(event.isOnWorld()){
            characterDone = true;
        }else{
            player = PlayerBuilder.spawnGenericPlayer();
            characterDone = true;
        }
    }
    
}
