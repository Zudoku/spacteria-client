package fingerprint.gameplay.map;

import java.util.List;

public class GameArea {
    private int areaID;
    private GameAreaType areaType;
    private List<int[][]> tileLayers;
    public GameArea(List<int[][]> tileLayers,GameAreaType areaType,int areaID) {
        this.tileLayers = tileLayers;
        this.areaType = areaType;
        this.areaID = areaID;
    }
    public List<int[][]> getTileLayers() {
        return tileLayers;
    }
    public GameAreaType getAreaType() {
        return areaType;
    }
    public int getAreaID() {
        return areaID;
    }
}
