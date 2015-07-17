package fingerprint.gameplay.map.generation;

import java.util.List;
import java.util.logging.Logger;

import fingerprint.gameplay.map.gameworld.GameWorld;
import fingerprint.gameplay.map.gameworld.GameWorldMetaData;

public class GameWorldBuilder {
    private static final Logger logger = Logger.getLogger(GameWorldBuilder.class.getName());
    private AreaGenerator areaGenerator;
    public GameWorldBuilder() {
        areaGenerator = new AreaGenerator();
    }
    public GameWorld generateNewWorld(GameWorldMetaData metaData) {
        GameWorld createdWorld = new GameWorld();
        //METADATA
        createdWorld.setMetaData(metaData);
        createdWorld.setMap(areaGenerator.generateAreas(metaData.filename));
        
        return createdWorld;
    }
    
}
