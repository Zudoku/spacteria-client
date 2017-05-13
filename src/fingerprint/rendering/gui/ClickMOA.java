/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.rendering.gui;

import com.google.common.eventbus.EventBus;
import java.util.Timer;
import java.util.TimerTask;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

/**
 * Created Apr 29, 2017
 * @author arska
 */
public class ClickMOA {
    
    private EventBus eventbus;
    private MOAType moaType;
    private int index;
    private int x;
    private int y;
    private int width;
    private int height;
    

    public ClickMOA(int x, int y, int width, int height, EventBus eventbus, MOAType moaType, int index) {
        this.eventbus = eventbus;
        this.moaType = moaType;
        this.index = index;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    

    public void mousePressed(int button, int mx, int my) {
        if (inside(mx, my)) {
            boolean rightClick = (button == 1);

            switch (moaType) {
                case EQUIPMENT:
                    eventbus.post(new EquipmentClickEvent(rightClick, index));
                    break;

                case INVENTORY:
                    eventbus.post(new InventoryClickEvent(rightClick, index));
                    break;

                case LOOTBAG:
                    eventbus.post(new LootBagClickEvent(rightClick, index));
                    break;
            }
        }

    }
        
        private boolean inside(int mx, int my){
            if( mx < x || mx > x + width || my < y || my > y + height) {
                return false;
            }
            return true;
        }

}