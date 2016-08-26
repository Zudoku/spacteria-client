package fingerprint.rendering.map;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMapPlus;


public class TilemapRenderer {
    private static final Logger logger = Logger.getLogger(TilemapRenderer.class.getName());
    
    public static final int tilesDrawnHorizontal = 26 +1;
    public static final int tilesDrawnVertical = 16 +1;
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
        
        if(mapToRender == null){
            if(filename.equals("")){
                return;
            }else {
                try {
                    mapToRender = new TiledMapPlus("resources/" + filename);
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
        
        
        
        //TODO: Tile Offset
        int tileOffsetX = 0;
        int tileOffsetY = 0;
        
        //Check if over the map
        if(startingX < 0 ){
            if(startingX + tilesDrawnHorizontal < 0 || startingX + tilesDrawnHorizontal >= mapToRender.getWidth() ){
                return;
            }
            //Set offset
            startingX = 0;
            offsetX = (int)(Math.floor((-screenX)));
            
        }//Check if over the map
        if(startingY < 0 ){
            if(startingY + tilesDrawnVertical < 0 || startingY + tilesDrawnVertical >= mapToRender.getHeight()){
                return;
            }
            //Set offset
            startingY = 0;
            offsetY = (int)(Math.floor((-screenY)));
        }
        
        mapToRender.render(offsetX, offsetY, tileOffsetX, tileOffsetY, tilesDrawnHorizontal, tilesDrawnVertical);
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
