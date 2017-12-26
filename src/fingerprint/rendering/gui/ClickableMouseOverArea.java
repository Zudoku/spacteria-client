/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.rendering.gui;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

/**
 * Created Dec 20, 2017
 * @author arska
 */
public class ClickableMouseOverArea  extends MouseOverArea {
    
    private Callable func;
    private String template;
    private int scroll;

    public ClickableMouseOverArea(GUIContext container, Image image, int x, int y, int width, int height, String template, Callable func) {
        super(container, image, x, y, width, height, null);
        this.func = func;
        this.template = template;
    }
    
    @Override
    public void mouseClicked(int button, int x, int y, int clickcount) {
        if(inside(x, y)){
            try {
                func.call();
            } catch (Exception ex) {
                Logger.getLogger(ClickableMouseOverArea.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        super.mouseClicked(button, x, y, clickcount);
    }
    
    private boolean inside(int mx, int my) {
        if (mx < getX() || mx > getX() + getWidth() || my < getY() - scroll || my > getY() + getHeight() - scroll) {
            return false;
        }
        return true;
    }


    public void render(GUIContext container, Graphics g, int scroll, Object value) {
        this.scroll = scroll;
        g.drawRect(getX(), getY() - scroll, getWidth(), getHeight());
        g.drawString(template + value.toString(), getX() + 2, getY() + 2 - scroll);
    }
    
    
}
