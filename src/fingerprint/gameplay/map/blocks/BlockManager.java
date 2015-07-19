package fingerprint.gameplay.map.blocks;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.inject.Singleton;

@Singleton
public class BlockManager {
    private static final Logger logger = Logger.getLogger(BlockManager.class.getName());
    private HashMap<Byte,Block> blocks = new HashMap<>();
    
    //META
    public static byte MaskBlockUnwalkable = 0;
    public static byte MaskBlockWalkable = 1;
    
    public static byte UnWalkableBasic = 30;

    
    private static boolean NON_COLLIDEABLE = false;
    private static boolean COLLIDEABLE = true;
    
    public BlockManager() {
        initializeBlocks();
    }
    private void initializeBlocks(){
        blocks.put(MaskBlockUnwalkable, new Block(NON_COLLIDEABLE));
        blocks.put(MaskBlockWalkable, new Block(NON_COLLIDEABLE));
        blocks.put((byte)2, new Block(NON_COLLIDEABLE));
        blocks.put((byte)3, new Block(NON_COLLIDEABLE));
        blocks.put((byte)4, new Block(NON_COLLIDEABLE));
        blocks.put((byte)5, new Block(NON_COLLIDEABLE));
        blocks.put((byte)6, new Block(NON_COLLIDEABLE));
        blocks.put((byte)7, new Block(NON_COLLIDEABLE));
        blocks.put((byte)8, new Block(NON_COLLIDEABLE));
        blocks.put((byte)9, new Block(COLLIDEABLE));
        blocks.put((byte)10, new Block(NON_COLLIDEABLE));
        blocks.put(UnWalkableBasic,new Block(COLLIDEABLE));
    }
    public Block getBlock(int ID){
        if(blocks.containsKey(ID)){
            return blocks.get(ID);
        }else{
            return null;
        }
    }
}
