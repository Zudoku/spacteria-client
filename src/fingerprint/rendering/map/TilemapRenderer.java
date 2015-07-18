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

import fingerprint.gameplay.map.FunctionalMap;
import fingerprint.gameplay.map.blocks.Block;
import fingerprint.gameplay.map.blocks.BlockManager;
import fingerprint.inout.GameFileHandler;
import fingerprint.inout.TileFileHandler;

public class TilemapRenderer {
    private static final Logger logger = Logger.getLogger(TilemapRenderer.class.getName());
    
    private BlockManager blockManager;
    
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
        
        int tileOffsetX = 0;
        int tileOffsetY = 0;
        if(startingX < 0 || startingX >= FunctionalMap.SIZE || (startingX + tilesDrawnHorizontal) >= FunctionalMap.SIZE){
            if(startingX + tilesDrawnHorizontal < 0 || startingX + tilesDrawnHorizontal >= FunctionalMap.SIZE ){
                return;
            }
            
            startingX = 0;
            offsetX = (int)(Math.floor((-screenX)));
            
        }
        if(startingY < 0 || startingY >= FunctionalMap.SIZE || (startingY + tilesDrawnVertical) >= FunctionalMap.SIZE){
            if(startingY + tilesDrawnVertical < 0 || startingY + tilesDrawnVertical >= FunctionalMap.SIZE ){
                return;
            }
            
            startingY = 0;
            offsetY = (int)(Math.floor((-screenY)));
        }
        
        short[][] mapData = tilehandler.getPartOfMap(startingX, startingY, tilesDrawnHorizontal - tileOffsetX, tilesDrawnVertical - tileOffsetY);
        HashMap<Short, Image> updatedTileBuffer = new HashMap<>();
        for (int y = 0; y < tilesDrawnVertical; y++) {
            for (int x = 0; x < tilesDrawnHorizontal; x++) {
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
