package fingerprint.gameplay.map.gameworld;

import java.util.ArrayList;
import java.util.List;

public class CharacterMetaData {
    public String filename;
    public int fileVersion;
    public long lastPlayed;
    public List<Integer> oldVersions;
    
    public CharacterMetaData() {
        oldVersions = new ArrayList<>();
    }
}
