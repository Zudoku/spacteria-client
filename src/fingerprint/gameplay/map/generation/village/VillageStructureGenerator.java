package fingerprint.gameplay.map.generation.village;

import java.util.Random;
import java.util.logging.Logger;

import fingerprint.gameplay.map.blocks.BlockManager;
import fingerprint.gameplay.map.blocks.BlockRendering;
import fingerprint.gameplay.map.generation.StructureContainer;

public class VillageStructureGenerator {
    private static final Logger logger = Logger.getLogger(VillageStructureGenerator.class.getName());
    private Random random;
    public VillageStructureGenerator() {
        random = new Random();
    }
    
    public StructureContainer getWoodenHouseHorizontal(int width,int height){
        byte[][] functional = new byte[width][height];
        short[][] rendering = new short[width*2][height];
        
        //Fill it with wood
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                functional[x][y] = BlockManager.UnWalkableBasic;
                rendering[x*2][y] = BlockRendering.DIRT;
            }
        }
        //Make a roof
        for (int x = 0; x < width; x++) {
            rendering[x*2][0] = BlockRendering.TILE_ROOF;
        }
        //Make door
        int doorX = 1 + random.nextInt(width -2);
        rendering[doorX*2][height-1] = BlockRendering.VILLAGE_FENCE;
        rendering[doorX*2][height-2] = BlockRendering.VILLAGE_FENCE;
        
        StructureContainer house = new StructureContainer(functional, rendering, width, height);
        
        return house;
        
    }
    
    public StructureContainer getPlayerHouseHorizontal(){
        int height = 3;
        int width = 6;
        
        byte[][] functional = new byte[width][height];
        short[][] rendering = new short[width*2][height];
        
        //Fill it with wood
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                functional[x][y] = BlockManager.UnWalkableBasic;
                rendering[x*2][y] = BlockRendering.DIRT;
            }
        }
        //Make a roof
        for (int x = 0; x < width; x++) {
            rendering[x*2][0] = BlockRendering.TILE_ROOF;
            rendering[x*2][1] = BlockRendering.TILE_ROOF_GUTTER;
        }
        //Make door
        int doorX = 1 + random.nextInt(width -2);
        rendering[1*2+1][height-1] = BlockRendering.DOOR_BLUE;
        
        //Make windows
        rendering[2*2+1][height-1] = BlockRendering.WINDOW_SMALL_1;
        rendering[4*2+1][height-1] = BlockRendering.WINDOW_SMALL_1;
        
        
        StructureContainer house = new StructureContainer(functional, rendering, width, height);
        
        return house;
        
    }
    
    public StructureContainer getNPCHouseFoundation(){
        int height = 8;
        int width = 8;
        
        byte[][] functional = new byte[width][height];
        short[][] rendering = new short[width*2][height];
        
        
        
        
        
        StructureContainer foundation = new StructureContainer(functional, rendering, width, height);
        return foundation;
    }
    
    
}
