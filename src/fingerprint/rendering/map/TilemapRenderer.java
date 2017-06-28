package fingerprint.rendering.map;

import java.util.logging.Level;
import java.util.logging.Logger;

import fingerprint.inout.FileUtil;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMapPlus;


public class TilemapRenderer {
    private static final Logger logger = Logger.getLogger(TilemapRenderer.class.getName());
    
    public static final int tilesDrawn = 16 + 7;
    public static final int tileSize = 64;
    
    public static String spriteSheetPath = "resources/tilemap.png";
    public static String spriteSheetLocation = "resources";
    
    
    private TiledMapPlus mapToRender;
    private String filename = "";
    
    public TilemapRenderer() {
        
    }
    public void setMap(String world){
        filename = world;
        mapToRender = null;
    }
    
    public void draw(double screenX, double screenY){
        
        screenX -= (3 * tileSize);
        screenY -= (3 * tileSize);
        
        if(mapToRender == null){
            if(filename.equals("")){
                return;
            }else {
                try {
                    mapToRender = new TiledMapPlus(FileUtil.TILEDMAPS_PATH + "/" + filename + FileUtil.TILEDMAP_FILE_EXTENSION);
                } catch (SlickException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        
        //Initial floor offset 
        int offsetX = -getTilesDrawingOffsetX(screenX);
        int offsetY = -getTilesDrawingOffsetY(screenY);
        
        //Calculate first tile from the map (top left corner)
        int startingX = getTileStartX(screenX);
        int startingY = getTileStartY(screenY);
        
        int drawedTileNumVertical = tilesDrawn;
        int drawedTileNumHorizontal = tilesDrawn;
        
        
        //Check if over the map from left side
        if(startingX < 0 ){
            if(startingX + tilesDrawn < 0){
                return;
            }
            //drawedTileNumHorizontal = drawedTileNumHorizontal + startingX;
            //Set offset
            startingX = 0;
            offsetX = (int)(Math.floor((-screenX)));
            
            
        }
        //Check if over the map from right side
        if(startingX + drawedTileNumHorizontal >  mapToRender.getWidth()){
            if(startingX > mapToRender.getWidth()){
                return;
            }
            drawedTileNumHorizontal = mapToRender.getWidth() - startingX;
            //Set offset
            //startingX = 0;
            //offsetX = (int)(Math.floor((-screenX)));
            
            
        }
        //Check if over the map from top side
        if(startingY < 0 ){
            if(startingY + tilesDrawn < 0){
                return;
            }
            //drawedTileNumHorizontal = drawedTileNumHorizontal + startingX;
            //Set offset
            startingY = 0;
            offsetY = (int)(Math.floor((-screenY)));
            
            
        }
        //Check if over the map from bottom side
        if(startingY + drawedTileNumVertical >  mapToRender.getHeight()){
            if(startingY > mapToRender.getHeight()){
                return;
            }
            drawedTileNumVertical = mapToRender.getHeight() - startingY;
            //Set offset
            //startingX = 0;
            //offsetX = (int)(Math.floor((-screenX)));
            
            
        }



        offsetX -= (3 * tileSize);
        offsetY -= (3 * tileSize);
        
        mapToRender.render(offsetX, offsetY, startingX, startingY, drawedTileNumHorizontal, drawedTileNumVertical);
    }
    

    public static int getTilesDrawingOffsetX(double drawX){
        return (int)((double)((int)drawX % tileSize));
    }
    public static int getTilesDrawingOffsetY(double drawY){
        return (int)((double)((int)drawY % tileSize));
    }
    public static int getTileStartX(double drawX){
        return (int)(Math.floor((drawX/tileSize)));
    }
    public static int getTileStartY(double drawY){
        return (int)(Math.floor((drawY/tileSize)));
    }
}
