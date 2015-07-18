package fingerprint.gameplay.map.blocks;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.inject.Singleton;

@Singleton
public class BlockManager {
    private static final Logger logger = Logger.getLogger(BlockManager.class.getName());
    private HashMap<Integer,Block> blocks = new HashMap<>();
    
    //CURRENT TILEMAP HOLDS 800 tiles (0-799)
    
    //META
    public static int MaskBlockUnwalkable = 0;
    public static int MaskBlockWalkable = 1;
    public static int ErrorBlock = 20;
    public static int VoidBlock = 25;
    public static int DebugBlock = 30;
    public static int PassageBlock = 50;
    
    //VILLAGE
    public static int VillageWall = 100;
    
    //OUTSKIRTS
    
    private static boolean NON_COLLIDEABLE = false;
    private static boolean COLLIDEABLE = true;
    
    public BlockManager() {
        initializeBlocks();
    }
    private void initializeBlocks(){
        blocks.put(MaskBlockUnwalkable, new Block(NON_COLLIDEABLE));
        blocks.put(MaskBlockWalkable, new Block(NON_COLLIDEABLE));
        blocks.put(2, new Block(NON_COLLIDEABLE));
        blocks.put(3, new Block(NON_COLLIDEABLE));
        blocks.put(4, new Block(NON_COLLIDEABLE));
        blocks.put(5, new Block(NON_COLLIDEABLE));
        blocks.put(6, new Block(NON_COLLIDEABLE));
        blocks.put(7, new Block(NON_COLLIDEABLE));
        blocks.put(8, new Block(NON_COLLIDEABLE));
        blocks.put(9, new Block(COLLIDEABLE));
        blocks.put(10, new Block(NON_COLLIDEABLE));
    }
    public Block getBlock(int ID){
        if(blocks.containsKey(ID)){
            return blocks.get(ID);
        }else{
            return null;
        }
    }
}
