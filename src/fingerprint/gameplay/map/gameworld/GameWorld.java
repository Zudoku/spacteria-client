package fingerprint.gameplay.map.gameworld;

import java.util.ArrayList;
import java.util.List;

import javax.jws.Oneway;

import fingerprint.gameplay.map.FunctionalMap;
import fingerprint.gameplay.map.village.Village;
import fingerprint.gameplay.objects.GameObject;
import fingerprint.gameplay.objects.player.Player;
import fingerprint.gameplay.time.Clock;

public class GameWorld {
    private transient FunctionalMap map;
    
    private List<GameObject> objects; //not including player
    private GameWorldMetaData metaData;
    private Clock worldClock;
    private Player player;
    
    public GameWorld() {
        objects = new ArrayList<>();
    }
    public GameWorldMetaData getMetaData() {
        return metaData;
    }
    public List<GameObject> getObjects() {
        return objects;
    }
    public void setMetaData(GameWorldMetaData metaData) {
        this.metaData = metaData;
    }
    public void setObjects(List<GameObject> objects) {
        this.objects = objects;
    }
    public Clock getWorldClock() {
        return worldClock;
    }
    public void setWorldClock(Clock worldClock) {
        this.worldClock = worldClock;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public FunctionalMap getMap() {
        return map;
    }
    public void setMap(FunctionalMap map) {
        this.map = map;
    }
}
