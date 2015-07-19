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
        short[][] rendering = new short[width][height];
        
        //Fill it with wood
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                functional[x][y] = BlockManager.UnWalkableBasic;
                rendering[x][y] = BlockRendering.WOODEN_PLANK;
            }
        }
        //Make a roof
        for (int x = 0; x < width; x++) {
            rendering[x][0] = BlockRendering.WOODEN_ROOF;
        }
        //Make door
        int doorX = 1 + random.nextInt(width -2);
        rendering[doorX][height-1] = BlockRendering.WOODEN_DOOR;
        rendering[doorX][height-2] = BlockRendering.WOODEN_DOOR;
        
        StructureContainer house = new StructureContainer(functional, rendering, width, height);
        
        return house;
        
    }
}
