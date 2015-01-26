package fingerprint.gameplay.map.blocks;

public class Block {
    private final boolean blocking;
    private String sprite;
    
    public Block(boolean blocking,String sprite) {
        this.blocking = blocking;
        this.sprite = sprite;
    }
    public boolean isBlocking() {
        return blocking;
    }
    public String getSprite() {
        return sprite;
    }
}
