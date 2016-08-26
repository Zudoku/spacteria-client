package fingerprint.core;

import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import fingerprint.controls.InputManager;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.inout.GameSettings;
import fingerprint.rendering.RenderingManager;


public class GameModule extends AbstractModule{
    private static final Logger logger = Logger.getLogger(GameModule.class.getName());
    private EntityManager entityManager;
    private RenderingManager renderingManager;
    private EventBus eventBus;
    private InputManager inputManager;
    private Gson gson;
    
    public GameModule() {
        entityManager = new EntityManager();
        renderingManager = new RenderingManager();
        eventBus = new EventBus("GameEventBus");
        inputManager = new InputManager();
        gson = new Gson();
    }
    
    @Override
    protected void configure() {
        entityManager.configure(eventBus);
        renderingManager.configure(entityManager,eventBus);
        
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
    @Provides
    public InputManager giveInputManager(){
        return inputManager;
    }
    @Provides
    public Gson giveGson(){
        return gson;
    }
    public void setGameSettings(GameSettings gamesettings){
        inputManager.loadKeyBinds(gamesettings);
    }

}
