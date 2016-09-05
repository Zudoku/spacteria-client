package fingerprint.rendering;

import java.awt.Dimension;
import java.awt.Font;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.gui.TextField;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Singleton;
import fingerprint.controls.InputManager;

import fingerprint.core.GameLauncher;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.gameplay.objects.GameObject;
import fingerprint.gameplay.objects.player.Player;
import fingerprint.mainmenus.CharacterInfoContainer;
import fingerprint.mainmenus.serverlist.MapDescription;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.rendering.map.TilemapRenderer;
import fingerprint.states.menu.enums.CharacterClass;
import fingerprint.states.menu.enums.MainMenuSelection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

@Singleton
public class RenderingManager {
    private static final Logger logger = Logger.getLogger(RenderingManager.class.getName());
    private TilemapRenderer tileMapRenderer;
    private MainMenuRenderer mainMenuRenderer;
    private UIManager uiManager;
    
    private EntityManager entityManager;
    private InputManager inputManager;
    
    public static RenderingResolutions currentResolution;
    public static int unScaledScreenWidth = 16*TilemapRenderer.tileSize;  //64 = 1024
    public static int unScaledScreenHeight = 16*TilemapRenderer.tileSize; //64 = 1024
    public static Color FONT_BASE_COLOR = Color.red;
    
    private static double screenStartX = 0;
    private static double screenStartY = 0;
    private static double cursorX = 0;
    private static double cursorY = 0;
    
    private int virtualResolutionHeight;
    private int virtualResolutionWidth;
    
    private TrueTypeFont smallVerdanaFont;
    private TrueTypeFont mediumVerdanaFont;
    private TrueTypeFont largeVerdanaFont;
    private TrueTypeFont giganticVerdanaFont;
    
    private Image cursor;
    
    public RenderingManager(){
        currentResolution = GameLauncher.gameSettings.resolution;
        mainMenuRenderer = new MainMenuRenderer();
        uiManager = new UIManager();
        if(currentResolution == RenderingResolutions.IDENTIFY_SCREEN){
            Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            virtualResolutionHeight = (int)dimension.getHeight();
            virtualResolutionWidth = (int)dimension.getWidth();
        }else{
            virtualResolutionHeight = currentResolution.getHeight();
            virtualResolutionWidth = currentResolution.getWidth();
        }
        
        
        
        
    }
    public void setMap(MapDescription map){
        tileMapRenderer.setMap(map.getFilename());
        
    }
    
    private void resetFonts(){
        Font font = new Font("Verdana", Font.PLAIN, 10);
        smallVerdanaFont = new TrueTypeFont(font, true);
        font = new Font("Verdana", Font.PLAIN, 18);
        mediumVerdanaFont = new TrueTypeFont(font, true);
        font = new Font("Verdana", Font.PLAIN, 25);
        largeVerdanaFont = new TrueTypeFont(font, true);
        font = new Font("Verdana", Font.PLAIN, 35);
        giganticVerdanaFont = new TrueTypeFont(font, true);
        
    }
    
    public void configure(EntityManager entityManager,EventBus eventBus, InputManager inputManager){
        this.entityManager = entityManager;
        this.inputManager = inputManager;
        tileMapRenderer = new TilemapRenderer();
        eventBus.register(this);
        eventBus.register(uiManager);
        
    }
    public void drawServerList(Graphics graphics,List<RoomDescription> rooms, int selection){
        initDraw(graphics);
        mainMenuRenderer.drawServerList(graphics, rooms, selection);
    }
    
