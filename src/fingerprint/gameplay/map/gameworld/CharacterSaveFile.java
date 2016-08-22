package fingerprint.gameplay.map.gameworld;

import fingerprint.gameplay.objects.player.Player;

public class CharacterSaveFile {
    
    private CharacterMetaData metaData;
    private Player player;
    
    public CharacterSaveFile() {
        
    }
    public CharacterMetaData getMetaData() {
        return metaData;
    }
    
    public void setMetaData(CharacterMetaData metaData) {
        this.metaData = metaData;
    }
    
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
}
