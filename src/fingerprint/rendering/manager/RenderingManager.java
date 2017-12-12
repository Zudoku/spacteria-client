package fingerprint.rendering.manager;

import com.google.common.collect.Sets;
import fingerprint.rendering.gui.event.SetScreenStartCoordinatesEvent;
import fingerprint.rendering.util.RenderingResolutions;
import fingerprint.rendering.util.GamePlayRenderingInformation;
import java.awt.Dimension;
import java.util.logging.Logger;

import fingerprint.gameplay.items.*;
import fingerprint.gameplay.objects.interact.GameItemWrapper;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.TextField;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Singleton;
import fingerprint.controls.InputManager;

import fingerprint.core.GameLauncher;
import fingerprint.gameplay.map.gameworld.UIMode;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.gameplay.objects.GameObject;
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.gameplay.objects.projectiles.Projectile;
import fingerprint.gameplay.objects.projectiles.ProjectileImage;
import fingerprint.inout.Chatline;
import fingerprint.mainmenus.CharacterInfoContainer;
import fingerprint.mainmenus.GenericGridController;
import fingerprint.mainmenus.serverlist.MapDescription;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.rendering.staterenderers.MainMenuRenderer;
import fingerprint.rendering.map.TilemapRenderer;
import fingerprint.rendering.util.ConnectionRenderingInformation;
import fingerprint.states.menu.enums.CharacterClass;
import fingerprint.states.menu.enums.MainMenuSelection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    
    public void drawLogin(Graphics graphics, GameContainer gameContainer, 
            TextField username, TextField password, GenericGridController controller,
            ConnectionRenderingInformation connectionInformation) {
        initDraw(graphics);
        mainMenuRenderer.drawLoginToGame(graphics, gameContainer, username, password, controller, connectionInformation);
    }
    
    public void drawCharacterCreation(Graphics graphics,GameContainer container,int phase,TextField characternameTextField, String naggingText){
        initDraw(graphics);
        mainMenuRenderer.drawCharacterCreation(graphics,container,phase,characternameTextField, naggingText);
    }
    public void drawMainMenu(Graphics graphics,MainMenuSelection selection){
        initDraw(graphics);
        mainMenuRenderer.drawMainMenu(graphics, selection);
    }
    public void drawCharacterSelection(Graphics graphics,CharacterInfoContainer gwic, List<CharacterInfoContainer> availableChars, int deleteCounter){
        initDraw(graphics);
        mainMenuRenderer.drawCharSelection(graphics, gwic, availableChars, deleteCounter);
    }
    public void drawGamePlay(Graphics graphics, GUIContext context, boolean drawDebugInfo, GamePlayRenderingInformation gri){
        initDraw(graphics);
        //LIGHTING
        //MAP
        graphics.rotate(unScaledGamePlayWidth / 2 , unScaledGamePlayHeight / 2, (float)gri.getCameraRotation());
        tileMapRenderer.draw(screenStartX, screenStartY);
        
        
        Set<GameObject> allDrawableGameObjects = entityManager.get(GameObject.class);
        Map<ProjectileImage, Set<Projectile>> projectiles = new HashMap<>();
        //OBJECTS
        for(GameObject drawableObject : allDrawableGameObjects){
            if(drawableObject instanceof Projectile){
                ProjectileImage image = ((Projectile) drawableObject).getImage();
                if(projectiles.containsKey(image)){
                    Set<Projectile> set = projectiles.get(image);
                    set.add((Projectile) drawableObject);
                    projectiles.put(image, set);
                } else {
                    projectiles.put(image, new HashSet<>(Sets.newHashSet((Projectile) drawableObject)));
                }
                
                continue;
            }
            if(drawableObject instanceof GCharacter){
                continue;
            }
            if(needToDraw(drawableObject)){
                drawableObject.draw(graphics);
            }
        }
        projectiles.keySet().forEach(imageKey -> {
            try {
                Set<Projectile> batch = projectiles.get(imageKey);
                Image imageRef = batch.iterator().next().getImageRef();
                imageRef.startUse();
                batch.stream().forEach((projectile) -> {
                    float offsetX = (imageRef.getWidth() - projectile.getWidth()) / 2;
                    float offsetY = (imageRef.getHeight() - projectile.getHeight()) / 2;
                    float rotation = 360 - (int) projectile.getAngle();
                    imageRef.drawEmbedded((float)projectile.getDrawingCoordinates()[0] - offsetX, 
                            (float)projectile.getDrawingCoordinates()[1] - offsetY,
                            imageRef.getWidth(),
                            imageRef.getHeight(),
                            rotation);
                });
                imageRef.endUse();
            } catch (SlickException ex) {
                Logger.getLogger(RenderingManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //PLAYER
        GCharacter player = null;
        for(GCharacter drawableObject : entityManager.get(GCharacter.class)){
            graphics.rotate(unScaledGamePlayWidth / 2 , unScaledGamePlayHeight / 2, 360f -(float)gri.getCameraRotation());
            drawableObject.draw(graphics);
            player = drawableObject;
        }
        
        //EFFECTS
        //UI
        
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

        //graphics.setBackground(Color.cyan);
        
        //TODO: MAGIC NUMBERS!
        if(drawDebugInfo){
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, 200, 100);
            
            
            graphics.setFont(UIRenderingUtil.smallVerdanaFont);
            graphics.setColor(Color.white);
            graphics.drawString("Memory: " + (Runtime.getRuntime().totalMemory()/1000000) + "(" + ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1000000) + ") MB", 10, 30);
            for(GCharacter drawableObject : entityManager.get(GCharacter.class)){
                graphics.drawString("Coord: " + drawableObject.getX() + "," + drawableObject.getY() + " (" + (int)Math.floor(drawableObject.getX()/64) + "," + (int)Math.floor(drawableObject.getY()/64) + ")", 10, 45);
                graphics.drawString("Rotation: " + (int)Math.floor(gri.getCameraRotation()) , 10, 60);
            }
            graphics.drawString("Entities: " + (entityManager.getIdMap().size()), 10, 75);
        }
        
        //Draw the background for real UI
        graphics.setColor(Color.darkGray);
        graphics.fillRect(unScaledGamePlayWidth, 0, 4 * TS, unScaledScreenHeight);
        
        //Draw minimap
        Color bgMinimapColor = new Color(0, 40, 0);
        Color walkableMinimapColor = new Color(200, 255, 200);
        graphics.setColor(walkableMinimapColor);
        graphics.fillRect(unScaledGamePlayWidth, 0, 4 * TS, 4 * TS);
        graphics.setColor(bgMinimapColor);
        if (gri.getMinimap() != null) {
            for (int y = 0; y < gri.getMinimap().length; y++) {
                for (int x = 0; x < gri.getMinimap()[0].length; x++) {
                    if (gri.getMinimap()[y][x] == 2) {
                        graphics.setColor(Color.red);
                        graphics.fillRect(unScaledGamePlayWidth + (x * 2), y * 2, 2, 2);
                        graphics.setColor(bgMinimapColor);
                    }
                    if (gri.getMinimap()[y][x] == 3) {
                        graphics.setColor(Color.blue);
                        graphics.fillRect(unScaledGamePlayWidth + (x * 2), y * 2, 2, 2);
                        graphics.setColor(bgMinimapColor);
                    }
                    if (gri.getMinimap()[y][x] == 0) {
                        graphics.fillRect(unScaledGamePlayWidth + (x * 2), y * 2, 2, 2);
                    }
                }
            }
        }
        graphics.setColor(Color.gray);
        graphics.setLineWidth(2);
        graphics.drawRect(unScaledGamePlayWidth, 0, 4 * TS, 4 * TS);
        //Draw map name
        graphics.setLineWidth(1);
        graphics.setColor(Color.white);
        graphics.fillRect(unScaledGamePlayWidth, 4 * TS, 4 * TS, 20);
        graphics.setColor(Color.black);
        graphics.drawRect(unScaledGamePlayWidth, 4 * TS, 4 * TS -1, 20);
        graphics.setFont(UIRenderingUtil.smallVerdanaFont);
        graphics.setColor(Color.black);
        String interactText = (gri.getCollidedInteractable() == null) ? "" : (gri.getCollidedInteractable().getInteractionText());
        graphics.drawString(interactText, unScaledGamePlayWidth + MINI_PADDING, 4 * TS + MINI_PADDING);
        //Draw character information

        UIRenderingUtil.drawTextEffect(gri.getMyName(), Color.black, Color.yellow, unScaledGamePlayWidth + 10, 5 * TS +10, 1, graphics, UIRenderingUtil.largeVerdanaFont);
        graphics.setFont(UIRenderingUtil.smallVerdanaFont);
        graphics.setColor(Color.black);

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
        graphics.setFont(UIRenderingUtil.smallVerdanaFont);
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
        graphics.setFont(UIRenderingUtil.smallVerdanaFont);
        graphics.setColor(Color.black);
        graphics.drawString("Health: " + gri.getMyStats().getHealth()+ " / " + gri.getMyStats().getMaxhealth() + "", unScaledGamePlayWidth + 14, 6 * TS + 27);
        
        graphics.setColor(Color.gray);
        graphics.fillRect(unScaledGamePlayWidth +10, 7 * TS, 4 * TS -20, 124);
        graphics.setColor(Color.black);
        graphics.drawRect(unScaledGamePlayWidth +10, 7 * TS, 4 * TS -21, 124);
        
        graphics.setFont(UIRenderingUtil.smallVerdanaFont);
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
        
        //CURRENCIES
        graphics.drawString("Coins: ", unScaledGamePlayWidth +14, 7 * TS + MINI_PADDING + (4 * 16));
        graphics.drawString("" + gri.getCurrencies().getCoin(), unScaledGamePlayWidth +88, 7 * TS + MINI_PADDING + (4 * 16));
        graphics.drawString("Bugbounty: ", unScaledGamePlayWidth +128, 7 * TS + 4 + (4 * 16));
        graphics.drawString("" + gri.getCurrencies().getBugbounty(), unScaledGamePlayWidth +210, 7 * TS + MINI_PADDING + (4 * 16));
        graphics.drawString("Rollticket: ", unScaledGamePlayWidth +14, 7 * TS + MINI_PADDING + (5 * 16));
        graphics.drawString("" + gri.getCurrencies().getRollticket(), unScaledGamePlayWidth +88, 7 * TS + MINI_PADDING + (5 * 16));
        graphics.drawString("", unScaledGamePlayWidth +128, 7 * TS + 4 + (5 * 16));
        graphics.drawString("", unScaledGamePlayWidth +210, 7 * TS + MINI_PADDING + (5 * 16));
        //INVENTORY + EQUIP
        
        graphics.setColor(Color.gray);
        graphics.fillRect(unScaledGamePlayWidth +SMALL_PADDING, 9 * TS, 4 * TS -20, 124);
        graphics.setColor(Color.black);
        graphics.drawRect(unScaledGamePlayWidth +SMALL_PADDING, 9 * TS, 4 * TS -21, 124);
        
        for(int t = 0; t < 4; t++){
            try {
                graphics.drawRect(unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9, 48, 48);
                if(gri.getEquipmentToRender().getItem(t) != null){
                    UIRenderingUtil.drawItem(gri.getEquipmentToRender().getItem(t).getImageid(),unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9);
                } else {
                    UIRenderingUtil.drawEquipmentSymbol(t,unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9);
                }
                graphics.drawRect(unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9 + ITEM_PADDING, 48, 48);
                if(gri.getEquipmentToRender().getItem(t + 4) != null){
                    UIRenderingUtil.drawItem(gri.getEquipmentToRender().getItem(t + 4).getImageid(),unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9 + ITEM_PADDING);
                } else {
                    UIRenderingUtil.drawEquipmentSymbol(t + 4,unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9 + ITEM_PADDING);
                }
                
            } catch (SlickException ex) {
                Logger.getLogger(RenderingManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        graphics.setColor(Color.gray);
        if(gri.getUiMode() == UIMode.SHOP){
            graphics.setColor(Color.orange);
        }
        graphics.fillRect(unScaledGamePlayWidth +10, 11 * TilemapRenderer.tileSize, 4 * TilemapRenderer.tileSize -20, 300);
        graphics.setColor(Color.black);
        graphics.drawRect(unScaledGamePlayWidth +10, 11 * TilemapRenderer.tileSize, 4 * TilemapRenderer.tileSize -21, 300);
        
        
        graphics.setFont(UIRenderingUtil.smallVerdanaFont);
        for (int u = 0; u < 5; u++) {
            for (int o = 0; o < 4; o++) {
                int index = o + 1 + u * 4;
                GameItemWrapper itemData = gri.getInventoryToRender().getItem(index);
                int ix = unScaledGamePlayWidth + 19 + (ITEM_PADDING * o);
                int iy = 11 * TS + 9 + (u * ITEM_PADDING);
                graphics.setColor(Color.black);
                graphics.drawRect(ix, iy, 48, 48);
                
                
                if (itemData != null) {
                    if (itemData.getUniqueid() == -1) {
                        UIRenderingUtil.drawItem(1, ix, iy);
                    } else {
                        UIRenderingUtil.drawItem(itemData.getData().getImageid(), ix, iy);
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
                            UIRenderingUtil.drawItem(1, xPos, yPos);

                            graphics.setColor(Color.red);
                            graphics.drawString(Integer.toString(itemToRender.getAmount()), xPos + 2, yPos);
                            //coins
                        } else {
                            UIRenderingUtil.drawItem(itemToRender.getData().getImageid(), xPos, yPos);
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
            
            int rows = UIRenderingUtil.getHoverRows(i);

            graphics.setColor(Color.darkGray);
            graphics.fillRect(mx, my, 200, rows * 14);
            graphics.setColor(Color.black);
            graphics.drawRect(mx, my, 200, rows * 14);

            ItemRarity rarity = ItemRarity.values()[i.getRarity()];

            graphics.setColor(rarity.getColor());
            
            int tx = mx + 6;
            int ty = my + 6;
            
            graphics.drawString(i.getDisplayname(), tx, ty);
            graphics.drawString(i.getDisplayname(), tx +1, ty);
            
            ty += 14;
            graphics.setColor(Color.white);
            graphics.drawString(ItemType.values()[i.getItemtypeid()].getText(),tx, ty);

            if(i.getItemtypeid() >= 0 && i.getItemtypeid() < 8){
                ty += 14;
                Color usableColor = i.getLevelreq() <= gri.getLevel() ? Color.white : Color.red;
                graphics.setColor(usableColor);
                graphics.drawString("Level requirement: " + i.getLevelreq(), tx, ty);
            }
            
            ty += 14;
            graphics.setColor(Color.yellow);
            graphics.drawString(i.getSellvalue() + " gold", tx, ty);

            ty += 14;
            graphics.setColor(Color.white);
            graphics.drawString(i.isStackable() ? "Stackable" : "Single", tx, ty);
            ty += 14;
            graphics.drawString(i.isTradeable() ? "Tradeable" : "Untradeable", tx, ty);

            
            for(GameItemAttribute attr : i.getAttributes()) {
                ty += 14;
                if(attr.getAttributevalue() >= 0){
                    graphics.setColor(Color.green);
                } else {
                    graphics.setColor(Color.red);
                }
                graphics.drawString(attr.toString(), tx, ty);
            }

            graphics.setColor(Color.lightGray);
            for(String descLine : i.getDescription().split("\n")){
                ty += 14;
                graphics.drawString(descLine, tx, ty);
            }
        }
        
        for(int i = 0; i < gri.getChat().size(); i++){
            Chatline current = gri.getChat().get(i);
            UIRenderingUtil.drawTextEffect(current.getLine(), Color.black, current.getColor(), 10, virtualResolutionHeight - 50 - i * 20, 1, graphics, UIRenderingUtil.smallVerdanaFont);
        }
        
        if(gri.isDead()){
            graphics.setColor(new Color(70, 70, 70));
            graphics.fillRect(200, 200, unScaledGamePlayWidth - 400, 380);
            graphics.setColor(Color.black);
            graphics.drawRect(200, 200, unScaledGamePlayWidth - 400, 380);
            UIRenderingUtil.getSpriteImage(22).draw(180, 200);
            graphics.setFont(UIRenderingUtil.giganticVerdanaFont);
            graphics.setColor(Color.red);
            graphics.drawString("YOU ARE DEAD", 500, 220);
            graphics.setColor(Color.orange);
            
            graphics.setFont(UIRenderingUtil.mediumVerdanaFont);
            graphics.drawString("Oops. You died to ___", 480, 280);
            graphics.drawString("You have to wait for your teammates ", 480, 310);
            graphics.drawString("to advance into the next level.", 480, 340);
            graphics.drawString("If you all die, you will be able to", 480, 370);
            graphics.drawString("teleport to home camp.", 480, 400);
            
            if(gri.isCanRessurect()){
                graphics.setColor(Color.red);
                graphics.drawString("[INTERACT] to teleport to camp", 480, 450);
            }
        }

    }
    
    private void initDraw(Graphics graphics){
        graphics.scale((float) ((double)virtualResolutionWidth/(double)unScaledScreenWidth),(float)((double)virtualResolutionHeight/(double)unScaledScreenHeight));
        graphics.setBackground(Color.black);
        if(UIRenderingUtil.smallVerdanaFont == null){
            UIRenderingUtil.resetFonts();
        }
        
        
    }
    
    public void drawCharacterCreation(Graphics graphics) {
        initDraw(graphics);
        graphics.setColor(FONT_BASE_COLOR);
        graphics.drawString("CHARACTER CREATION SCREEN",  UIRenderingUtil.calculateTextAllignCenterX(graphics,"CHARACTER CREATION SCREEN" ), 100);
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
