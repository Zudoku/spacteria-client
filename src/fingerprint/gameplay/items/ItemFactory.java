package fingerprint.gameplay.items;

public class ItemFactory {
    
    public static final int HAND = 1;
    public static final int SEED_WHEAT = 2;
    
    public static final int WOOD = 20;
    public static final int WOOD_HACKED = 21;
    public static final int WOOD_PLANK = 22;
    public static final int WOOD_STICK = 23;
    public static final int WOODEN_BOWL = 24;
    public static final int WOODEN_BOWL_WATER = 25;
    
    
    
    public static final String FILEPATH_HAND = "Hand";
    public static final String FILEPATH_SEED_WHEAT = "WheatSeed";
    public static final String FILEPATH_WOOD = "ChunkOfWood";
    public static final String FILEPATH_WOOD_HACKED = "HackedWood";
    public static final String FILEPATH_WOOD_PLANK = "WoodenPlank";
    public static final String FILEPATH_WOOD_STICK = "WoodenStick";
    public static final String FILEPATH_WOODEN_BOWL = "WoodenBowl";
    public static final String FILEPATH_WOODEN_BOWL_WATER = "WoodenBowlWater";
    
    
    
    public static Item getItem(int ID){
        
        Item item = null;
        switch(ID){
        case HAND:
            
            item = new Item(FILEPATH_HAND,HAND);
            
            return item;
            
        case SEED_WHEAT:
            
            item = new Item(FILEPATH_SEED_WHEAT,SEED_WHEAT);
            
            return item;
            
        case WOOD:
            
            item = new Item(FILEPATH_WOOD,WOOD);
            
            return item;
            
        case WOOD_HACKED:
            
            item = new Item(FILEPATH_WOOD_HACKED,WOOD_HACKED);
            
            return item;
            
        case WOOD_PLANK:
            
            item = new Item(FILEPATH_WOOD_PLANK,WOOD_PLANK);
            
            return item;
            
        case WOOD_STICK:
            
            item = new Item(FILEPATH_WOOD_STICK,WOOD_STICK);
            
            return item;
            
        case WOODEN_BOWL:
            
            item = new Item(FILEPATH_WOODEN_BOWL,WOODEN_BOWL);
            
            return item;
            
        case WOODEN_BOWL_WATER:
            
            item = new Item(FILEPATH_WOODEN_BOWL_WATER,WOODEN_BOWL_WATER);
            
            return item;
            
            default:
                return null;
        }
    }

}
