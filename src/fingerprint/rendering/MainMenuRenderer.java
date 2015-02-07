package fingerprint.rendering;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.TextField;

import fingerprint.core.GameLauncher;
import fingerprint.states.menu.enums.GameDifficulty;
import fingerprint.states.menu.enums.MainMenuSelection;

public class MainMenuRenderer {
    
    private static int MAINMENU_MENUITEM_PADDING = 50;
    private static int MAINMENU_MENUITEM_STARTDRAWING_Y = 300;
    
    
    public MainMenuRenderer() {
        // TODO Auto-generated constructor stub
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
    public void drawWorldCreation(Graphics graphics,GameContainer container,GameDifficulty selectedDifficulty,int selectedRow,int selectedColumn,TextField nameField,boolean drawBadFileName){
        graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        String titleText = "Creating a new world";
        graphics.drawString(titleText, RenderingManager.calculateTextAllignCenterX(graphics, titleText), 100);
        
        String chooseDifficultyText = "Select Difficulty:";
        graphics.drawString(chooseDifficultyText, 200, 400);
        GameDifficulty[] difficulties = GameDifficulty.values();
        graphics.setColor(Color.orange);
        int counter = 0;
        int offsetX = 0; 
        for(GameDifficulty difficulty : difficulties){
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
        String worldName = "Give the savefile a name:";
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
            graphics.drawString("Bad filename!", 530, 530);
        }
        graphics.setColor(RenderingManager.FONT_BASE_COLOR);
        String generateString = "Generate world";
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
