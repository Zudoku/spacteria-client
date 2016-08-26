package fingerprint.states.events;

import fingerprint.gameplay.map.gameworld.CharacterSaveFile;

public class SelectCharacterEvent {
    private CharacterSaveFile characterInfo;
    public SelectCharacterEvent(CharacterSaveFile world) {
        this.characterInfo = world;
    }
    public CharacterSaveFile getCharacterInfo() {
        return characterInfo;
    }
}
