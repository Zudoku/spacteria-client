package fingerprint.rendering;

import java.awt.Dimension;
import java.io.ObjectInputStream.GetField;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.tiled.TiledMapPlus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import fingerprint.core.GameLauncher;
import fingerprint.gameplay.map.blocks.BlockManager;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.gameplay.objects.GameObject;
import fingerprint.gameplay.objects.player.Player;
import fingerprint.mainmenus.GameWorldInfoContainer;
import fingerprint.rendering.map.TilemapRenderer;
import fingerprint.states.MainMenuState;
import fingerprint.states.menu.enums.GameDifficulty;
import fingerprint.states.menu.enums.MainMenuSelection;

@Singleton
public class RenderingManager {
    private static final Logger logger = Logger.getLogger(RenderingManager.class.getName());
    private TilemapRenderer tileMapRenderer;
    private MainMenuRenderer mainMenuRenderer;
    
    @Inject private EntityManager entityManager;
    
    public static RenderingResolutions currentResolution;
    public static int unScaledScreenWidth = 26*TilemapRenderer.tileSize;  //64 = 1664
    public static int unScaledScreenHeight = 16*TilemapRenderer.tileSize; //64 = 1024
    public static Color FONT_BASE_COLOR = Color.red;
    
    private static double screenStartX = 0;
    private static double screenStartY = 0;
    
    private int virtualResolutionHeight;
    private int virtualResolutionWidth;
    
    public RenderingManager() {
        currentResolution = GameLauncher.gameSettings.resolution;
        mainMenuRenderer = new MainMenuRenderer();
        if(currentResolution == RenderingResolutions.IDENTIFY_SCREEN){
            Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            virtualResolutionHeight = (int)dimension.getHeight();
            virtualResolutionWidth = (int)dimension.getWidth();
        }else{
            virtualResolutionHeight = currentResolution.getHeight();
            virtualResolutionWidth = currentResolution.getWidth();
        }
    }
    public void setWorld(String world){
        tileMapRenderer.setWorld(world);
    }
    public void configure(EntityManager entityManager,BlockManager blockManager,EventBus eventBus){
        this.entityManager = entityManager;
        tileMapRenderer = new TilemapRenderer(blockManager);
        eventBus.register(this);
    }
    public void drawWorldCreation(Graphics graphics,GameContainer container,GameDifficulty difficulty,int row,int col,TextField filename,boolean drawBadFileName){
        initDraw(graphics);
        mainMenuRenderer.drawWorldCreation(graphics,container,difficulty,row,col,filename,drawBadFileName);
    }
    public void drawMainMenu(Graphics graphics,MainMenuSelection selection){
        initDraw(graphics);
        mainMenuRenderer.drawMainMenu(graphics, selection);
    }
    public void drawWorldSelection(Graphics graphics,GameWorldInfoContainer gwic){
        initDraw(graphics);
        graphics.setColor(FONT_BASE_COLOR);
        graphics.drawString(gwic.worldTitle,  calculateTextAllignCenterX(graphics, gwic.worldTitle), 100);
        
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
    public void drawGamePlay(Graphics graphics){
        initDraw(graphics);
        //LIGHTING
        //MAP
        tileMapRenderer.draw(screenStartX, screenStartY);
        //OBJECTS
        for(GameObject drawableObject : entityManager.get(GameObject.class)){
            if(drawableObject instanceof Player){
                continue;
            }
            if(needToDraw(drawableObject)){
                drawableObject.draw(graphics);
            }
        }
        //PLAYER
        for(Player drawableObject : entityManager.get(Player.class)){
            drawableObject.draw(graphics);
        }
        //EFFECTS
        //UI
    }
    private boolean needToDraw(GameObject object){
        Rectangle screen = new Rectangle((float)screenStartX,(float)screenStartY, (float)virtualResolutionWidth,(float) virtualResolutionHeight);
        Rectangle drawing = new Rectangle((float)object.getX(),(float)object.getY(),200f, 200f);
        return screen.intersects(drawing);
    }
    public void drawDebugGamePlay(Graphics graphics,List<int[][]>tileLayers){
        initDraw(graphics);
        //TODO:
    }
    private void initDraw(Graphics graphics){
        graphics.scale((float) ((double)virtualResolutionWidth/(double)unScaledScreenWidth),(float)((double)virtualResolutionHeight/(double)unScaledScreenHeight));
        graphics.setBackground(Color.black);
    }
    public static int calculateTextAllignCenterX(Graphics graphics,String title){
        int titleLenght = graphics.getFont().getWidth(title);
        int place = RenderingManager.unScaledScreenWidth/2 - titleLenght/2;
        
        return place;
    }
    public void drawCharacterCreation(Graphics graphics) {
        initDraw(graphics);
        graphics.setColor(FONT_BASE_COLOR);
        graphics.drawString("CHARACTER CREATION SCREEN",  calculateTextAllignCenterX(graphics,"CHARACTER CREATION SCREEN" ), 100);
    }
    public void setScreenStartX(double screenX) {
        screenStartX = screenX;
    }
    public void setScreenStartY(double screenY) {
        screenStartY = screenY;
    }
    public static double getScreenStartX() {
        return screenStartX;
    }
    public static double getScreenStartY() {
        return screenStartY;
    }
    @Subscribe
    public void listenSetScreenStartCoordinates(SetScreenStartCoordinatesEvent event){
        screenStartX = event.newX;
        screenStartY = event.newY;
    }
}
