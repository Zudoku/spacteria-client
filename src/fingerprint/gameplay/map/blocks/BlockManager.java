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
        
        blocks.put(ErrorBlock, new Block(NON_COLLIDEABLE));
        blocks.put(VoidBlock, new Block(NON_COLLIDEABLE));
        blocks.put(DebugBlock, new Block(COLLIDEABLE));
        blocks.put(PassageBlock, new Block(NON_COLLIDEABLE));
        
        blocks.put(60, new Block(NON_COLLIDEABLE));
        blocks.put(61, new Block(NON_COLLIDEABLE));
        blocks.put(62, new Block(NON_COLLIDEABLE));
        blocks.put(70, new Block(NON_COLLIDEABLE));
        blocks.put(50, new Block(NON_COLLIDEABLE));
        
        blocks.put(VillageWall, new Block(COLLIDEABLE));
        
    }
    public Block getBlock(int ID){
        if(blocks.containsKey(ID)){
            return blocks.get(ID);
        }else{
            return null;
        }
    }
}
