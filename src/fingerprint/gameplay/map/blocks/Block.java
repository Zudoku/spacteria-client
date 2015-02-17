package fingerprint.gameplay.map.blocks;

public class Block {
    private final boolean blocking;
    
    public Block(boolean blocking) {
        this.blocking = blocking;
    }
    public boolean isBlocking() {
        return blocking;
    }
}
