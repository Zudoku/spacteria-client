package fingerprint.gameplay.map.blocks;

public class Block {
    private final boolean blocking;
    private final int ID;
    private String sprite;
    
    public Block(int ID,boolean blocking) {
        this.ID = ID;
        this.blocking = blocking;
    }
}
