package fingerprint.rendering.staterenderers;

import fingerprint.rendering.manager.RenderingManager;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;

import fingerprint.core.GameLauncher;
import fingerprint.mainmenus.CharacterInfoContainer;
import fingerprint.mainmenus.GenericGridController;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.rendering.manager.UIRenderingUtil;
import fingerprint.rendering.util.ConnectionRenderingInformation;
import fingerprint.states.menu.enums.MainMenuSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.util.Pair;

public class MainMenuRenderer {
    
    private static int MAINMENU_MENUITEM_PADDING = 50;
    private static int MAINMENU_MENUITEM_STARTDRAWING_Y = 300;
    
    private long lastMainMenuItemEmitterRefresh = 0;
    private long lastMainMenuSpriteEmitterRefresh = 0;
    private List<Pair<Image, Integer>> lastMainMenuItemImages = new ArrayList<>();
    private Image lastMainMenuSpriteImage;
    
    
    public MainMenuRenderer() {
        // TODO Auto-generated constructor stub
    }
    
    public void drawServerList(Graphics graphics, List<RoomDescription> rooms, int selection){
        String titleText = "Select a room to join or make your own";
        String roomsText = rooms.size() + " Rooms available";
        
        graphics.setColor(Color.white);
        graphics.setFont(UIRenderingUtil.largeVerdanaFont);
        
        graphics.drawString(titleText, UIRenderingUtil.calculateTextAllignCenterX(graphics, titleText), 100);
        graphics.drawString(roomsText, UIRenderingUtil.calculateTextAllignCenterX(graphics, roomsText), 150);
        
        graphics.setColor(Color.white);
        
        for(int i = 0; i < rooms.size() + 1; i++){
            RoomDescription drawed = null;
            String roomString = "";
            if(i == 0){
                roomString = "Make a new room";
            } else {
                drawed =  rooms.get(i - 1);
                roomString = drawed.getName() +"  -  {" + drawed.getDifficulty() + "}  -  " + Arrays.toString(drawed.getPlayers().toArray()) + "";
            }
            if(selection == i){
                graphics.setColor(Color.pink);
            }
            
            graphics.drawString(roomString, UIRenderingUtil.calculateTextAllignCenterX(graphics, roomString), MAINMENU_MENUITEM_STARTDRAWING_Y+MAINMENU_MENUITEM_PADDING*i);
            if(selection == i){
                graphics.setColor(Color.cyan);
                int selX= UIRenderingUtil.calculateTextAllignCenterX(graphics, roomString) -17;
                int selY = MAINMENU_MENUITEM_STARTDRAWING_Y+MAINMENU_MENUITEM_PADDING*i-4;
                int width = graphics.getFont().getWidth(roomString)+30;
                int height = 40;
                graphics.drawRect(selX,selY, width, height);
            }
            graphics.setColor(Color.white);
        }
        
        
    }
    

    public void drawMainMenu(Graphics graphics, MainMenuSelection selection) {
        //PLACEHOLDER MENU
        graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        
        //draw a menu item
        MainMenuSelection[] enumValues = MainMenuSelection.values();
        for(int i = 0; i < enumValues.length; i++){
            MainMenuSelection drawed =  enumValues[i];
            if(selection == drawed){
                graphics.setColor(Color.pink);
            }
            UIRenderingUtil.drawTextEffect(drawed.toString(), Color.lightGray, Color.black, 20, MAINMENU_MENUITEM_STARTDRAWING_Y+MAINMENU_MENUITEM_PADDING*i, 2, graphics, UIRenderingUtil.giganticVerdanaFont);
            //graphics.drawString(drawed.toString(),UIRenderingUtil.calculateTextAllignCenterX(graphics, drawed.toString()), MAINMENU_MENUITEM_STARTDRAWING_Y+MAINMENU_MENUITEM_PADDING*i);
            if(selection == drawed){
                graphics.setColor(Color.cyan);
                int selX= 5;
                int selY = MAINMENU_MENUITEM_STARTDRAWING_Y+MAINMENU_MENUITEM_PADDING*i-4;
                int width = graphics.getFont().getWidth(drawed.toString())+30;
                int height = 54;
                graphics.drawRect(selX,selY, width, height);
            }
            graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        }
        graphics.setColor(Color.black);
        
        if(lastMainMenuItemEmitterRefresh + 1000 < System.currentTimeMillis()){
            Random r = new Random();
            lastMainMenuItemEmitterRefresh = System.currentTimeMillis();
            if(lastMainMenuItemImages.size() > 10){
                lastMainMenuItemImages.remove(lastMainMenuItemImages.size() - 1);
            }
            lastMainMenuItemImages.add(0, new Pair<>(UIRenderingUtil.getItemImage(r.nextInt(50) + 1),r.nextInt(3) - 1));
        }
        
        if(lastMainMenuSpriteEmitterRefresh + 2500 < System.currentTimeMillis()){
            Random r = new Random();
            lastMainMenuSpriteEmitterRefresh = System.currentTimeMillis();
            lastMainMenuSpriteImage = UIRenderingUtil.getSpriteImage(r.nextInt(23) + 1);
        }
        
        List<Pair<Image,Integer>> drawed = lastMainMenuItemImages;
        for(int i = 0; i < drawed.size(); i++){
            Image currentImage = drawed.get(i).getKey();
            currentImage.draw(600 - (drawed.get(i).getValue() * 80), - 100 + ((System.currentTimeMillis() - lastMainMenuItemEmitterRefresh) / 10) + (i * 100));
        }
        lastMainMenuSpriteImage.getScaledCopy(256, 256).drawCentered(1040, 400);
        
    }
    
