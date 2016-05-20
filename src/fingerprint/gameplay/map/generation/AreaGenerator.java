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
import fingerprint.gameplay.map.blocks.BlockRendering;
import fingerprint.gameplay.map.generation.village.VillageGenerator;
import fingerprint.gameplay.map.village.Village;
import fingerprint.inout.TileFileHandler;
import fingerprint.rendering.map.TilemapRenderer;

public class AreaGenerator {
    private static final Logger logger = Logger.getLogger(AreaGenerator.class.getName());
    
    private final int WORLD_SIZE = FunctionalMap.SIZE;
    private final static int LAYERS = TileFileHandler.LAYERS;
    
    private int idCounter = 0;
    private AreaShapeGenerator shapeGenerator = new AreaShapeGenerator();
    private AreaStructureGenerator structureGenerator = new AreaStructureGenerator();
    private TileFileHandler tileFileHandler;
    private Village village;
    private VillageGenerator villageGenerator;
    private Random random;
    
    
    
    
    private static final int  SPAWN_RATE_ROCK_SMOOTH = 80;
    private static final int  SPAWN_RATE_ROCK_SHARP = 80;
    
    
    
    
    
    public AreaGenerator() { 
        villageGenerator = new VillageGenerator();
        random = new Random();
    }
    public FunctionalMap generateAreas(String worldName){
        FunctionalMap map = new FunctionalMap(new byte[FunctionalMap.SIZE*FunctionalMap.SIZE]);
        tileFileHandler = new TileFileHandler();
        tileFileHandler.init(worldName);
        logger.log(Level.INFO,"Laying out grass...");
        layoutGrass();
        
        logger.log(Level.INFO,"Laying out dirt...");
        layoutDirt(map);
        
        logger.log(Level.INFO,"Laying out plants...");
        layoutPlants(map);
        
        return map;
    }
    
    private void layoutPlants(FunctionalMap map) {
        
        Random r = new Random();
        
        Byte[][] functionalData = map.getData();
        
        int layer = 1;
        int SLICE_HEIGHT = 1;
        int seed = r.nextInt(10000);
        
        for(int y = 0; y < WORLD_SIZE ; y ++){
            short[][] tempMapData = tileFileHandler.getPartOfMap(0, y, WORLD_SIZE, SLICE_HEIGHT);
            float[][] tempNoiseData = new float[WORLD_SIZE][1];
            for(int x = 0; x < WORLD_SIZE ; x ++){
                double value = shapeGenerator.getNoiseValue(x, y, WORLD_SIZE, WORLD_SIZE, seed, 100);
                tempNoiseData[x][0] = (float)value;
            }
            tempNoiseData = shapeGenerator.toUnitArray(tempNoiseData, WORLD_SIZE, 1);
            for(int x = 0; x < WORLD_SIZE ; x ++){
                if(functionalData[x][y] == 0){
                    float value = tempNoiseData[x][0];
                    
                    if(value > 0.8 && value < 0.9){
                        tempMapData[x * LAYERS  + layer][0] = BlockRendering.BUSH;
                        functionalData[x][y] = BlockManager.Bush;
                    } else if(value > 0.9){
                        tempMapData[x * LAYERS  + layer][0] = BlockRendering.TREE;
                        functionalData[x][y] = BlockManager.Tree;
                    } else if(value < 0.15){
                        tempMapData[x * LAYERS  + layer][0] = BlockRendering.WATER;
                        functionalData[x][y] = BlockManager.Water;
                    } else if(r.nextInt(SPAWN_RATE_ROCK_SMOOTH) == 0){
                        tempMapData[x * LAYERS  + layer][0] = BlockRendering.ROCK_SMOOTH;
                        functionalData[x][y] = BlockManager.Rock_Smooth;
                    } else if(r.nextInt(SPAWN_RATE_ROCK_SHARP) == 0){
                        tempMapData[x * LAYERS  + layer][0] = BlockRendering.ROCK_SHARP;
                        functionalData[x][y] = BlockManager.Rock_Sharp;
                    }
                    
                }
            }

            tileFileHandler.writeMap(tempMapData, 0, y, WORLD_SIZE, SLICE_HEIGHT, false);
            
        }
        
        map.setData(functionalData);
        
    }
    
    private void layoutDirt(FunctionalMap map) {
        
        int shapeSize = 100;
        int layer = 0;
        Byte[][] functionalData = map.getData();
        for(int t = 0 ; t < 30 ; t++){
            
            int startX = random.nextInt(WORLD_SIZE - (shapeSize * 2)) + shapeSize;
            int startY = random.nextInt(WORLD_SIZE - (shapeSize * 2)) + shapeSize;
            
            int [][] mask = shapeGenerator.maskGeneration(shapeSize,shapeSize, shapeGenerator.GRAINY_AREA_SMOOTHNESS, shapeGenerator.SHORT_MASK_CUTOFF,false);
            
            short[][] tempMapData = tileFileHandler.getPartOfMap(startX, startY, shapeSize, shapeSize);
            for(int y = 0; y < shapeSize ; y++){
                for(int x = 0; x < shapeSize ; x++){
                    if(mask[x][y] == 1){
                        tempMapData[x * LAYERS + layer][y] = BlockRendering.DIRT;
                        functionalData[startX + x][startY + y] = BlockManager.Dirt;
                    }
                }
            }
            
            tileFileHandler.writeMap(tempMapData, startX, startY, shapeSize, shapeSize, false);
        }
        map.setData(functionalData);
    }
    
    private void layoutGrass(){
        
        for (int y = 0; y < WORLD_SIZE; y++) {
            short[][] slice = new short[WORLD_SIZE*2][1];
            for (int x = 0; x < WORLD_SIZE*2; x+=2) {
                slice[x][0] = BlockRendering.GRASS;
            }
            
            tileFileHandler.writeMap(slice, 0, y, WORLD_SIZE, 1,false);
        }
        
    }
    
    
    

}
