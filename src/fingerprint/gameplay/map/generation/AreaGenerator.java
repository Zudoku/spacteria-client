package fingerprint.gameplay.map.generation;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMapPlus;

import fingerprint.gameplay.map.FunctionalMap;
import fingerprint.gameplay.map.blocks.BlockManager;
import fingerprint.gameplay.map.village.Village;
import fingerprint.inout.TileFileHandler;
import fingerprint.rendering.map.TilemapRenderer;

public class AreaGenerator {

    private final int WORLD_SIZE = FunctionalMap.SIZE;
    
    private int idCounter = 0;
    private AreaShapeGenerator shapeGenerator = new AreaShapeGenerator();
    private AreaStructureGenerator structureGenerator = new AreaStructureGenerator();
    private TileFileHandler tileFileHandler;
    public AreaGenerator() { 
        /*
        int[][] areaMask = shapeGenerator.maskGeneration(VILLAGE_SIZE,VILLAGE_SIZE,
                AreaShapeGenerator.GENERIC_AREA_SMOOTHNESS,AreaShapeGenerator.GENERIC_MASK_CUTOFF);
        int[][]tilemap = areaMask.clone();
        float[][]noisemap = shapeGenerator.getNoise(VILLAGE_SIZE, VILLAGE_SIZE, AreaShapeGenerator.GENERIC_AREA_SMOOTHNESS); 
        */
    }
    public FunctionalMap generateAreas(String worldName){
        FunctionalMap map = new FunctionalMap(new byte[FunctionalMap.SIZE*FunctionalMap.SIZE]);
        
        for (int i = 0; i < FunctionalMap.SIZE; i++) {
            for (int u = 0; u < FunctionalMap.SIZE; u++) {
                map.getData()[i][u] = (byte) ((byte) i%10);
            }
        }
        
        tileFileHandler = new TileFileHandler();
        tileFileHandler.init(worldName);
        
        short slice[][] = new short[FunctionalMap.SIZE][1];
        for (int i = 0; i < FunctionalMap.SIZE; i++) {
            slice[i][0] = (short) (10);
        }
        for (int i = 0; i < FunctionalMap.SIZE; i++) {
            tileFileHandler.writeMap(slice,0,i,FunctionalMap.SIZE,1);
            
        }
        
        return map;
    }
   

    private int[][] overLapTiles(int[][] original,int[][] overWriting,int overwriteWidth,int overwriteHeight, int offsetWidth, int offsetHeight){
        int[][] result = original.clone();
        for(int y = 0; y < overwriteHeight; y++){
            for(int x = 0; x < overwriteWidth; x++){
                result[x + offsetWidth][y + offsetHeight] = overWriting[x][y];
            }
        }
        return result;
    }
    public Village generateVillage(){
        
        
        
        Village village = null;
        
        return village;
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