    public void drawLoginToGame(Graphics graphics,GameContainer container, TextField usernameField,
            TextField passwordField, ConnectionRenderingInformation connectionInformation){
        try {
            Image logo = new Image("resources/UI/spacterialogo.png");
            logo.drawCentered(RenderingManager.unScaledScreenWidth / 2, 250);
        } catch (SlickException e) {
            e.printStackTrace();
        }

        Color usernameTextColor = null;
        String usernamelabelText = "Username";
        if(usernameField.hasFocus()) {
            usernameTextColor = Color.lightGray;   
        } else {
            usernameTextColor = Color.darkGray;
        }

        UIRenderingUtil.drawTextEffect(usernamelabelText, Color.lightGray, Color.black, 200, 496, 2, graphics, UIRenderingUtil.mediumVerdanaFont);
        
        Color passwordTextColor = null;
        String passwordlabelText = "Password";
        if(passwordField.hasFocus()) {
            passwordTextColor = Color.lightGray;
        } else {
            passwordTextColor = Color.darkGray;
        }
        //graphics.drawString(passwordlabelText, 200, 600);
        UIRenderingUtil.drawTextEffect(passwordlabelText, Color.lightGray, Color.black, 200, 596, 2, graphics, UIRenderingUtil.mediumVerdanaFont);
        
        graphics.setColor(usernameTextColor);
        usernameField.render(container, graphics);
        graphics.setColor(passwordTextColor);
        passwordField.render(container, graphics);
        
        graphics.setFont(UIRenderingUtil.mediumVerdanaFont);
        graphics.setColor(Color.white);
        
        graphics.drawString(connectionInformation.getLastMessage(), 200, 700);
        
        graphics.setFont(UIRenderingUtil.smallVerdanaFont);
        graphics.setColor(Color.white);
        
        graphics.drawString("Environtment: " + connectionInformation.getEnvironment().toString(), 5, RenderingManager.unScaledScreenHeight - 80);
        graphics.drawString("Socket ID: " + connectionInformation.getSocket().id(), 5, RenderingManager.unScaledScreenHeight - 60);
        graphics.drawString("Server status: " + connectionInformation.getStatus(), 5, RenderingManager.unScaledScreenHeight - 40);
        graphics.drawString("Server: " + connectionInformation.getHost(), 5, RenderingManager.unScaledScreenHeight - 20);
        
        graphics.drawString("Version: " + GameLauncher.GAME_VERSION, RenderingManager.unScaledScreenWidth - 80, 5);
        
        //Draw changelog
        String[] lines = connectionInformation.getChangelog().split(";");
        graphics.drawRect(RenderingManager.unScaledScreenWidth - 284, 500, 270, 22 + (lines.length * 20));
        graphics.drawString("CHANGELOG (VERSION " + connectionInformation.getVersion() + "):", RenderingManager.unScaledScreenWidth - 280, 504);
        
        
        graphics.drawRect(RenderingManager.unScaledScreenWidth - 284, 500, 270, 22);
        
        
        for(int index = 0; index < lines.length; index++){
            graphics.drawString(lines[index], RenderingManager.unScaledScreenWidth - 280, 524 + 20 * index);
        }
        
        graphics.drawString("Community Discord: discord.gg/zcYCrcY", RenderingManager.unScaledScreenWidth - 280, RenderingManager.unScaledScreenHeight - 34);
        graphics.drawString("BitBucket: bitbucket.org/Arap/project-fingerprint", RenderingManager.unScaledScreenWidth - 280, RenderingManager.unScaledScreenHeight - 20);
    }
    
