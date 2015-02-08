package fingerprint.gameplay.map.gameworld;

import java.util.List;
import java.util.logging.Logger;

public class GameWorldContainer {
    private static final Logger logger = Logger.getLogger(GameWorldContainer.class.getName());
    private GameWorld world;
    private int currentAreaID = 0;
    
    public GameWorldContainer() {
        
        
    }
    public void setWorld(GameWorld world) {
        this.world = world;
    }
    public List<int[][]> getTileMapToDraw(){
        return world.getAreas().get(currentAreaID).getTileLayers();
    }
    
}
