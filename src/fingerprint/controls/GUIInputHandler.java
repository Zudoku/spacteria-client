/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.controls;

import com.google.common.eventbus.EventBus;
import static fingerprint.rendering.RenderingManager.unScaledGamePlayWidth;
import fingerprint.rendering.gui.ClickMOA;
import fingerprint.rendering.gui.MOAType;
import fingerprint.rendering.map.TilemapRenderer;

/**
 * Created May 13, 2017
 * @author arska
 */
public class GUIInputHandler {
    private EventBus eventBus;
    
    private ClickMOA[] inventoryMOA = new ClickMOA[20];
    private ClickMOA[] equipmentMOA = new ClickMOA[8];
    private ClickMOA[] lootbagMOA = new ClickMOA[8];

    public GUIInputHandler(EventBus eventBus) {
        this.eventBus = eventBus;
        initGameplayMOA();
    }

    
    
    private void initGameplayMOA() {
        // unScaledGamePlayWidth +19 + (ITEM_PADDING * t), 9 * TS + 9
        int ITEM_PADDING = 56;
        int startX = unScaledGamePlayWidth +19;
        int startY = 9 * TilemapRenderer.tileSize + 9;
        // equipment
        for(int index = 0; index < 8; index++){
            int xi = index % 4;
            int yi = (int) Math.floor(index / 4);
            
            
            ClickMOA moa = new ClickMOA(startX + (xi * ITEM_PADDING), startY + (yi * ITEM_PADDING),
                    48, 48, eventBus, MOAType.EQUIPMENT, index);
       
            
            equipmentMOA[index] = moa;
        }
        
        
        startY = 11 * TilemapRenderer.tileSize + 9;
        // inventory
        for(int index = 0; index < 20; index++){
            int xi = index % 4;
            int yi = (int) Math.floor(index / 4);
            
            
            ClickMOA moa = new ClickMOA(startX + (xi * ITEM_PADDING), startY + (yi * ITEM_PADDING),
                    48, 48, eventBus, MOAType.INVENTORY, index);
            
            inventoryMOA[index] = moa;
        }
        
        
        startX = unScaledGamePlayWidth -241;
        startY = 14 * TilemapRenderer.tileSize -11;
        // lootbag
        for(int index = 0; index < 8; index++){
            int xi = index % 4;
            int yi = (int) Math.floor(index / 4);
            
            
            ClickMOA moa = new ClickMOA(startX + (xi * ITEM_PADDING), startY + (yi * ITEM_PADDING),
                    48, 48, eventBus, MOAType.LOOTBAG, index);
            
            lootbagMOA[index] = moa;
        }
    }
    
    public void update(InputManager inputManager) {
        for (int mouseButton = 0; mouseButton < 4; mouseButton++) {
            if (inputManager.getInput().isMousePressed(mouseButton)) {
                
                int mx = inputManager.getInput().getMouseX();
                int my = inputManager.getInput().getMouseY();
                for (int moaindex = 0; moaindex < 20; moaindex++) {

                    if (moaindex < 8) {
                        equipmentMOA[moaindex].mousePressed(mouseButton, mx, my);
                        lootbagMOA[moaindex].mousePressed(mouseButton, mx, my);
                    }
                    inventoryMOA[moaindex].mousePressed(mouseButton, mx, my);

                }
            }
        }

        // Hover
        int mx = inputManager.getInput().getMouseX();
        int my = inputManager.getInput().getMouseY();
        for (int moaindex = 0; moaindex < 20; moaindex++) {

            if (moaindex < 8) {
                equipmentMOA[moaindex].mouseHover(mx, my);
                lootbagMOA[moaindex].mouseHover(mx, my);
            }
            inventoryMOA[moaindex].mouseHover(mx, my);

        }
    }
}