    public void drawWorldCreation(Graphics graphics,GameContainer container,CharacterClass difficulty,int row,int col,TextField filename,boolean drawBadFileName){
        initDraw(graphics);
        mainMenuRenderer.drawWorldCreation(graphics,container,difficulty,row,col,filename,drawBadFileName);
    }
    public void drawMainMenu(Graphics graphics,MainMenuSelection selection){
        initDraw(graphics);
        mainMenuRenderer.drawMainMenu(graphics, selection);
    }
    public void drawWorldSelection(Graphics graphics,CharacterInfoContainer gwic){
        initDraw(graphics);
        graphics.setColor(FONT_BASE_COLOR);
        
        if(!gwic.isIsCreateNewCharDummy()){
            graphics.drawString(gwic.getFilename(),  calculateTextAllignCenterX(graphics, gwic.getFilename()), 100);
        } else {
            graphics.drawString("Create new Character",  calculateTextAllignCenterX(graphics, "Create new Character"), 100);
        }
        
        
        if(gwic.isMoreLeft()){
            
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
        if(gwic.isMoreRight()){
            
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
    public void drawGamePlay(Graphics graphics,boolean drawDebugInfo, double cameraRotation){
        initDraw(graphics);
        //LIGHTING
        //MAP
        graphics.rotate(unScaledScreenWidth / 2 , unScaledScreenHeight / 2, (float)cameraRotation);
        tileMapRenderer.draw(screenStartX, screenStartY);
        Set<GameObject> asd = entityManager.get(GameObject.class);
        //OBJECTS
        for(GameObject drawableObject : asd){
            if(drawableObject instanceof Player){
                continue;
            }
            if(needToDraw(drawableObject)){
                drawableObject.draw(graphics);
            }
        }
        //PLAYER
        Player player = null;
        for(Player drawableObject : entityManager.get(Player.class)){
            
            drawableObject.draw(graphics);
            graphics.rotate(unScaledScreenWidth / 2 , unScaledScreenHeight / 2, 360f -(float)cameraRotation);
            player = drawableObject;
        }
        //EFFECTS
        //UI
        //graphics.scale(unScaledScreenWidth, unScaledScreenWidth);
        drawGamePlayUI(graphics, drawDebugInfo, cameraRotation);
    }
    private boolean needToDraw(GameObject object){
        Rectangle screen = new Rectangle((float)screenStartX,(float)screenStartY, (float)virtualResolutionWidth,(float) virtualResolutionHeight);
        Rectangle drawing = new Rectangle((float)object.getX(),(float)object.getY(),200f, 200f);
        return screen.intersects(drawing);
    }
    
    
    private void drawGamePlayUI(Graphics graphics,boolean drawDebugInfo, double cameraRotation){
        if(drawDebugInfo){
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, 300, 200);
            
            
            graphics.setFont(smallVerdanaFont);
            graphics.setColor(Color.white);
            graphics.drawString("Memory used: " + (Runtime.getRuntime().totalMemory()/1000000) + "(" + ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1000000) + ") MB", 10, 30);
            graphics.drawString("Screen coordinates: " + screenStartX + "," + screenStartY, 10, 50);
            for(Player drawableObject : entityManager.get(Player.class)){
                graphics.drawString("Player coordinates: " + drawableObject.getX() + "," + drawableObject.getY() + " (" + (int)Math.floor(drawableObject.getX()/64) + "," + (int)Math.floor(drawableObject.getY()/64) + ")", 10, 70);
                graphics.drawString("Player speed (x,y): " + (int)drawableObject.displaySpeedX + "," + (int)drawableObject.displaySpeedY , 10, 90);
                graphics.drawString("Player rectangle (x,y): " +(int)drawableObject.getCollideShape().getX()+"," +(int)drawableObject.getCollideShape().getY() , 10, 110);
                graphics.drawString("Player rotation: " + (int)Math.floor(cameraRotation) , 10, 130);
            }
            graphics.drawString("Mouse coordinates (x,y): " + (int)Math.floor(inputManager.getInput().getAbsoluteMouseX()) + "," + (int)Math.floor(inputManager.getInput().getAbsoluteMouseY()) , 10, 150);
            graphics.drawString("Entities: " + (entityManager.getIdMap().size()), 10, 170);
        }
        
        
        //Draw the console
        
        drawTextEffect(uiManager.getNextConsoleText(), Color.black, Color.yellow, 30, virtualResolutionHeight - 60, 1, graphics, largeVerdanaFont);
        
        //cursor.drawCentered((float)inputManager.getInput().getAbsoluteMouseX(),(float)inputManager.getInput().getAbsoluteMouseY());
    }
    
    
    private void drawTextEffect(String text, Color color1, Color color2, int x, int y, int sizeDiff, Graphics graphics, TrueTypeFont font){
        graphics.setColor(color1);
        graphics.setFont(font);
        graphics.drawString(text, x, y - sizeDiff);
        graphics.drawString(text, x, y + sizeDiff);
        graphics.drawString(text, x + sizeDiff, y);
        graphics.drawString(text, x - sizeDiff, y);
        graphics.setColor(color2);
        graphics.drawString(text, x, y);
    }
    
    
    
    private void initDraw(Graphics graphics){
        graphics.scale((float) ((double)virtualResolutionWidth/(double)unScaledScreenWidth),(float)((double)virtualResolutionHeight/(double)unScaledScreenHeight));
        graphics.setBackground(Color.black);
        if(smallVerdanaFont == null){
            resetFonts();
        }
        if(cursor == null){
            try {
                cursor = new Image("resources/cursor.png");
            } catch (SlickException ex) {
                Logger.getLogger(RenderingManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
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
