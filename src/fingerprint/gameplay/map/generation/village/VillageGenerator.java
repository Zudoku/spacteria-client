package fingerprint.gameplay.map.generation.village;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import fingerprint.gameplay.map.FunctionalMap;
import fingerprint.gameplay.map.blocks.BlockRendering;
import fingerprint.gameplay.map.generation.StructureContainer;
import fingerprint.inout.TileFileHandler;

public class VillageGenerator {
    private static final Logger logger = Logger.getLogger(VillageGenerator.class.getName());
    
    private final int WORLD_SIZE = FunctionalMap.SIZE;
    private VillageStructureGenerator villageStructs = new VillageStructureGenerator();
    private Random random;
    
    public static int VILLAGEWIDTH = 20;
    public static int VILLAGEHEIGHT = 20;
    
    public VillageGenerator() {
        random = new Random();
    }
    
    public FunctionalMap generateVillage(FunctionalMap map,TileFileHandler tileFileHandler){
        logger.log(Level.INFO,"Calculating village position");
        int startingPoint[] = calculateVillageStartingPoint();
        logger.log(Level.INFO,"Village spawnpoint: ({0},{1})",new Object[]{startingPoint[0],startingPoint[1]});
        
        
        byte functionalMapBuffer[][] = new byte[VILLAGEWIDTH][VILLAGEHEIGHT];
        short renderingMapBuffer[][] = new short[VILLAGEWIDTH][VILLAGEHEIGHT];
        
        
        
        placeBorders(functionalMapBuffer, renderingMapBuffer);
        placeHouses(functionalMapBuffer, renderingMapBuffer);
        
        
        for (int y = 0; y < VILLAGEHEIGHT; y++) {
            for (int x = 0; x < VILLAGEWIDTH; x++) {
                int xIndex = x + startingPoint[0];
                int yIndex = y + startingPoint[1];
                if(functionalMapBuffer[x][y] != 0){
                    map.getData()[xIndex][yIndex] = functionalMapBuffer[x][y];
                }
                
            }
        }
        
        tileFileHandler.writeMap(renderingMapBuffer, startingPoint[0], startingPoint[1], VILLAGEWIDTH, VILLAGEHEIGHT);
        
        return map;
    }
    
