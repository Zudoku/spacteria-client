package fingerprint.gameplay.map.gameworld;

import fingerprint.gameplay.objects.player.GCharacter;

public class CharacterSaveFile {
    
    private CharacterMetaData metaData;
    private GCharacter player;
    
    public CharacterSaveFile() {
        
    }
    public CharacterMetaData getMetaData() {
        return metaData;
    }
    
    public void setMetaData(CharacterMetaData metaData) {
        this.metaData = metaData;
    }
    
    public GCharacter getPlayer() {
        return player;
    }
    public void setPlayer(GCharacter player) {
        this.player = player;
    }
}
