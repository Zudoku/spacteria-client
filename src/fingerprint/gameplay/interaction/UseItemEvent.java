package fingerprint.gameplay.interaction;

import fingerprint.gameplay.items.Item;

public class UseItemEvent {
    
    private Item onHand;
    private int xTileCoord;
    private int yTileCoord;
    
    public UseItemEvent(Item onHand, int xTileCoord, int yTileCoord) {
        this.onHand = onHand;
        this.xTileCoord = xTileCoord;
        this.yTileCoord = yTileCoord;  
    }
    
    public Item getOnHand() {
        return onHand;
    }
    public int getxTileCoord() {
        return xTileCoord;
    }
    public int getyTileCoord() {
        return yTileCoord;
    }
    

}
