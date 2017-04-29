package fingerprint.rendering;



import fingerprint.inout.FileUtil;
import org.newdawn.slick.Color;

import com.google.common.eventbus.Subscribe;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.opengl.SlickCallable;

public class UIManager {
    
    private String nextConsoleText = "Testing a longer console message";
    private Color textColor = Color.black;
    private SpriteSheet items;
    
    
    public UIManager() {

    }
    
    public String getNextConsoleText() {
        return nextConsoleText;
    }
    
    @Subscribe
    public void listenDisplayConsoleMessageEvent(DisplayConsoleMessageEvent event){
        nextConsoleText = event.getText();
        textColor = event.getColor();


    }

    public Image getItemImage(int imageid) {

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

}
