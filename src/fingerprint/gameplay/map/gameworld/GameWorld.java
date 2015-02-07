package fingerprint.gameplay.map.gameworld;

import java.util.ArrayList;
import java.util.List;

import fingerprint.gameplay.map.GameArea;
import fingerprint.gameplay.map.village.Village;
import fingerprint.gameplay.objects.GameObject;
import fingerprint.gameplay.time.Clock;

public class GameWorld {
    private List<GameArea> areas = new ArrayList<GameArea>();
    private Village village;
    private List<GameObject> objects;
    private GameWorldMetaData metaData;
    private Clock worldClock;
    
    public List<GameArea> getAreas() {
        return areas;
    }
    public void setAreas(List<GameArea> areas) {
        this.areas = areas;
    }
    public void setVillage(Village village) {
        this.village = village;
    }
    public Village getVillage() {
        return village;
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
}
