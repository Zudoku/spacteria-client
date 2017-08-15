package fingerprint.rendering.staterenderers;

import fingerprint.rendering.manager.RenderingManager;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;

import fingerprint.core.GameLauncher;
import fingerprint.mainmenus.GenericGridController;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.states.menu.enums.CharacterClass;
import fingerprint.states.menu.enums.MainMenuSelection;
import java.util.List;

public class MainMenuRenderer {
    
    private static int MAINMENU_MENUITEM_PADDING = 50;
    private static int MAINMENU_MENUITEM_STARTDRAWING_Y = 300;
    
    
    public MainMenuRenderer() {
        // TODO Auto-generated constructor stub
    }
    
    public void drawServerList(Graphics graphics, List<RoomDescription> rooms, int selection){
        graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        String titleText = "Select a room to join or make your own";
        String roomsText = rooms.size() + " Rooms available";
        graphics.drawString(titleText, RenderingManager.calculateTextAllignCenterX(graphics, titleText), 100);
        graphics.drawString(roomsText, RenderingManager.calculateTextAllignCenterX(graphics, roomsText), 150);
        for(int i = 0; i < rooms.size() + 1; i++){
            RoomDescription drawed = null;
            String roomString = "";
            if(i == 0){
                roomString = "Make a new room";
            } else {
                drawed =  rooms.get(i - 1);
                roomString = drawed.getName() +" [" + drawed.getDifficulty() + "] (" + drawed.getPlayers() + ")";
            }
            if(selection == i){
                graphics.setColor(Color.pink);
            }
            
            graphics.drawString(roomString,RenderingManager.calculateTextAllignCenterX(graphics, roomString), MAINMENU_MENUITEM_STARTDRAWING_Y+MAINMENU_MENUITEM_PADDING*i);
            if(selection == i){
                graphics.setColor(Color.cyan);
                int selX= RenderingManager.calculateTextAllignCenterX(graphics, roomString) -17;
                int selY = MAINMENU_MENUITEM_STARTDRAWING_Y+MAINMENU_MENUITEM_PADDING*i-4;
                int width = graphics.getFont().getWidth(roomString)+30;
                int height = 25;
                graphics.drawRect(selX,selY, width, height);
            }
            graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        }
        
        
    }
    

    public void drawMainMenu(Graphics graphics, MainMenuSelection selection) {
        //PLACEHOLDER MENU
        graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        String titleText = GameLauncher.PROGRAM_NAME;
        graphics.drawString(titleText, RenderingManager.calculateTextAllignCenterX(graphics, titleText), 100);
        //draw a menu item
        MainMenuSelection[] enumValues = MainMenuSelection.values();
        for(int i = 0; i < enumValues.length; i++){
            MainMenuSelection drawed =  enumValues[i];
            if(selection == drawed){
                graphics.setColor(Color.pink);
            }
            graphics.drawString(drawed.toString(),RenderingManager.calculateTextAllignCenterX(graphics, drawed.toString()), MAINMENU_MENUITEM_STARTDRAWING_Y+MAINMENU_MENUITEM_PADDING*i);
            if(selection == drawed){
                graphics.setColor(Color.cyan);
                int selX= RenderingManager.calculateTextAllignCenterX(graphics, drawed.toString()) -17;
                int selY = MAINMENU_MENUITEM_STARTDRAWING_Y+MAINMENU_MENUITEM_PADDING*i-4;
                int width = graphics.getFont().getWidth(drawed.toString())+30;
                int height = 25;
                graphics.drawRect(selX,selY, width, height);
            }
            graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        }
        
    }
    
    public void drawLoginToGame(Graphics graphics,GameContainer container, TextField usernameField, TextField passwordField, GenericGridController controller){
        try {
            Image logo = new Image("resources/UI/spacterialogo.png");
            logo.drawCentered(RenderingManager.unScaledScreenWidth / 2, 250);
        } catch (SlickException e) {
            e.printStackTrace();
        }

        graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        String usernamelabelText = "Username";
        if(controller.getSelectedRow() == 0) {
            graphics.setColor(Color.white);   
        } else {
            graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        }
        graphics.drawString(usernamelabelText, 200, 500);
        
        
        String passwordlabelText = "Password";
        if(controller.getSelectedRow() == 1) {
            graphics.setColor(Color.white);
        } else {
            graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        }
        graphics.drawString(passwordlabelText, 200, 600);
        
        graphics.setColor(Color.lightGray);
        usernameField.render(container, graphics);
        passwordField.render(container, graphics);
        
        String loginLabelText = "Login";
        if(controller.getSelectedRow() == 2) {
            graphics.setColor(Color.white);
        } else {
            graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        }
        graphics.drawString(loginLabelText, 200, 800);
        
        
    }
    
    public void drawWorldCreation(Graphics graphics,GameContainer container,CharacterClass selectedDifficulty,int selectedRow,int selectedColumn,TextField nameField,boolean drawBadFileName){
        graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        String titleText = "Creating a new Character";
        graphics.drawString(titleText, RenderingManager.calculateTextAllignCenterX(graphics, titleText), 100);
        
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
}
