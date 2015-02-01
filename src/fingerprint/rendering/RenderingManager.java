package fingerprint.rendering;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fingerprint.core.GameLauncher;
import fingerprint.gameplay.map.blocks.BlockManager;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.gameplay.objects.GameObject;
import fingerprint.mainmenus.GameWorldInfoContainer;
import fingerprint.rendering.map.TilemapRenderer;
import fingerprint.states.MainMenuState;

@Singleton
public class RenderingManager {
    private static final Logger logger = Logger.getLogger(RenderingManager.class.getName());
    private TilemapRenderer tileMapRenderer;
    
    @Inject private EntityManager entityManager;
    
    public static RenderingResolutions currentResolution;
    public static int unScaledScreenWidth = 26*TilemapRenderer.tileSize;  //64 = 1664
    public static int unScaledScreenHeight = 16*TilemapRenderer.tileSize; //64 = 1024
    
    private double screenStartX = 0;
    private double screenStartY = 0;
    
    
    public RenderingManager() {
        currentResolution = GameLauncher.gameSettings.resolution;
        
    }
    public void configure(EntityManager entityManager,BlockManager blockManager){
        this.entityManager = entityManager;
        tileMapRenderer = new TilemapRenderer(blockManager);
    }
    public void drawMainMenu(Graphics graphics){
        initDraw(graphics);
        graphics.setColor(Color.red);
        String mainmenuText = GameLauncher.PROGRAM_NAME;
        graphics.drawString(mainmenuText, calculateWorldTitleLocationX(graphics, mainmenuText), 100);
        String pressText = "Press SPACE to play!";
        graphics.drawString(pressText, calculateWorldTitleLocationX(graphics, pressText), 924);
    }
    public void drawWorldSelection(Graphics graphics,GameWorldInfoContainer gwic){
        initDraw(graphics);
        graphics.setColor(Color.red);
        graphics.drawString(gwic.worldTitle, calculateWorldTitleLocationX(graphics, gwic.worldTitle), 100);
        
        if(gwic.moreLeft){
            
            Shape triangle = new Shape() {
                
                @Override
                public Shape transform(Transform arg0) {
                    // TODO Auto-generated method stub
                    return this;
                }
                
                @Override
                protected void createPoints() {
                    int startX = 100;
                    int startY = RenderingManager.unScaledScreenHeight/2;
                    points = new float[]{startX  , startY -25 ,startX + 50 , startY -50 ,startX+50,startY};
                    
                    
                    
                }
            };
            graphics.fill(triangle);
        }
        if(gwic.moreRight){
            
            Shape triangle = new Shape() {
                
                @Override
                public Shape transform(Transform arg0) {
                    // TODO Auto-generated method stub
                    return this;
                }
                
                @Override
                protected void createPoints() {
                    int startX = RenderingManager.unScaledScreenWidth - 100;
                    int startY = RenderingManager.unScaledScreenHeight/2;
                    points = new float[]{startX  , startY -25 ,startX - 50 , startY -50 ,startX-50,startY};
                    
                    
                    
                }
            };
            graphics.fill(triangle);
        }
    }
    public void drawGamePlay(Graphics graphics,List<int[][]>tileLayers){
        initDraw(graphics);
        //LIGHTING
        //MAP
        tileMapRenderer.draw(screenStartX, screenStartY,tileLayers);
        //OBJECTS
        for(GameObject drawableObject : entityManager.get(GameObject.class)){
            drawableObject.draw();
        }
        //PLAYER
        //EFFECTS
        //UI
    }
    public void drawDebugGamePlay(Graphics graphics,List<int[][]>tileLayers){
        initDraw(graphics);
        //TODO:
    }
    private void initDraw(Graphics graphics){
        graphics.scale((float) ((double)currentResolution.getWidth()/(double)unScaledScreenWidth),(float)((double)currentResolution.getHeight()/(double)unScaledScreenHeight));
        graphics.setBackground(Color.black);
    }
    private int calculateWorldTitleLocationX(Graphics graphics,String title){
        int titleLenght = graphics.getFont().getWidth(title);
        int place = RenderingManager.unScaledScreenWidth/2 - titleLenght/2;
        return place;
    }
    public void drawCharacterCreation(Graphics graphics) {
        initDraw(graphics);
        graphics.setColor(Color.red);
        graphics.drawString("CHARACTER CREATION SCREEN", calculateWorldTitleLocationX(graphics,"CHARACTER CREATION SCREEN" ), 100);
    }
}
