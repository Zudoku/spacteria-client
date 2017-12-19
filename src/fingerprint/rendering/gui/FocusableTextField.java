/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.rendering.gui;

import org.newdawn.slick.Font;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

/**
 * Created Dec 19, 2017
 * @author arska
 */
public class FocusableTextField extends TextField {

    public FocusableTextField(GUIContext container, Font font, int x, int y, int width, int height) {
        super(container, font, x, y, width, height);
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickcount) {
        if(inside(x, y)){
            doFocus();
        } else {
            setFocus(false);
            setConsumeEvents(false);
        }
        super.mouseClicked(button, x, y, clickcount);
        
    }
    
    private boolean inside(int mx, int my) {
        if (mx < x || mx > x + getWidth() || my < y || my > y + getHeight()) {
            return false;
        }
        return true;
    }
    
    public void doFocus(){
        setFocus(true);
        setConsumeEvents(true);
        setCursorPos(getText().length());
    }

    public void dopaste(String pasted){
        super.doPaste(pasted);
    }
}
