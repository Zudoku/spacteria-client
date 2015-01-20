package fingerprint.gameplay.map.gameworld;

import java.util.ArrayList;
import java.util.List;

import fingerprint.gameplay.map.GameArea;
import fingerprint.gameplay.map.village.Village;

public class GameWorld {
    private List<GameArea> areas = new ArrayList<GameArea>();
    private Village village;
    
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
}
