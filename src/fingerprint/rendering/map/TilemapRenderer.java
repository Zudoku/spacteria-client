package fingerprint.rendering.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMapPlus;

import com.google.inject.Inject;

import fingerprint.gameplay.map.blocks.Block;
import fingerprint.gameplay.map.blocks.BlockManager;
import fingerprint.inout.GameFileHandler;
import fingerprint.inout.TileFileHandler;

public class TilemapRenderer {
    private static final Logger logger = Logger.getLogger(TilemapRenderer.class.getName());
    
    private BlockManager blockManager;
    
    private int pixelsDrawnX;
    private int pixelsDrawnY;
    
    private int pixelsDrawnBufferX;
    private int pixelsDrawnBufferY;
    
    public static int tilesDrawnHorizontal = 26 +1;
    public static int tilesDrawnVertical = 16 +1;
    public static int tileSize = 64;
    
    
    public static String spriteSheetPath = "resources/tilemap.png";
    public static String spriteSheetLocation = "resources";
    
    private SpriteSheet spriteSheet;
    
    private HashMap<Short,Image> tilebuffer = new HashMap<>();
    private TileFileHandler tilehandler = new TileFileHandler();
    
    public TilemapRenderer(BlockManager blockManager) {
        this.blockManager = blockManager;
        
        
    }
    public void setWorld(String world){
        tilehandler.init(world);
        try {
            spriteSheet = new SpriteSheet(spriteSheetPath, tileSize, tileSize);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(double screenX, double screenY){
        
        
        
        int offsetX = -getTilesDrawingOffsetX(screenX);
        int offsetY = -getTilesDrawingOffsetY(screenY);
        
        
        int startingX = getTileStartX(screenX);
        int startingY = getTileStartY(screenY);
        
        short[][] mapData = tilehandler.getPartOfMap(startingX, startingY, tilesDrawnHorizontal, tilesDrawnVertical);
        HashMap<Short, Image> updatedTileBuffer = new HashMap<>();
        for (int y = 0; y < tilesDrawnVertical; y++) {
            for (int x = 0; x < tilesDrawnHorizontal; x++) {
                if(y == 17){
                    System.out.println("a");
                }
                short currentDrawableTile = mapData[x][y];
                Image currentDrawableImage = null;
                if(tilebuffer.containsKey(currentDrawableTile)){
                    currentDrawableImage = tilebuffer.get(currentDrawableTile);
                }else{
                    int spriteX = currentDrawableTile % 20;
                    int spriteY = (int) Math.floor(currentDrawableTile / 20);
                    currentDrawableImage = spriteSheet.getSprite(spriteX, spriteY);
                }
                if(!updatedTileBuffer.containsKey(currentDrawableTile)){
                    updatedTileBuffer.put(currentDrawableTile, currentDrawableImage);
                }
                double drawingCordinateX = offsetX + x * tileSize;
                double drawingCordinateY = offsetY + y * tileSize;
                currentDrawableImage.draw((float)drawingCordinateX,(float)drawingCordinateY);
                
            }
        }
        tilebuffer = updatedTileBuffer;
        
        
        
        /*
        pixelsDrawnX = 0;
        pixelsDrawnY = 0;
        
        int drawingGoalX =(int) screenX + tilesDrawnHorizontal * tileSize;
        int drawingGoalY =(int) screenY + tilesDrawnVertical * tileSize;
        
        boolean horizontalInProgress = true;
        boolean verticalInProgress = true;
        
        
        int timesDrawn = 0;
        while(horizontalInProgress){
            if(pixelsDrawnX > drawingGoalX){
                horizontalInProgress = false;
                continue;
            }
            
            String chunkFilename = getChunkLocationID(pixelsDrawnX, pixelsDrawnY);
            String tilemapPath = GameFileHandler.getChunkFilePath(world, chunkFilename);
            TiledMapPlus tilemap = null;
            try {
                tilemap = new TiledMapPlus(tilemapPath,spriteSheetLocation);
            } catch (SlickException e) {
                e.printStackTrace();
            }
            if(tilemap == null){
                continue;
            }
            
            double chunkStartsX = getChunk(pixelsDrawnX) * WorldChunk.CHUNK_SIZE;
            double chunkStartsY = getChunk(pixelsDrawnY) * WorldChunk.CHUNK_SIZE;
            
            drawSectionOfMap((int)screenX, (int)screenY, pixelsDrawnX, pixelsDrawnY, tilesDrawnVertical, tilemap);
            pixelsDrawnX += pixelsDrawnBufferX;
            
            timesDrawn ++;
        }
        */
    }
    
    private void drawSectionOfMap(int screenX,int screenY,int pixelsDrawnX,int pixelsDrawnY,int tilesToDrawVertical, TiledMapPlus tilemapToRender){
        /*
        int tilesOffSetX = getTilesDrawingOffsetX(screenX);
        int tilesOffSetY = getTilesDrawingOffsetX(screenX);
        
        
        int worldCoordinateDrawingStartX = (int)(screenX+pixelsDrawnX);
        int worldCoordinateDrawingStartY = (int)(screenY+pixelsDrawnY);
        
        int chunkCoordinatesDrawingStartX = worldCoordinateDrawingStartX - getChunk(worldCoordinateDrawingStartX) * WorldChunk.CHUNK_SIZE;
        int chunkCoordinatesDrawingStartY = worldCoordinateDrawingStartX - getChunk(worldCoordinateDrawingStartY) * WorldChunk.CHUNK_SIZE;
        
        int startTileX = (chunkCoordinatesDrawingStartX - tilesOffSetX) / tileSize;
        int startTileY = (chunkCoordinatesDrawingStartY - tilesOffSetY) / tileSize;
        
        int tilesToDrawHorizontal = WorldChunk.CHUNK_SIZE - startTileX;
        
        if(tilesToDrawHorizontal > tilesDrawnHorizontal){
            tilesToDrawHorizontal = tilesDrawnHorizontal;
        }
        //Optimization
        if(tilesToDrawHorizontal == tilesDrawnHorizontal){
            double pixelsToGo = (tilesDrawnHorizontal * tileSize) - pixelsDrawnX;
            if(pixelsToGo < tilesDrawnHorizontal*tileSize){
                tilesToDrawHorizontal = (int) Math.ceil(pixelsToGo/tileSize);
            }
        }
        pixelsDrawnBufferX = (tilesDrawnHorizontal * tileSize);
        
        int displayCoordinatesX = pixelsDrawnX - tilesOffSetX;
        int displayCoordinatesY = pixelsDrawnY - tilesOffSetY;
        logger.log(Level.SEVERE,"tilesDrawn {0} {1}",new Object[]{tilesToDrawHorizontal,tilesToDrawVertical});
        tilemapToRender.render(displayCoordinatesX,displayCoordinatesY,startTileX,startTileY,tilesToDrawHorizontal,tilesToDrawVertical);
        
        
        int screenMapPosX = ((int)((double)(mapStartDrawingX - screenX)))-getTilesDrawingOffsetX(screenX); //WORLD COORDINATE
        int screenMapPosY = ((int)((double)(mapStartDrawingY - screenY)))-getTilesDrawingOffsetY(screenY);
        
        int startTileX = (int)((mapStartDrawingX-mapStartX) /tileSize);//the start-tile to draw on the map in the tilemap
        int startTileY = (int)((mapStartDrawingY-mapStartY) /tileSize);
        
        int tilesDrawnx = WorldChunk.CHUNK_SIZE - startTileX;
        int tilesDrawny = WorldChunk.CHUNK_SIZE - startTileY;
        
        if(tilesDrawnx > tilesDrawnHorizontal){
            tilesDrawnx = tilesDrawnHorizontal;
        }
        if(tilesDrawny > tilesDrawnVertical){
            tilesDrawny = tilesDrawnVertical;
        }
        //Optimization
        if(tilesDrawnx == tilesDrawnHorizontal){
            double pixelsToGo = tilesDrawnHorizontal * tileSize - mapStartDrawingX;
            if(pixelsToGo < tilesDrawnHorizontal*tileSize){
                tilesDrawnx = (int) Math.ceil(pixelsToGo/tileSize);
            }
        }
      //Optimization
        if(tilesDrawny== tilesDrawnVertical){
            double pixelsToGo = tilesDrawnVertical * tileSize - mapStartDrawingY;
            if(pixelsToGo < tilesDrawnVertical*tileSize){
                tilesDrawny = (int) Math.ceil(pixelsToGo/tileSize);
            }
        }

        mapStartDrawingTrackerBufferX = (tilesDrawnx * tileSize);

        mapStartDrawingTrackerBufferY = (tilesDrawny * tileSize);
        
        
        tilemapToRender.render(screenMapPosX,screenMapPosY,startTileX,startTileY,tilesDrawnx,tilesDrawny);
        */
    }

    private int getTilesDrawingOffsetX(double drawX){
        return (int)((double)((int)drawX % tileSize));
    }
    private int getTilesDrawingOffsetY(double drawY){
        return (int)((double)((int)drawY % tileSize));
    }
    private int getTileStartX(double drawX){
        return (int)(Math.floor((drawX/tileSize)));
    }
    private int getTileStartY(double drawY){
        return (int)(Math.floor((drawY/tileSize)));
    }
}
