package fingerprint.states.events;

import fingerprint.gameplay.map.gameworld.GameWorld;

public class SelectPlayableWorldEvent {
    private GameWorld world;
    public SelectPlayableWorldEvent(GameWorld world) {
        this.world = world;
    }
    public GameWorld getWorld() {
        return world;
    }
}
