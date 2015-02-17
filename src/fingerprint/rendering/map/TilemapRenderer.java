package fingerprint.rendering.map;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMapPlus;

import com.google.inject.Inject;

import fingerprint.gameplay.map.blocks.Block;
import fingerprint.gameplay.map.blocks.BlockManager;

public class TilemapRenderer {
    private static final Logger logger = Logger.getLogger(TilemapRenderer.class.getName());
    
    private BlockManager blockManager;
    
    private double screenStartX;
    private double screenStartY;
    
    public static int tilesDrawnHorizontal = 26 +1;
    public static int tilesDrawnVertical = 16 +1;
    public static int tileSize = 64;
    
    public TilemapRenderer(BlockManager blockManager) {
        this.blockManager = blockManager;
    }
    public void draw(double screenX, double screenY,TiledMapPlus tilemap){
        this.screenStartX = screenX;
        this.screenStartY = screenY;
        int startMapRenderingX = -getTilesDrawingOffsetX();
        int startMapRenderingY = -getTilesDrawingOffsetY();
        int startTileX = (int)(screenStartX/tileSize);
        int startTileY = (int)(screenStartY/tileSize);
        if(startMapRenderingY == 0){
            //startMapRenderingY = 1; // I have no idea what is causing this, but when startMapRenderingY == 0, 
            //white grid comes visible ?? probably a rounding error somewhere
        }
        if(startMapRenderingX == 0){
            //startMapRenderingX = 1;
        }
        tilemap.render(startMapRenderingX,startMapRenderingY,startTileX, startTileY, tilesDrawnHorizontal , tilesDrawnVertical);
        logger.log(Level.INFO, "{0} {1} {2} ", new Object[]{startMapRenderingX,startTileX, screenStartX});
    }

    private int getTilesDrawingOffsetX(){
        return (int)((double)((int)screenStartX % tileSize));
    }
    private int getTilesDrawingOffsetY(){
        return (int)((double)((int)screenStartY % tileSize));
    }
}
