/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.events.gui;

/**
 * Created Apr 29, 2017
 * @author arska
 */
public class LootItemEvent {
    private int index;
    private String lootbagHash;
    private int itemID;
    private int itemQuantity;

    public LootItemEvent(int index, String lootbagHash, int itemID, int itemQuantity) {
        this.index = index;
        this.lootbagHash = lootbagHash;
        this.itemID = itemID;
        this.itemQuantity = itemQuantity;
    }

    public int getIndex() {
        return index;
    }

    public int getItemID() {
        return itemID;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public String getLootbagHash() {
        return lootbagHash;
    }
    
    
}