    public void drawCharacterCreation(Graphics graphics,GameContainer container,int phase,TextField nameField, String naggingText){
        graphics.setColor(Color.white);
        String titleText = "Creating a new Character";
        graphics.setFont(UIRenderingUtil.giganticVerdanaFont);
        graphics.drawString(titleText, UIRenderingUtil.calculateTextAllignCenterX(graphics, titleText), 100);
        
        graphics.setFont(UIRenderingUtil.mediumVerdanaFont);
        graphics.setColor(Color.lightGray);
        nameField.render(container, graphics);
        
        if(phase == 1){
            graphics.setColor(Color.white);
            String worldName = "Give the Character a name:";
            graphics.drawString(worldName, 200, 390);
            
            graphics.setColor(Color.cyan);
            int height = 44;
            int selX= 200 -7;
            int selY = 414;
            int width = 300+10;
            graphics.drawRect(selX, selY, width, height);
        }
        if(!naggingText.isEmpty()){
            graphics.setColor(Color.pink);
            graphics.drawString(naggingText, 530, 430);
        }
        graphics.setColor(Color.white);
        
        if(phase == 2){
            String generateString = "Generate Character";
            graphics.setFont(UIRenderingUtil.largeVerdanaFont);
            graphics.drawString(generateString, 200, 490);
            int height = 36;
            graphics.setColor(Color.cyan);
            int selX= 200 -7;
            int selY = 486;
            int width = graphics.getFont().getWidth(generateString)+10;
            graphics.drawRect(selX, selY, width, height);
        }
    }

    public void drawCharSelection(Graphics graphics, CharacterInfoContainer gwic, List<CharacterInfoContainer> availableChars, int deleteCounter) {
        
        UIRenderingUtil.drawTextEffect("Characters:", Color.lightGray, Color.black, 14, 60, 2, graphics, UIRenderingUtil.largeVerdanaFont);
        
        graphics.setColor(Color.white);
        
        
        
        //Draw select char box
        graphics.drawRect(10f, 100f, 200, 50);
        
        int charIndex = 0;
        for(CharacterInfoContainer infoContainer : availableChars) {
            if(infoContainer == gwic) {
                graphics.setColor(Color.darkGray);
                graphics.fillRect(10f, 100f + (60f * charIndex), 200, 50);
                graphics.setColor(Color.cyan);
            } else {
                graphics.setColor(Color.white);
            }
            
            graphics.drawRect(10f, 100f + (60f * charIndex), 200, 50);
            graphics.setColor(Color.white);
            graphics.setFont(UIRenderingUtil.smallVerdanaFont);
            if(infoContainer.isIsCreateNewCharDummy()){
                graphics.drawString("Create new character", 14f, 120f + (60f * charIndex));
            } else {
                graphics.drawString(infoContainer.getPlayerData().getName(),14f, 104f + (60f * charIndex));
                //a hack to get around things not loaded yet
                String descriptionString = "Level: " + infoContainer.getPlayerData().getLevel();
                graphics.drawString(descriptionString,14f, 126f + (60f * charIndex));
                
            }
            
            charIndex++;
        }
        
        graphics.setColor(Color.white);
        graphics.setFont(UIRenderingUtil.giganticVerdanaFont);
        
        if(!gwic.isIsCreateNewCharDummy()){
            graphics.drawString(gwic.getPlayerData().getName(), 650, 100);
            graphics.setFont(UIRenderingUtil.smallVerdanaFont);
            graphics.setColor(Color.darkGray);
            graphics.fillRect(440, RenderingManager.unScaledScreenHeight / 2 + 100, 600, 200);
            if(deleteCounter > 0) {
                graphics.setColor(Color.red);
                graphics.fillRect(440, RenderingManager.unScaledScreenHeight / 2 + 100, (int)(((double)deleteCounter / 2500d) * 600d), 200);
            }
            graphics.setColor(Color.white);
            graphics.drawRect(440, RenderingManager.unScaledScreenHeight / 2 + 100, 600, 200);
            
            UIRenderingUtil.drawTextEffect(gwic.getPlayerData().getName(), Color.lightGray, Color.black, 450, RenderingManager.unScaledScreenHeight / 2 + 110, 2, graphics, UIRenderingUtil.largeVerdanaFont);
            UIRenderingUtil.drawTextEffect("" + gwic.getPlayerData().getLevel(), Color.darkGray, Color.yellow, 450, RenderingManager.unScaledScreenHeight / 2 + 140, 1, graphics, UIRenderingUtil.mediumVerdanaFont);
            UIRenderingUtil.drawTextEffect("Created: " + gwic.getPlayerData().getCreated(), Color.lightGray, Color.black, 450, RenderingManager.unScaledScreenHeight / 2 + 185, 1, graphics, UIRenderingUtil.mediumVerdanaFont);
            graphics.setColor(Color.black);
            graphics.setFont(UIRenderingUtil.mediumVerdanaFont);
            graphics.drawString("DELETE character: hold TAB + F2", 450, RenderingManager.unScaledScreenHeight / 2 + 220);
        } else {
            graphics.drawString("Create new Character",  560, 100);
        }
    }
}
    