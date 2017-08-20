package fingerprint.rendering.staterenderers;

import fingerprint.rendering.manager.RenderingManager;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;

import fingerprint.core.GameLauncher;
import fingerprint.mainmenus.CharacterInfoContainer;
import fingerprint.mainmenus.GenericGridController;
import fingerprint.mainmenus.serverlist.RoomDescription;
import static fingerprint.rendering.manager.RenderingManager.FONT_BASE_COLOR;
import fingerprint.rendering.manager.UIRenderingUtil;
import fingerprint.rendering.util.ConnectionRenderingInformation;
import fingerprint.states.menu.enums.CharacterClass;
import fingerprint.states.menu.enums.MainMenuSelection;
import io.socket.client.IO;
import java.util.Arrays;
import java.util.List;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class MainMenuRenderer {
    
    private static int MAINMENU_MENUITEM_PADDING = 50;
    private static int MAINMENU_MENUITEM_STARTDRAWING_Y = 300;
    
    
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
        
    }
    
    public void drawLoginToGame(Graphics graphics,GameContainer container, TextField usernameField,
            TextField passwordField, GenericGridController controller, ConnectionRenderingInformation connectionInformation){
        try {
            Image logo = new Image("resources/UI/spacterialogo.png");
            logo.drawCentered(RenderingManager.unScaledScreenWidth / 2, 250);
        } catch (SlickException e) {
            e.printStackTrace();
        }

        Color usernameTextColor = null;
        String usernamelabelText = "Username";
        if(controller.getSelectedRow() == 0) {
            usernameTextColor = Color.lightGray;   
        } else {
            usernameTextColor = Color.darkGray;
        }
        //graphics.drawString(usernamelabelText, 200, 500);
        UIRenderingUtil.drawTextEffect(usernamelabelText, Color.lightGray, Color.black, 200, 496, 2, graphics, UIRenderingUtil.mediumVerdanaFont);
        
        Color passwordTextColor = null;
        String passwordlabelText = "Password";
        if(controller.getSelectedRow() == 1) {
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
        
        graphics.drawString(connectionInformation.getLastMessage(), 600, 580);
        
        graphics.setFont(UIRenderingUtil.smallVerdanaFont);
        graphics.setColor(Color.white);
        
        graphics.drawString("Socket ID: " + connectionInformation.getSocket().id(), 5, RenderingManager.unScaledScreenHeight - 60);
        graphics.drawString("Server status: " + connectionInformation.getStatus(), 5, RenderingManager.unScaledScreenHeight - 40);
        graphics.drawString("Server: " + connectionInformation.getHost(), 5, RenderingManager.unScaledScreenHeight - 20);
        
        graphics.drawString("Version: " + GameLauncher.GAME_VERSION, RenderingManager.unScaledScreenWidth - 80, 5);
        
        //Draw changelog
        
        graphics.drawRect(RenderingManager.unScaledScreenWidth - 284, 500, 270, 180);
        graphics.drawString("CHANGELOG (VERSION 0000 - 00.00.2017):", RenderingManager.unScaledScreenWidth - 280, 504);
        graphics.drawRect(RenderingManager.unScaledScreenWidth - 284, 500, 270, 22);
        
        graphics.drawString("- Changed X Y and Z to be F and in the", RenderingManager.unScaledScreenWidth - 280, 504 + 20 * 1);
        graphics.drawString("process B changed.", RenderingManager.unScaledScreenWidth - 280, 504 + 20 * 2);
        graphics.drawString("-", RenderingManager.unScaledScreenWidth - 280, 504 + 20 * 3);
        graphics.drawString("-", RenderingManager.unScaledScreenWidth - 280, 504 + 20 * 4);
        graphics.drawString("-", RenderingManager.unScaledScreenWidth - 280, 504 + 20 * 5);
        graphics.drawString("-", RenderingManager.unScaledScreenWidth - 280, 504 + 20 * 6);
        graphics.drawString("-", RenderingManager.unScaledScreenWidth - 280, 504 + 20 * 7);
        graphics.drawString("-", RenderingManager.unScaledScreenWidth - 280, 504 + 20 * 8);
        
        
        
        graphics.drawString("Community Discord: discord.gg/zcYCrcY", RenderingManager.unScaledScreenWidth - 280, RenderingManager.unScaledScreenHeight - 34);
        graphics.drawString("BitBucket: bitbucket.org/Arap/project-fingerprint", RenderingManager.unScaledScreenWidth - 280, RenderingManager.unScaledScreenHeight - 20);
        
        
        
    }
    
    public void drawWorldCreation(Graphics graphics,GameContainer container,CharacterClass selectedDifficulty,int selectedRow,int selectedColumn,TextField nameField,boolean drawBadFileName){
        graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        String titleText = "Creating a new Character";
        graphics.drawString(titleText, UIRenderingUtil.calculateTextAllignCenterX(graphics, titleText), 100);
        
        String chooseDifficultyText = "Select Class:";
        graphics.drawString(chooseDifficultyText, 200, 400);
        CharacterClass[] difficulties = CharacterClass.values();
        graphics.setColor(Color.orange);
        int counter = 0;
        int offsetX = 0; 
        for(CharacterClass difficulty : difficulties){
            if(difficulty == selectedDifficulty){
                graphics.setColor(Color.white);
            }else{
                graphics.setColor(Color.orange);
            }
            graphics.drawString(difficulty.toString(), 200+offsetX, 430);
            if(selectedRow == 0){
                if(counter == selectedColumn){
                    graphics.setColor(Color.cyan);
                    int selX= 200+offsetX -7;
                    int selY = 426;
                    int width = graphics.getFont().getWidth(difficulty.toString())+10;
                    int height = 25;
                    graphics.drawRect(selX,selY, width, height);
                }
            }
            offsetX += graphics.getFont().getWidth(difficulty.toString()) + 10;
            counter++;
        }
        graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        String worldName = "Give the Character a name:";
        graphics.drawString(worldName, 200, 490);
        graphics.setColor(Color.lightGray);
        nameField.render(container, graphics);
        if(selectedRow == 1){
            graphics.setColor(Color.cyan);
            int height = 44;
            int selX= 200 -7;
            int selY = 514;
            int width = 300+10;
            graphics.drawRect(selX, selY, width, height);
        }
        if(drawBadFileName){
            graphics.setColor(Color.pink);
            graphics.drawString("Bad name!", 530, 530);
        }
        graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        String generateString = "Create Character";
        graphics.drawString(generateString, 200, 590);
        String backString = "Back";
        graphics.drawString(backString, 200 + 30 + graphics.getFont().getWidth(generateString), 590);
        if(selectedRow == 2){
            int height = 25;
            graphics.setColor(Color.cyan);
            if(selectedColumn == 0){
                int selX= 200 -7;
                int selY = 586;
                int width = graphics.getFont().getWidth(generateString)+10;
                graphics.drawRect(selX, selY, width, height);
            }else if (selectedColumn == 1){
                int selX= 200+ 30 + graphics.getFont().getWidth(generateString) -7;
                int selY = 586;
                int width = graphics.getFont().getWidth(backString)+10;
                graphics.drawRect(selX, selY, width, height);
            }
        }
        
    }

    public void drawCharSelection(Graphics graphics, CharacterInfoContainer gwic, List<CharacterInfoContainer> availableChars) {
        
        UIRenderingUtil.drawTextEffect("Characters:", Color.lightGray, Color.black, 14, 60, 2, graphics, UIRenderingUtil.largeVerdanaFont);
        
        graphics.setColor(Color.white);
        
        
        
        //Draw select char box
        graphics.drawRect(10f, 100f, 200, 50);
        
        int charIndex = 0;
        for(CharacterInfoContainer infoContainer : availableChars) {
            if(infoContainer == gwic) {
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
                String descriptionString = "Level: " + infoContainer.getPlayerData().getLevel() + "   " + infoContainer.getPlayerData().getStatManager().getCharacterClass();
                graphics.drawString(descriptionString,14f, 126f + (60f * charIndex));
            }
            
            charIndex++;
        }
        
        graphics.setColor(Color.white);
        graphics.setFont(UIRenderingUtil.giganticVerdanaFont);
        
        if(!gwic.isIsCreateNewCharDummy()){
            graphics.drawString(gwic.getPlayerData().getName(), 650, 100);
            graphics.setFont(UIRenderingUtil.smallVerdanaFont);
            graphics.drawRect(440, RenderingManager.unScaledScreenHeight / 2 + 100, 600, 200);
            
            UIRenderingUtil.drawTextEffect(gwic.getPlayerData().getName(), Color.lightGray, Color.black, 450, RenderingManager.unScaledScreenHeight / 2 + 110, 2, graphics, UIRenderingUtil.largeVerdanaFont);
            UIRenderingUtil.drawTextEffect("" + gwic.getPlayerData().getLevel(), Color.yellow, Color.black, 450, RenderingManager.unScaledScreenHeight / 2 + 140, 1, graphics, UIRenderingUtil.mediumVerdanaFont);
            UIRenderingUtil.drawTextEffect("" + gwic.getPlayerData().getStatManager().getCharacterClass(), Color.lightGray, Color.black, 450, RenderingManager.unScaledScreenHeight / 2 + 165, 1, graphics, UIRenderingUtil.mediumVerdanaFont);
        } else {
            graphics.drawString("Create new Character",  560, 100);
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
                    int startX = 400;
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
                    int startX = RenderingManager.unScaledScreenWidth - 200;
                    int startY = RenderingManager.unScaledScreenHeight/2;
                    points = new float[]{startX  , startY -25 ,startX - 50 , startY -50 ,startX-50,startY};
                    
                    
                    
                }
            };
            graphics.fill(triangle);
        }
    }
}
