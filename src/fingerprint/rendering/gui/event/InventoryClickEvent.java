/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.rendering.gui.event;

/**
 * Created Apr 29, 2017
 * @author arska
 */
public class InventoryClickEvent {
    private boolean rightclick;
    private int index;
    private int mousebutton;

    public InventoryClickEvent(boolean rightclick, int index, int mousebutton) {
        this.rightclick = rightclick;
        this.index = index;
        this.mousebutton = mousebutton;
    }
    
    public boolean isRightclick() {
        return rightclick;
    }

    public int getIndex() {
        return index;
    }

    public int getMousebutton() {
        return mousebutton;
    }
    
    
    
    

}
