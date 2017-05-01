/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.rendering.gui;

/**
 * Created Apr 29, 2017
 * @author arska
 */
public class LootBagClickEvent {
    private boolean rightclick;
    private int index;

    public LootBagClickEvent(boolean rightclick, int index) {
        this.rightclick = rightclick;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean isRightclick() {
        return rightclick;
    }
    
    
}