    private void placeBorders(byte[][] functionalMapBuffer,short[][] renderingMapBuffer){
        //THIS CODE IS REALLY SHITTY
        //SORRY IN ADVANCE
        byte villageFunctionalBlock = 30;
        
        for (int i = 0; i < VILLAGEWIDTH-8; i++) {
            functionalMapBuffer[4 +i][0]= villageFunctionalBlock;
            functionalMapBuffer[4 +i][VILLAGEHEIGHT-1]= villageFunctionalBlock;
            
            renderingMapBuffer[4 +i][0] = BlockRendering.VILLAGE_FENCE;
            renderingMapBuffer[4 +i][VILLAGEHEIGHT-1] = BlockRendering.VILLAGE_FENCE;
            
        }
        for (int i = 0; i < VILLAGEHEIGHT-8; i++) {
            functionalMapBuffer[VILLAGEWIDTH-1][4+i]= villageFunctionalBlock;
            functionalMapBuffer[0][4+i]= villageFunctionalBlock;
            
            renderingMapBuffer[VILLAGEWIDTH-1][4+i] = BlockRendering.VILLAGE_FENCE;
            renderingMapBuffer[0][4+i] = BlockRendering.VILLAGE_FENCE;
        }
        //TOP LEFT
        renderingMapBuffer[1][2] = BlockRendering.VILLAGE_FENCE;
        renderingMapBuffer[1][3] = BlockRendering.VILLAGE_FENCE;
        
        renderingMapBuffer[2][1] = BlockRendering.VILLAGE_FENCE;
        renderingMapBuffer[3][1] = BlockRendering.VILLAGE_FENCE;
        
        functionalMapBuffer[1][2] = villageFunctionalBlock;
        functionalMapBuffer[1][3] = villageFunctionalBlock;
        
        functionalMapBuffer[2][1] = villageFunctionalBlock;
        functionalMapBuffer[3][1] = villageFunctionalBlock;
        
        
        //BOTTOM LEFT
        renderingMapBuffer[1][VILLAGEHEIGHT-2-1] = BlockRendering.VILLAGE_FENCE;
        renderingMapBuffer[1][VILLAGEHEIGHT-3-1] = BlockRendering.VILLAGE_FENCE;

        renderingMapBuffer[2][VILLAGEHEIGHT-1-1] = BlockRendering.VILLAGE_FENCE;
        renderingMapBuffer[3][VILLAGEHEIGHT-1-1] = BlockRendering.VILLAGE_FENCE;
        
        functionalMapBuffer[1][VILLAGEHEIGHT-2-1] = villageFunctionalBlock;
        functionalMapBuffer[1][VILLAGEHEIGHT-3-1] = villageFunctionalBlock;

        functionalMapBuffer[2][VILLAGEHEIGHT-1-1] = villageFunctionalBlock;
        functionalMapBuffer[3][VILLAGEHEIGHT-1-1] = villageFunctionalBlock;
        
        //TOP RIGHT
        renderingMapBuffer[VILLAGEWIDTH-2-1][1] = BlockRendering.VILLAGE_FENCE;
        renderingMapBuffer[VILLAGEWIDTH-3-1][1] = BlockRendering.VILLAGE_FENCE;
        
        renderingMapBuffer[VILLAGEWIDTH-1-1][2] = BlockRendering.VILLAGE_FENCE;
        renderingMapBuffer[VILLAGEWIDTH-1-1][3] = BlockRendering.VILLAGE_FENCE;
        
        functionalMapBuffer[VILLAGEWIDTH-2-1][1] = villageFunctionalBlock;
        functionalMapBuffer[VILLAGEWIDTH-3-1][1] = villageFunctionalBlock;
        
        functionalMapBuffer[VILLAGEWIDTH-1-1][2] = villageFunctionalBlock;
        functionalMapBuffer[VILLAGEWIDTH-1-1][3] = villageFunctionalBlock;
        
        //BOTTOM RIGHT
        renderingMapBuffer[VILLAGEWIDTH-1-1][VILLAGEHEIGHT-1-2] = BlockRendering.VILLAGE_FENCE;
        renderingMapBuffer[VILLAGEWIDTH-1-1][VILLAGEHEIGHT-1-3] = BlockRendering.VILLAGE_FENCE;
        
        renderingMapBuffer[VILLAGEWIDTH-1-2][VILLAGEHEIGHT-1-1] = BlockRendering.VILLAGE_FENCE;
        renderingMapBuffer[VILLAGEWIDTH-1-3][VILLAGEHEIGHT-1-1] = BlockRendering.VILLAGE_FENCE;
        
        functionalMapBuffer[VILLAGEWIDTH-1-1][VILLAGEHEIGHT-1-2] = villageFunctionalBlock;
        functionalMapBuffer[VILLAGEWIDTH-1-1][VILLAGEHEIGHT-1-3] = villageFunctionalBlock;
        
        functionalMapBuffer[VILLAGEWIDTH-1-2][VILLAGEHEIGHT-1-1] = villageFunctionalBlock;
        functionalMapBuffer[VILLAGEWIDTH-1-3][VILLAGEHEIGHT-1-1] = villageFunctionalBlock;
        
        
    }
    
    
    private void placeHouses(byte[][] functionalMapBuffer,short[][] renderingMapBuffer){
        int houseAmount = 3 +random.nextInt(3);
        logger.log(Level.INFO,"Placing {0} houses",houseAmount);
        //Place a house
        for (int i = 0; i < houseAmount; i++) {
            int houseWidth = 4 + random.nextInt(4);
            int houseHeight = 4 + random.nextInt(2);
            StructureContainer house = villageStructs.getWoodenHouseHorizontal(houseWidth, houseHeight);
            
            short tries = 0;
            boolean trying = true;
            while(trying){
                if(tryToFitStructureIntoBuffer(house, functionalMapBuffer, renderingMapBuffer, VILLAGEWIDTH, VILLAGEHEIGHT)){
                    
                    trying = false;
                    continue;
                }
                if(tries > 100){
                    logger.log(Level.INFO,"Can't place house {0} because there is not enough space.");
                    trying = false;
                }
                tries++;
            }
            logger.log(Level.INFO,"Placing house {0} took {1} tries",new Object[]{i,tries});
        }
        
    }
    private boolean tryToFitStructureIntoBuffer(StructureContainer container, byte[][] functionalMapBuffer,short[][] renderingMapBuffer,int bufferwidth, int bufferheight){
        int widthLimit = bufferwidth-container.getWidth() -1 ;
        int heightLimit = bufferheight-container.getHeight() -1;
        
        if(widthLimit < 0){
            return false;
        }
        if(heightLimit < 0){
            return false;
        }
        
        int x = random.nextInt(widthLimit);
        int y = random.nextInt(heightLimit);
        //Test if it doesnt fit 
        for (int i = 0; i < container.getHeight(); i++) {
            for (int u = 0; u < container.getWidth(); u++) {
                // (x,y) == (u,i)
                if(container.getFunctional()[u][i] != 0 && functionalMapBuffer[x + u][y + i ]!=0){
                    return false;
                }
            }
        }
        //Set the data
        for (int i = 0; i < container.getHeight(); i++) {
            for (int u = 0; u < container.getWidth(); u++) {
                // (x,y) == (u,i)
                if(container.getFunctional()[u][i] != 0){
                    functionalMapBuffer[x+u][y+i] = container.getFunctional()[u][i];
                    renderingMapBuffer[x+u][y+i] = container.getRendering()[u][i];
                }
            }
        }
        
        return true;
    }
    
    private int[] calculateVillageStartingPoint(){
        int result[] = new int[2];
        int startingX = WORLD_SIZE/2 ;
        int startingY = WORLD_SIZE/2;
        
        int SPAWNAREA = 100;
        
        startingX -= SPAWNAREA/2;
        startingY -= SPAWNAREA/2;
        
        Random r = new Random();
        
        result[0] = startingX + r.nextInt(SPAWNAREA);
        result[1] = startingY + r.nextInt(SPAWNAREA);
        return result;
        
    }
    
}
