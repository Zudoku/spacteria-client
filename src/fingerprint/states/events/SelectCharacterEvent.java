package fingerprint.states.events;

import fingerprint.gameplay.objects.player.GCharacter;

public class SelectCharacterEvent {
    private GCharacter characterInfo;
    public SelectCharacterEvent(GCharacter world) {
        this.characterInfo = world;
    }
    public GCharacter getCharacterInfo() {
        return characterInfo;
    }
}
