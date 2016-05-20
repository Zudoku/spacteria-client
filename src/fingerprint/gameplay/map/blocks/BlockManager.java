package fingerprint.gameplay.map.blocks;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.inject.Singleton;

@Singleton
public class BlockManager {
    private static final Logger logger = Logger.getLogger(BlockManager.class.getName());
    private HashMap<Byte,Block> blocks = new HashMap<>();
    
    //META
    public static final byte MaskBlockUnwalkable = 0;
    public static final byte MaskBlockWalkable = 1;
    public static final byte Bush = 5;
    public static final byte Tree = 6;
    public static final byte Rock_Sharp = 7;
    public static final byte Rock_Smooth = 8;
    public static final byte Water = 9;
    public static final byte Dirt = 10;
    public static final byte UnWalkableBasic = 30;

    
    private static final boolean NON_COLLIDEABLE = false;
    private static final boolean COLLIDEABLE = true;
    
    public BlockManager() {
        initializeBlocks();
    }
    private void initializeBlocks(){
        blocks.put(MaskBlockUnwalkable, new Block(NON_COLLIDEABLE));
        blocks.put(MaskBlockWalkable, new Block(NON_COLLIDEABLE));
        blocks.put((byte)2, new Block(NON_COLLIDEABLE));
        blocks.put((byte)3, new Block(NON_COLLIDEABLE));
        blocks.put((byte)4, new Block(NON_COLLIDEABLE));
        blocks.put(Bush, new Block(NON_COLLIDEABLE));
        blocks.put(Tree, new Block(COLLIDEABLE));
        blocks.put(Rock_Sharp, new Block(COLLIDEABLE));
        blocks.put(Rock_Smooth, new Block(COLLIDEABLE));
        blocks.put(Water, new Block(COLLIDEABLE));
        blocks.put(Dirt, new Block(NON_COLLIDEABLE));
        blocks.put(UnWalkableBasic,new Block(COLLIDEABLE));
    }
    public Block getBlock(byte ID){
        if(blocks.containsKey(ID)){
            return blocks.get(ID);
        }else{
            return null;
        }
    }
}
