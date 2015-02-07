package fingerprint.gameplay.map.gameworld;

import java.util.logging.Logger;

public class GameWorldContainer {
    private static final Logger logger = Logger.getLogger(GameWorldContainer.class.getName());
    private GameWorld world;
    
    public GameWorldContainer() {
        
        
    }
    public void setWorld(GameWorld world) {
        this.world = world;
    }
    
    
}
