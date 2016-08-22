package fingerprint.states.events;

import fingerprint.gameplay.map.gameworld.CharacterSaveFile;

public class SelectCharacterEvent {
    private CharacterSaveFile world;
    public SelectCharacterEvent(CharacterSaveFile world) {
        this.world = world;
    }
    public CharacterSaveFile getWorld() {
        return world;
    }
}
