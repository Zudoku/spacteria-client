package fingerprint.rendering.manager;

import fingerprint.rendering.gui.event.SetScreenStartCoordinatesEvent;
import fingerprint.rendering.util.RenderingResolutions;
import fingerprint.rendering.util.GamePlayRenderingInformation;
import java.awt.Dimension;
import java.awt.Font;
import java.util.logging.Logger;

import fingerprint.gameplay.items.*;
import fingerprint.gameplay.objects.lootbag.GameItemWrapper;
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
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.inout.FileUtil;
import fingerprint.mainmenus.CharacterInfoContainer;
import fingerprint.mainmenus.GenericGridController;
import fingerprint.mainmenus.serverlist.MapDescription;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.rendering.staterenderers.MainMenuRenderer;
import fingerprint.rendering.map.TilemapRenderer;
import fingerprint.states.menu.enums.CharacterClass;
import fingerprint.states.menu.enums.MainMenuSelection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

@Singleton
public class RenderingManager {
    private static final Logger logger = Logger.getLogger(RenderingManager.class.getName());
    private TilemapRenderer tileMapRenderer;
    private MainMenuRenderer mainMenuRenderer;
    private UIRenderingUtil uiManager;
    private EventBus eventBus;
    
    private EntityManager entityManager;
    private InputManager inputManager;
    
    public static RenderingResolutions currentResolution;
    public static int unScaledGamePlayWidth = 16*TilemapRenderer.tileSize;  //64 = 1024
    public static int unScaledGamePlayHeight = 16*TilemapRenderer.tileSize; //64 = 1024
    public static int unScaledScreenWidth = 20*TilemapRenderer.tileSize;  //64 = 1280
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
    
    
    public RenderingManager(){
        currentResolution = GameLauncher.gameSettings.resolution;
        mainMenuRenderer = new MainMenuRenderer();
        uiManager = new UIRenderingUtil();
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
        this.eventBus = eventBus;
        tileMapRenderer = new TilemapRenderer();
        eventBus.register(this);
        eventBus.register(uiManager);
        
    }
    public void drawServerList(Graphics graphics,List<RoomDescription> rooms, int selection){
        initDraw(graphics);
        mainMenuRenderer.drawServerList(graphics, rooms, selection);
    }
    
