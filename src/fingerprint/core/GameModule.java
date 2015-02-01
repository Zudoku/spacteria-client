package fingerprint.core;

import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import fingerprint.gameplay.map.blocks.BlockManager;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.rendering.RenderingManager;


public class GameModule extends AbstractModule{
    private static final Logger logger = Logger.getLogger(GameModule.class.getName());
    private BlockManager blockManager;
    private EntityManager entityManager;
    private RenderingManager renderingManager;
    private EventBus eventBus;
    
    public GameModule() {
        blockManager = new BlockManager();
        entityManager = new EntityManager();
        renderingManager = new RenderingManager();
        eventBus  = new EventBus("GameEventBus");
    }
    
    @Override
    protected void configure() {
        renderingManager.configure(entityManager, blockManager);
        
    }
    @Provides
    public BlockManager giveBlockManager(){
        return blockManager;
    }
    @Provides
    public EntityManager giveEntityManager(){
        return entityManager;
    }
    @Provides
    public RenderingManager giveRenderingManager(){
        return renderingManager;
    }
    @Provides
    public EventBus giveEventBus(){
        return eventBus;
    }

}
