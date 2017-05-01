/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.rendering.gui;

import com.google.common.eventbus.EventBus;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

/**
 * Created Apr 29, 2017
 * @author arska
 */
public class DiffClickMOA extends MouseOverArea{
    
    private EventBus eventbus;
    private MOAType moaType;
    private int index;

    public DiffClickMOA(GUIContext container, Image image, int x, int y, int width, int height, EventBus eventbus, MOAType moaType, int index) {
        super(container, image, x, y, width, height);
        this.eventbus = eventbus;
        this.moaType = moaType;
        this.index = index;
    }
    
    @Override
        public void mousePressed(int button, int mx, int my) {
            super.mousePressed(button, mx, my);
            
            if(inside(mx, my)){
                boolean rightClick = (button == 1);
            
            switch(moaType){
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
            if( mx < getX() || mx > getX() + getWidth() || my < getY() || my > getY() + getHeight()) {
                return false;
            }
            return true;
        }

}
