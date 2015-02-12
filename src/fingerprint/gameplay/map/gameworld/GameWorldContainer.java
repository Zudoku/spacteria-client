package fingerprint.gameplay.map.gameworld;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.gameplay.objects.GameObject;
import fingerprint.gameplay.objects.player.Player;
import fingerprint.gameplay.objects.player.PlayerContainer;

public class GameWorldContainer {
    private static final Logger logger = Logger.getLogger(GameWorldContainer.class.getName());
    
    @Inject private EntityManager entityManager;
    @Inject private CollisionManager collisionManager;
    private GameWorld world;
    @Inject private PlayerContainer playerContainer;
    
    private int currentAreaID = 0;
    
    public GameWorldContainer() {
        playerContainer = new PlayerContainer();
        
    }
    public void updateWorld(InputManager inputManager,int delta){
        playerContainer.updatePlayer(inputManager,delta);
        for(GameObject object : (Collection<GameObject>)entityManager.getIdMap().values()){
            object.move(delta, collisionManager);
        }
    }
    public void setWorld(GameWorld world) {
        this.world = world;
        for(GameObject object: this.world.getObjects()){
            entityManager.addNewObject(object);
        }
        setPlayer(this.world.getPlayer());
    }
    public void setPlayer(Player player){
        playerContainer.setCurrentPlayer(player);
        entityManager.addNewObject(player);
    }
    public List<int[][]> getTileMapToDraw(){
        return world.getAreas().get(currentAreaID).getTileLayers();
    }
    
}
