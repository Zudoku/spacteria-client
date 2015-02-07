package fingerprint.gameplay.map.gameworld;

import java.util.ArrayList;
import java.util.List;

public class GameWorldMetaData {
    public String filename;
    public int fileVersion;
    public long lastPlayed;
    public List<Integer> oldVersions;
    
    public GameWorldMetaData() {
        oldVersions = new ArrayList<>();
    }
}
