package fingerprint.rendering;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import fingerprint.core.GameLauncher;
import fingerprint.rendering.map.TilemapRenderer;

public class RenderingManager {
    private static final Logger logger = Logger.getLogger(RenderingManager.class.getName());
    private TilemapRenderer tileMapRenderer;
    
    public static RenderingResolutions currentResolution;
    public static int unScaledScreenWidth = 26*64;  //1664
    public static int unScaledScreenHeight = 16*64; //1024
    
    private double screenStartX = 0;
    private double screenStartY = 0;
    
    public RenderingManager() {
        currentResolution = GameLauncher.gameSettings.resolution;
        tileMapRenderer = new TilemapRenderer();
        //BAD HABIT :S
        GameLauncher.injector.injectMembers(tileMapRenderer);
    }
    public void draw(Graphics graphics,List<int[][]>tileLayers){
        graphics.setBackground(Color.yellow);
        graphics.scale((float) ((double)currentResolution.getWidth()/(double)unScaledScreenWidth),(float)((double)currentResolution.getHeight()/(double)unScaledScreenHeight));
        //LIGHTING
        //MAP
        tileMapRenderer.draw(screenStartX, screenStartY,tileLayers);
        //OBJECTS
        //PLAYER
        //EFFECTS
        //UI
    }
}
