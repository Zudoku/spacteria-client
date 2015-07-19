package fingerprint.gameplay.map.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMapPlus;

import fingerprint.gameplay.map.FunctionalMap;
import fingerprint.gameplay.map.blocks.BlockManager;
import fingerprint.gameplay.map.generation.village.VillageGenerator;
import fingerprint.gameplay.map.village.Village;
import fingerprint.inout.TileFileHandler;
import fingerprint.rendering.map.TilemapRenderer;

public class AreaGenerator {
    private static final Logger logger = Logger.getLogger(AreaGenerator.class.getName());
    
    private final int WORLD_SIZE = FunctionalMap.SIZE;
    
    private int idCounter = 0;
    private AreaShapeGenerator shapeGenerator = new AreaShapeGenerator();
    private AreaStructureGenerator structureGenerator = new AreaStructureGenerator();
    private TileFileHandler tileFileHandler;
    private Village village;
    private VillageGenerator villageGenerator;
    
    
    public AreaGenerator() { 
        villageGenerator = new VillageGenerator();
    }
    public FunctionalMap generateAreas(String worldName){
        FunctionalMap map = new FunctionalMap(new byte[FunctionalMap.SIZE*FunctionalMap.SIZE]);
        tileFileHandler = new TileFileHandler();
        tileFileHandler.init(worldName);
        
        logger.log(Level.INFO,"Generating Village...");
        map = villageGenerator.generateVillage(map, tileFileHandler);
        
        return map;
    }
    
    

}
