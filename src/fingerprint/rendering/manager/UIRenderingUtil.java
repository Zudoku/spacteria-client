package fingerprint.rendering.manager;



import fingerprint.gameplay.items.GameItem;
import fingerprint.inout.FileUtil;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;

public class UIRenderingUtil {
    
    private static SpriteSheet items;
    
    public static TrueTypeFont smallVerdanaFont;
    public static TrueTypeFont mediumVerdanaFont;
    public static TrueTypeFont largeVerdanaFont;
    public static TrueTypeFont giganticVerdanaFont;
    
    public static Map<Integer,Image> sprites = new HashMap();

    public static final Image getItemImage(int imageid) {

        if( items == null) {
            try {
                items = new SpriteSheet(FileUtil.UI_FILES_PATH + "/items.png", 48, 48);
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }


        int x = (imageid - 1) % 10;
        int y = (int)Math.floor((imageid - 1) / 10);
        return items.getSprite(x, y);
    }
    public static final Image getSpriteImage(int imageid) {
        if(sprites.containsKey(imageid)){
            return sprites.get(imageid);
        } else {
            try {
                Image img = new Image(FileUtil.SPRITE_FILES_PATH + "/" + imageid + ".png");
                sprites.put(imageid, img);
                return img;
            } catch (SlickException ex) {
                Logger.getLogger(UIRenderingUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }
    
    public static final void drawTextEffect(String text, Color color1, Color color2, int x, int y, int sizeDiff, Graphics graphics, TrueTypeFont font){
        graphics.setColor(color1);
        graphics.setFont(font);
        graphics.drawString(text, x, y - sizeDiff);
        graphics.drawString(text, x, y + sizeDiff);
        graphics.drawString(text, x + sizeDiff, y);
        graphics.drawString(text, x - sizeDiff, y);
        graphics.setColor(color2);
        graphics.drawString(text, x, y);
    }
    
    public static final void drawItem(int imageid, float x, float y) {
        getItemImage(imageid).draw(x,y);
    }
    
    public static final void drawEquipmentSymbol(int position,float x, float y) throws SlickException{
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
    
    public static final int calculateTextAllignCenterX(Graphics graphics,String title){
        int titleLenght = graphics.getFont().getWidth(title);
        int place = RenderingManager.unScaledScreenWidth/2 - titleLenght/2;
        
        return place;
    }
    
    public static final void resetFonts(){
        Font font = new Font("Verdana", Font.PLAIN, 10);
        smallVerdanaFont = new TrueTypeFont(font, true);
        font = new Font("Verdana", Font.PLAIN, 18);
        mediumVerdanaFont = new TrueTypeFont(font, true);
        font = new Font("Verdana", Font.PLAIN, 25);
        largeVerdanaFont = new TrueTypeFont(font, true);
        font = new Font("Verdana", Font.PLAIN, 35);
        giganticVerdanaFont = new TrueTypeFont(font, true);
        
    }
    
    public static final int getHoverRows(GameItem item){
        int rows = 6;
        if(item.getItemtypeid() >= 0 && item.getItemtypeid() < 8){
            rows++;
        }
        
        rows += item.getAttributes().length;
        rows += item.getDescription().split("\n").length;
        
        return rows;
    }

}
