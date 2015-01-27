package fingerprint.gameplay.map.blocks;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.inject.Singleton;

@Singleton
public class BlockManager {
    private static final Logger logger = Logger.getLogger(BlockManager.class.getName());
    private HashMap<Integer,Block> blocks = new HashMap<>();
    
    public static int PassageBlock = 50;
    public static int DebugBlock = 30;
    public static int ErrorBlock = 20;
    public static int MaskBlockUnwalkable = 0;
    public static int MaskBlockWalkable = 1;
    public static int VoidBlock = 25;
    
    public BlockManager() {
        initializeBlocks();
    }
    private void initializeBlocks(){
        boolean NON_COLLIDEABLE = false;
        boolean COLLIDEABLE = true;
        blocks.put(ErrorBlock, new Block(NON_COLLIDEABLE,"resources/empty.png"));
        blocks.put(VoidBlock, new Block(NON_COLLIDEABLE,"resources/count.png"));
        blocks.put(60, new Block(NON_COLLIDEABLE,"resources/60.png"));
        blocks.put(61, new Block(NON_COLLIDEABLE,"resources/61.png"));
        blocks.put(62, new Block(NON_COLLIDEABLE,"resources/62.png"));
        blocks.put(70, new Block(NON_COLLIDEABLE,"resources/70.png"));
        blocks.put(50, new Block(NON_COLLIDEABLE,"resources/50.png"));
        
    }
    public Block getBlock(int ID){
        if(blocks.containsKey(ID)){
            return blocks.get(ID);
        }else{
            return null;
        }
    }
}
