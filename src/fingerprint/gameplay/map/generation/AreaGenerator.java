package fingerprint.gameplay.map.generation;

import java.util.ArrayList;
import java.util.List;


import fingerprint.gameplay.map.GameArea;
import fingerprint.gameplay.map.GameAreaType;
import fingerprint.gameplay.map.blocks.Block;
import fingerprint.gameplay.map.blocks.BlockManager;
import fingerprint.gameplay.map.village.Village;

public class AreaGenerator {

    private final int OUTSKIRTS_SIZE = 512;
    private int idCounter = 0;
    private AreaShapeGenerator shapeGenerator = new AreaShapeGenerator();
    private AreaStructureGenerator structureGenerator = new AreaStructureGenerator();
    
    public AreaGenerator() {
        
    }
    public List<GameArea> generateAreas(){
        List<GameArea> areas = new ArrayList<>();
        areas.add(generateOutskirts()); idCounter++;
        return areas;
    }
    public Village generateVillage(){
        Village village = null;
        
        return village;
    }
    private GameArea generateOutskirts(){
        int[][] areaMask = shapeGenerator.maskGeneration(OUTSKIRTS_SIZE,OUTSKIRTS_SIZE,
                AreaShapeGenerator.GENERIC_AREA_SMOOTHNESS,AreaShapeGenerator.GENERIC_MASK_CUTOFF);
        int[][]tilemap = areaMask.clone();
        float[][]noisemap = shapeGenerator.getNoise(OUTSKIRTS_SIZE, OUTSKIRTS_SIZE, AreaShapeGenerator.GENERIC_AREA_SMOOTHNESS);
        structureGenerator.generatePassagesToNewArea(tilemap, OUTSKIRTS_SIZE,OUTSKIRTS_SIZE, 4);
        
        /**
         * Generate the walls
         */
        for (int x = 1; x < OUTSKIRTS_SIZE-1; x++) {
            for (int y = 1; y < OUTSKIRTS_SIZE-1; y++) {
                if(checkIfShouldBeWall(tilemap, x, y, OUTSKIRTS_SIZE, OUTSKIRTS_SIZE)){
                    tilemap[x][y] = 70;
                }else{
                    if(tilemap[x][y] == BlockManager.MaskBlockUnwalkable){
                        tilemap[x][y] = BlockManager.VoidBlock;
                    }
                }
            }
         }
        
        /**
         * Fill the rest with void 
         */
        for (int x = 0; x < OUTSKIRTS_SIZE; x++) {
            for (int y = 0; y < OUTSKIRTS_SIZE; y++) {
                if(tilemap[x][y] == BlockManager.MaskBlockUnwalkable){
                    tilemap[x][y] = BlockManager.VoidBlock;
                }
            }
        }
        
        /**
         * Generate the land
         */
        
        for (int x = 0; x < OUTSKIRTS_SIZE; x++) {
            for (int y = 0; y < OUTSKIRTS_SIZE; y++) {
                float noisevalue = noisemap[x][y]*10;
                int value;
                if(noisevalue <4){
                    value = 1;
                }else if(noisevalue < 7){
                    value = 2;
                }else{
                    value = 3;
                }
                int blockID = BlockManager.ErrorBlock;
                switch(value){
                case 1:
                    blockID = 60;
                    break;
                case 2:
                    blockID = 61;
                    break;
                    
                case 3:
                    blockID = 62;
                    break;
                }
                if(tilemap[x][y] == BlockManager.MaskBlockWalkable){
                    tilemap[x][y] = blockID;
                }
            }
        }
        List<int[][]>tileLayers = new ArrayList<>();
        tileLayers.add(tilemap);
        GameArea result = new GameArea(tileLayers, GameAreaType.OUTSKIRTS, idCounter);
        return result;
    }
    private boolean checkIfShouldBeWall(int[][] tilemap,int x , int y,int width,int height){
        if(tilemap[x][y] != BlockManager.MaskBlockUnwalkable) return false;
        if(tilemap[x+1][y] == BlockManager.MaskBlockWalkable) return true;
        if(tilemap[x+1][y+1] == BlockManager.MaskBlockWalkable) return true;
        if(tilemap[x][y+1] == BlockManager.MaskBlockWalkable) return true;
        if(tilemap[x+1][y-1] == BlockManager.MaskBlockWalkable) return true;
        if(tilemap[x][y-1] == BlockManager.MaskBlockWalkable) return true;
        if(tilemap[x-1][y-1] == BlockManager.MaskBlockWalkable) return true;
        if(tilemap[x-1][y] == BlockManager.MaskBlockWalkable) return true;
        if(tilemap[x-1][y-1] == BlockManager.MaskBlockWalkable) return true;
        return false;
    }
    private int calculateOldCoordinate(int currentSize, int oldSize){
        int result = (currentSize - oldSize)/2;
        return result;
    }
}
