package fingerprint.core;

import java.util.logging.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import fingerprint.gameplay.map.blocks.BlockManager;


public class GameModule extends AbstractModule{
    private static final Logger logger = Logger.getLogger(GameModule.class.getName());
    private BlockManager blockManager;
    
    public GameModule() {
        blockManager = new BlockManager();
    }
    
    @Override
    protected void configure() {
        
        
    }
    @Provides
    public BlockManager giveBlockManager(){
        return blockManager;
    }


}
