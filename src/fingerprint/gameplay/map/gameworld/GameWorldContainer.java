package fingerprint.gameplay.map.gameworld;

import java.util.Collection;
import java.util.logging.Logger;


import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.gameplay.objects.GameObject;
import fingerprint.gameplay.objects.player.PlayerContainer;
import fingerprint.inout.TileFileHandler;

public class GameWorldContainer {
    private static final Logger logger = Logger.getLogger(GameWorldContainer.class.getName());
    
    @Inject private EntityManager entityManager;
    @Inject private CollisionManager collisionManager;
    private GameWorld world;
    private TileFileHandler tileFileHandler;
    @Inject private PlayerContainer playerContainer;
    @Inject private EventBus eventBus;
    
    public GameWorldContainer() {
        playerContainer = new PlayerContainer();
        tileFileHandler = new TileFileHandler();
        //eventBus.register(this);
        
    }
    public void updateWorld(InputManager inputManager,int delta){
        playerContainer.updatePlayer(inputManager,delta);
        for(GameObject object : (Collection<GameObject>)entityManager.getIdMap().values()){
            object.move(delta, collisionManager);
        }
        playerContainer.updateCamera();
        double addedTime = (1d / 100d);
        addedTime *= delta;

    }
    public void setWorld(GameWorld world) {
        this.world = world;
        eventBus.register(this);
    }

}
