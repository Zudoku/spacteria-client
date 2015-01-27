package fingerprint.rendering.map;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.google.inject.Inject;

import fingerprint.gameplay.map.blocks.Block;
import fingerprint.gameplay.map.blocks.BlockManager;

public class TilemapRenderer {
    private static final Logger logger = Logger.getLogger(TilemapRenderer.class.getName());
    
    @Inject private BlockManager blockManager;
    
    private double screenStartX;
    private double screenStartY;
    
    private int tilesDrawnHorizontal = 26 +1;
    private int tilesDrawnVertical = 16 +1;
    private int tileSize = 64;
    
    public TilemapRenderer() {
        
    }
    public void draw(double screenX, double screenY,List<int[][]> tileLayers){
        this.screenStartX = screenX;
        this.screenStartY = screenY;
        
        int layer = 0;
        for(int[][] tileLayer : tileLayers){
            Image[][] tiles = getDrawableTilesFromLayer(tileLayer);
            double startMapRenderingX = getTilesDrawingOffsetX();
            double startMapRenderingY = getTilesDrawingOffsetY();
            for(int horizontal = 0; horizontal < tilesDrawnHorizontal; horizontal++){
                for(int vertical = 0;vertical < tilesDrawnVertical; vertical ++){
                    Image tile = tiles[horizontal][vertical];
                    if(tile == null){
                        logger.log(Level.INFO,"Image null, cant draw on layer {0}",layer);
                        continue;
                    }
                    tile.draw((float)(startMapRenderingX + horizontal * tileSize), (float)(startMapRenderingY + vertical * tileSize));
                }
            }
            layer++;
        }
    }
    private Image[][]getDrawableTilesFromLayer(int[][] tileLayer){
        Image[][] tilesToDraw = new Image[tilesDrawnHorizontal][tilesDrawnVertical];
        
        int startTileX = (int)Math.floor(screenStartX/tileSize);
        int startTileY = (int)Math.floor(screenStartY/tileSize);
        for(int x = startTileX; x < (startTileX+tilesDrawnHorizontal) ; x++){
            for(int y = startTileY; y < (startTileY+tilesDrawnVertical) ; y++){
                Block currentBlock = blockManager.getBlock(tileLayer[x][y]);
                
                try {
                    Image tileImage;
                    if(currentBlock == null){
                        tileImage = new Image(blockManager.getBlock(BlockManager.ErrorBlock).getSprite());
                    }else{
                        tileImage = new Image(currentBlock.getSprite());
                    }
                    
                    tilesToDraw[x-startTileX][y-startTileY] = tileImage;
                } catch (SlickException e) {
                    logger.log(Level.WARNING,"ERROR:{0}",e.getMessage());
                }
                
            }
        }
        return tilesToDraw;
    }
    private double getTilesDrawingOffsetX(){
        return -(screenStartX % tileSize);
    }
    private double getTilesDrawingOffsetY(){
        return -(screenStartY % tileSize);
    }
}