    public void drawLogin(Graphics graphics, GameContainer gameContainer, TextField username, TextField password, GenericGridController controller) {
        initDraw(graphics);
        mainMenuRenderer.drawLoginToGame(graphics, gameContainer, username, password, controller);
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
            graphics.drawString(gwic.getPlayerData().getName(),  calculateTextAllignCenterX(graphics, gwic.getPlayerData().getName()), 100);
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
                    int startY = RenderingManager.unScaledGamePlayHeight/2;
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
                    int startX = RenderingManager.unScaledGamePlayWidth - 100;
                    int startY = RenderingManager.unScaledGamePlayHeight/2;
                    points = new float[]{startX  , startY -25 ,startX - 50 , startY -50 ,startX-50,startY};
                    
                    
                    
                }
            };
            graphics.fill(triangle);
        }
    }
    public void drawGamePlay(Graphics graphics, GUIContext context, boolean drawDebugInfo, GamePlayRenderingInformation gri){
        initDraw(graphics);
        //LIGHTING
        //MAP
        graphics.rotate(unScaledGamePlayWidth / 2 , unScaledGamePlayHeight / 2, (float)gri.getCameraRotation());
        tileMapRenderer.draw(screenStartX, screenStartY);
        Set<GameObject> asd = entityManager.get(GameObject.class);
        //OBJECTS
        for(GameObject drawableObject : asd){
            if(drawableObject instanceof GCharacter){
                continue;
            }
            if(needToDraw(drawableObject)){
                drawableObject.draw(graphics);
            }
        }
        //PLAYER
        GCharacter player = null;
        for(GCharacter drawableObject : entityManager.get(GCharacter.class)){
            drawableObject.draw(graphics);
            graphics.rotate(unScaledGamePlayWidth / 2 , unScaledGamePlayHeight / 2, 360f -(float)gri.getCameraRotation());
            player = drawableObject;
        }
        
        //EFFECTS
        //UI
        //graphics.setColor(FONT_BASE_COLOR);
        //graphics.scale(unScaledScreenWidth, unScaledScreenWidth);
        //graphics.rotate(unScaledGamePlayWidth / 2 , unScaledGamePlayHeight / 2, (float)gri.getCameraRotation());
        drawGamePlayUI(graphics, context, drawDebugInfo, gri);
    }
    private boolean needToDraw(GameObject object){
        
        final int SCREENBUFFER = 400;
        final int DRAWINGBUFFER = 100;
        
        float needToDrawScreenX = (float)Math.max(screenStartX - SCREENBUFFER, 0f);
        float needToDrawScreenY = (float)Math.max(screenStartY - SCREENBUFFER, 0f);
        
        float needToDrawDrawingX = (float)Math.max(object.getX() - DRAWINGBUFFER, 0f);
        float needToDrawDrawingY = (float)Math.max(object.getY() - DRAWINGBUFFER, 0f);
        
        Rectangle screen = new Rectangle(needToDrawScreenX, needToDrawScreenY, (float)unScaledGamePlayWidth + (2 * SCREENBUFFER),(float) unScaledGamePlayHeight + (2 * SCREENBUFFER));
        Rectangle drawing = new Rectangle(needToDrawDrawingX, needToDrawDrawingY, 2 * DRAWINGBUFFER, 2 * DRAWINGBUFFER);
        
        return screen.intersects(drawing); 
    }
    
    
    private void drawGamePlayUI(Graphics graphics, GUIContext context, boolean drawDebugInfo, GamePlayRenderingInformation gri){
        int MINI_PADDING = 4;
        int SMALL_PADDING = 10;
        int ITEM_PADDING = 56;
        int TS = TilemapRenderer.tileSize;

        graphics.setBackground(Color.cyan);
        
        //TODO: MAGIC NUMBERS!
        if(drawDebugInfo){
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, 300, 200);
            
            
            graphics.setFont(smallVerdanaFont);
            graphics.setColor(Color.white);
            graphics.drawString("Memory used: " + (Runtime.getRuntime().totalMemory()/1000000) + "(" + ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1000000) + ") MB", 10, 30);
            graphics.drawString("Screen coordinates: " + screenStartX + "," + screenStartY, 10, 50);
            for(GCharacter drawableObject : entityManager.get(GCharacter.class)){
                graphics.drawString("Player coordinates: " + drawableObject.getX() + "," + drawableObject.getY() + " (" + (int)Math.floor(drawableObject.getX()/64) + "," + (int)Math.floor(drawableObject.getY()/64) + ")", 10, 70);
                graphics.drawString("Player speed (x,y): " + (int)drawableObject.displaySpeedX + "," + (int)drawableObject.displaySpeedY , 10, 90);
                graphics.drawString("Player rectangle (x,y): " +(int)drawableObject.getCollideShape().getX()+"," +(int)drawableObject.getCollideShape().getY() , 10, 110);
                graphics.drawString("Player rotation: " + (int)Math.floor(gri.getCameraRotation()) , 10, 130);
            }
            graphics.drawString("Mouse coordinates (x,y): " + (int)Math.floor(inputManager.getInput().getAbsoluteMouseX()) + "," + (int)Math.floor(inputManager.getInput().getAbsoluteMouseY()) , 10, 150);
            graphics.drawString("Entities: " + (entityManager.getIdMap().size()), 10, 170);
        }
        
        
        //Draw the console
        
        //drawTextEffect(uiManager.getNextConsoleText(), Color.black, Color.yellow, 30, virtualResolutionHeight - 60, 1, graphics, largeVerdanaFont);
        
        //Draw the background for real UI
        graphics.setColor(Color.darkGray);
        graphics.fillRect(unScaledGamePlayWidth, 0, 4 * TS, unScaledScreenHeight);
        
        //Draw minimap
        graphics.setColor(Color.blue);
        graphics.fillRect(unScaledGamePlayWidth, 0, 4 * TS, 4 * TS);
        graphics.setColor(Color.gray);
        graphics.setLineWidth(2);
        graphics.drawRect(unScaledGamePlayWidth, 0, 4 * TS, 4 * TS);
        //Draw map name
        graphics.setLineWidth(1);
        graphics.setColor(Color.white);
        graphics.fillRect(unScaledGamePlayWidth, 4 * TS, 4 * TS, 20);
        graphics.setColor(Color.black);
        graphics.drawRect(unScaledGamePlayWidth, 4 * TS, 4 * TS -1, 20);
        graphics.setFont(smallVerdanaFont);
        graphics.setColor(Color.black);
        String interactText = (gri.getPortalToRender() == null) ? "" : (" Space to enter: " + gri.getPortalToRender().getTo());
        graphics.drawString(interactText, unScaledGamePlayWidth + MINI_PADDING, 4 * TS + MINI_PADDING);
        //Draw character information

        drawTextEffect(gri.getMyName(), Color.black, Color.yellow, unScaledGamePlayWidth + 10, 5 * TS +10, 1, graphics, largeVerdanaFont);
        graphics.setFont(smallVerdanaFont);
        graphics.setColor(Color.black);
        graphics.drawString("(" + gri.getCharClass().name() + ")", unScaledGamePlayWidth + 10, 5 * TS +40);

        //EXP

        double xppercent = (double) gri.getExperience() / (double) gri.getMyStats().getExpRequirements()[gri.getLevel() - 1];
        int xppurplewidth = (int) (xppercent * (4 * TS -20));
        if(gri.getExperience() < 0){
            xppurplewidth = 0;
        }
        graphics.setColor(Color.magenta);
        graphics.fillRect(unScaledGamePlayWidth +10, 6 * TS,xppurplewidth, 20);
        graphics.setColor(Color.black);
        graphics.drawRect(unScaledGamePlayWidth +10, 6 * TS, 4 * TS -21, 20);
        graphics.setFont(smallVerdanaFont);
        graphics.setColor(Color.black);
        graphics.drawString("Level: " + gri.getLevel() + " Experience: " + gri.getExperience() + " / " + gri.getMyStats().getExpRequirements()[gri.getLevel() - 1], unScaledGamePlayWidth + 14, 6 * TS + 3);
        //HP
        graphics.setColor(Color.green);
        double percent = (double) gri.getMyStats().getHealth() / (double) gri.getMyStats().getMaxhealth();
        int hpgreenwidth = (int) (percent * (4 * TS -20));
        if(gri.getMyStats().getHealth() < 0){
            hpgreenwidth = 0;
        }
        graphics.fillRect(unScaledGamePlayWidth +10, 6 * TS +24, hpgreenwidth, 20);
        graphics.setColor(Color.black);
        graphics.drawRect(unScaledGamePlayWidth +10, 6 * TS +24, 4 * TS -21, 20);
        graphics.setFont(smallVerdanaFont);
        graphics.setColor(Color.black);
        graphics.drawString("Health: " + gri.getMyStats().getHealth()+ " / " + gri.getMyStats().getMaxhealth() + "", unScaledGamePlayWidth + 14, 6 * TS + 27);
        
        graphics.setColor(Color.gray);
        graphics.fillRect(unScaledGamePlayWidth +10, 7 * TS, 4 * TS -20, 100);
        graphics.setColor(Color.black);
        graphics.drawRect(unScaledGamePlayWidth +10, 7 * TS, 4 * TS -21, 100);
        
        graphics.setFont(smallVerdanaFont);
        graphics.setColor(Color.black);
        //STATS
        graphics.drawString("Strength: ", unScaledGamePlayWidth +14, 7 * TS + MINI_PADDING);
        graphics.drawString("" + gri.getMyStats().getStrength(), unScaledGamePlayWidth +88, 7 * TS + MINI_PADDING);
        graphics.drawString("Dexterity: ", unScaledGamePlayWidth +128, 7 * TS + 4);
        graphics.drawString("" + gri.getMyStats().getDexterity(), unScaledGamePlayWidth +210, 7 * TS + MINI_PADDING);
        graphics.drawString("Speed: ", unScaledGamePlayWidth +14, 7 * TS + MINI_PADDING + (1 * 16));
        graphics.drawString("" + gri.getMyStats().getSpeed(), unScaledGamePlayWidth +88, 7 * TS + MINI_PADDING + (1 * 16));
        graphics.drawString("Defence: ", unScaledGamePlayWidth +128, 7 * TS + MINI_PADDING + (1 * 16));
        graphics.drawString("" + gri.getMyStats().getDefence(), unScaledGamePlayWidth +210, 7 * TS + MINI_PADDING + (1 * 16));
        graphics.drawString("Vitality: ", unScaledGamePlayWidth +14, 7 * TS + MINI_PADDING + (2 * 16));
        graphics.drawString("" + gri.getMyStats().getVitality(), unScaledGamePlayWidth +88, 7 * TS + MINI_PADDING + (2 * 16));
        graphics.drawString("", unScaledGamePlayWidth +128, 7 * TS + MINI_PADDING + (2 * 16));
        graphics.drawString("", unScaledGamePlayWidth +210, 7 * TS + MINI_PADDING + (2 * 16));
        
        //INVENTORY + EQUIP
        
        graphics.setColor(Color.gray);
        graphics.fillRect(unScaledGamePlayWidth +SMALL_PADDING, 9 * TS, 4 * TS -20, 124);
        graphics.setColor(Color.black);
        graphics.drawRect(unScaledGamePlayWidth +SMALL_PADDING, 9 * TS, 4 * TS -21, 124);
        
        for(int t = 0; t < 4; t++){
            try {
                graphics.drawRect(unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9, 48, 48);
                if(gri.getEquipmentToRender().getItem(t) != null){
                    drawItem(gri.getEquipmentToRender().getItem(t).getImageid(),unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9);
                } else {
                    drawEquipment(t,unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9);
                }
                graphics.drawRect(unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9 + ITEM_PADDING, 48, 48);
                if(gri.getEquipmentToRender().getItem(t + 4) != null){
                    drawItem(gri.getEquipmentToRender().getItem(t + 4).getImageid(),unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9 + ITEM_PADDING);
                } else {
                    drawEquipment(t + 4,unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9 + ITEM_PADDING);
                }
                
            } catch (SlickException ex) {
                Logger.getLogger(RenderingManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        graphics.setColor(Color.gray);
        graphics.fillRect(unScaledGamePlayWidth +10, 11 * TilemapRenderer.tileSize, 4 * TilemapRenderer.tileSize -20, 300);
        graphics.setColor(Color.black);
        graphics.drawRect(unScaledGamePlayWidth +10, 11 * TilemapRenderer.tileSize, 4 * TilemapRenderer.tileSize -21, 300);
        
        
        graphics.setFont(smallVerdanaFont);
        for (int u = 0; u < 5; u++) {
            for (int o = 0; o < 4; o++) {
                int ix = unScaledGamePlayWidth + 19 + (ITEM_PADDING * o);
                int iy = 11 * TS + 9 + (u * ITEM_PADDING);
                graphics.setColor(Color.black);
                graphics.drawRect(ix, iy, 48, 48);
                int index = o + 1 + u * 4;
                GameItemWrapper itemData = gri.getInventoryToRender().getItem(index);
                if (itemData != null) {
                    if (itemData.getUniqueid() == -1) {
                        drawItem(1, ix, iy);
                    } else {
                        drawItem(itemData.getData().getImageid(), ix, iy);
                    }
                    graphics.setColor(Color.red);
                    graphics.drawString(Integer.toString(itemData.getAmount()), ix + 2, iy);
                }

            }
        }

        //LOOT INFORMATION
        if (gri.getLootToRender() != null) {
            graphics.setColor(Color.gray);
            graphics.fillRect(unScaledGamePlayWidth - 250, 14 * TS - 20, 4 * TS - 20, 120);
            graphics.setColor(Color.black);
            graphics.drawRect(unScaledGamePlayWidth - 250, 14 * TS - 20, 4 * TS - 21, 120);

            for (int u = 0; u < 2; u++) {
                for (int o = 0; o < 4; o++) {
                    int xPos = unScaledGamePlayWidth - 241 + (ITEM_PADDING * o);
                    int yPos = 14 * TS - 11 + (u * ITEM_PADDING);
                    graphics.setColor(Color.black);
                    graphics.drawRect(xPos, yPos, 48, 48);
                    int lootIndex = (u * 4) + o;
                    if (gri.getLootToRender().getItems().size() > lootIndex) {
                        GameItemWrapper itemToRender = gri.getLootToRender().getItems().get(lootIndex);
                        if (itemToRender.getUniqueid() == -1) {
                            drawItem(1, xPos, yPos);

                            graphics.setColor(Color.red);
                            graphics.drawString(Integer.toString(itemToRender.getAmount()), xPos + 2, yPos);
                            //coins
                        } else {
                            drawItem(itemToRender.getData().getImageid(), xPos, yPos);
                            graphics.setColor(Color.red);
                            graphics.drawString(Integer.toString(itemToRender.getAmount()), xPos + 2, yPos);
                        }
                    }
                }
            }
        }

        if(gri.getHoverGameItem() != null) {
            GameItem i = gri.getHoverGameItem();
            int mx = inputManager.getInput().getMouseX() - 300;
            int my = inputManager.getInput().getMouseY() - 150;

            graphics.setColor(Color.darkGray);
            graphics.fillRect(mx, my, 200, 260);
            graphics.setColor(Color.black);
            graphics.drawRect(mx, my, 200, 260);

            ItemRarity rarity = ItemRarity.values()[i.getRarity() - 1];

            graphics.setColor(rarity.getColor());
            graphics.drawString(i.getDisplayname(), mx + 6, my + 14);
            graphics.drawString(i.getDisplayname(), mx + 7, my + 14);
            graphics.drawString(rarity.getName(), mx + 6, my + 28);

            graphics.setColor(Color.white);
            graphics.drawString(ItemType.values()[i.getItemtypeid() - 1].getText(), mx + 6, my + 42);

            Color usableColor = i.getLevelreq() >= gri.getLevel() ? Color.white : Color.red;
            graphics.setColor(usableColor);
            graphics.drawString("Level requirement: " + i.getLevelreq(), mx + 6, my + 56);

            graphics.setColor(Color.yellow);
            graphics.drawString(i.getSellvalue() + " gold", mx + 6, my + 70);

            graphics.setColor(Color.white);
            graphics.drawString(i.isStackable() ? "Stackable" : "Single", mx + 6, my + 84);
            graphics.drawString(i.isTradeable() ? "Tradeable" : "Untradeable", mx + 6, my + 98);

            graphics.setColor(Color.green);
            int index = 0;
            for(GameItemAttribute attr : i.getAttributes()) {
                graphics.drawString(attr.toString(), mx + 6, my + 112 + (index * 14));
                index++;
            }

            graphics.setColor(Color.lightGray);
            graphics.drawString(i.getDescription(), mx + 6, my + 246);

        }

    }

    private void drawEquipment(int position,float x, float y) throws SlickException{
        Image image = null;
        switch(position){
            case 0:
                image = new Image(FileUtil.UI_FILES_PATH + "/helmet.png");
                break;
            case 1:
                image = new Image(FileUtil.UI_FILES_PATH + "/pants.png");
                break;
            case 2:
                image = new Image(FileUtil.UI_FILES_PATH + "/shoulder.png");
                break;
            case 3:
                image = new Image(FileUtil.UI_FILES_PATH + "/weapon.png");
                break;
            case 4:
                image = new Image(FileUtil.UI_FILES_PATH + "/chest.png");
                break;
            case 5:
                image = new Image(FileUtil.UI_FILES_PATH + "/boots.png");
                break;
            case 6:
                image = new Image(FileUtil.UI_FILES_PATH + "/ring.png");
                break;
            case 7:
                image = new Image(FileUtil.UI_FILES_PATH + "/relic.png");
                break;
                
            default:
                image = new Image(FileUtil.UI_FILES_PATH + "/helmet.png");
                
        }
        
        image.draw(x, y);
    }

    private void drawItem(int imageid, float x, float y) {
        uiManager.getItemImage(imageid).draw(x,y);
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
        
        
    }
    
    
    
    
    
    
    public static int calculateTextAllignCenterX(Graphics graphics,String title){
        int titleLenght = graphics.getFont().getWidth(title);
        int place = RenderingManager.unScaledGamePlayWidth/2 - titleLenght/2;
        
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
