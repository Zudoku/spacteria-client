package fingerprint.gameplay.interaction;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import fingerprint.gameplay.items.Item;
import fingerprint.gameplay.items.ItemFactory;
import fingerprint.gameplay.map.blocks.BlockManager;
import fingerprint.gameplay.map.blocks.BlockRendering;
import fingerprint.gameplay.map.gameworld.GameWorld;
import fingerprint.gameplay.objects.player.PlayerContainer;
import fingerprint.inout.TileFileHandler;

public class UseInteractionManager {
    private static final Logger logger = Logger.getLogger(PlayerContainer.class.getName());
    
    private GameWorld world;
    private TileFileHandler tileFileHandler;
    private Random random = new Random();
    
    public UseInteractionManager(GameWorld world, TileFileHandler tileFileHandler) {
        this.world = world;
        this.tileFileHandler = tileFileHandler;
    }
    
    
    public void use(Item item, byte functionalTile, int x, int y){
        switch(functionalTile){
            case BlockManager.Bush:

                Item itemToAdd = ItemFactory.getItem(ItemFactory.SEED_WHEAT);
                world.getPlayer().getInventory().addItem(itemToAdd);
                world.getMap().getData()[x][y] = 0;
                short[][] writable = new short[2][2];
                writable[0][0] = BlockRendering.GRASS;
                writable[1][0] = 0;
                
                tileFileHandler.writeMap(writable, x, y, 1, 1, true);
                
                break;
                
            case BlockManager.Tree:
                
                if(random.nextInt(10) == 0){
                    itemToAdd = ItemFactory.getItem(ItemFactory.WOOD);
                    world.getPlayer().getInventory().addItem(itemToAdd);
                    
                    world.getMap().getData()[x][y] = 0;
                    writable = new short[2][2];
                    writable[0][0] = BlockRendering.GRASS;
                    writable[1][0] = 0;
                    
                    tileFileHandler.writeMap(writable, x, y, 1, 1, true);
                }
                
                break;
                
            case BlockManager.Rock_Smooth:
                useItemOnRock(item,false);
                
                break;
                
            case BlockManager.Rock_Sharp:
                useItemOnRock(item, true);
                break;
                
            case BlockManager.Water:
                
                if(item.getName().equals(ItemFactory.FILEPATH_WOODEN_BOWL)){
                    world.getPlayer().getInventory().useItem();
                    itemToAdd = ItemFactory.getItem(ItemFactory.WOODEN_BOWL_WATER);
                    world.getPlayer().getInventory().addItem(itemToAdd);
                }
                
                break;
        }
    }
    
    private void useItemOnRock(Item item, boolean sharp){
        switch (item.getName()) {
        
        case ItemFactory.FILEPATH_WOOD:
            world.getPlayer().getInventory().useItem();
            if(sharp){
                Item itemToAdd = ItemFactory.getItem(ItemFactory.WOOD_PLANK);
                world.getPlayer().getInventory().addItem(itemToAdd);
            } else {
                Item itemToAdd = ItemFactory.getItem(ItemFactory.WOOD_HACKED);
                world.getPlayer().getInventory().addItem(itemToAdd);
            }
            break;
            
        case ItemFactory.FILEPATH_WOOD_HACKED:
            
            if(!sharp){
                world.getPlayer().getInventory().useItem();
                Item itemToAdd = ItemFactory.getItem(ItemFactory.WOODEN_BOWL);
                world.getPlayer().getInventory().addItem(itemToAdd);
            }
            
            break;

        default:
            break;
        }
    }
    
    
    

}
