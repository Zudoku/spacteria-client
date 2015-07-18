package fingerprint.gameplay.map.gameworld;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMapPlus;

import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.gameplay.objects.GameObject;
import fingerprint.gameplay.objects.player.Player;
import fingerprint.gameplay.objects.player.PlayerContainer;
import fingerprint.inout.TileFileHandler;

public class GameWorldContainer {
    private static final Logger logger = Logger.getLogger(GameWorldContainer.class.getName());
    
    @Inject private EntityManager entityManager;
    @Inject private CollisionManager collisionManager;
    private GameWorld world;
    private TileFileHandler tileFileHandler;
    @Inject private PlayerContainer playerContainer;
    
    public GameWorldContainer() {
        playerContainer = new PlayerContainer();
        tileFileHandler = new TileFileHandler();
        
    }
    public void updateWorld(InputManager inputManager,int delta){
        playerContainer.updatePlayer(inputManager,delta);
        for(GameObject object : (Collection<GameObject>)entityManager.getIdMap().values()){
            object.move(delta, collisionManager);
        }
        playerContainer.updateCamera();
    }
    public void setWorld(GameWorld world) {
        this.world = world;
        tileFileHandler.init(world.getMetaData().filename);
        collisionManager.setMap(world.getMap());
        for(GameObject object: this.world.getObjects()){
            entityManager.addNewObject(object);
        }
        //setPlayer(this.world.getPlayer());
    }
    public void setPlayer(Player player){
        playerContainer.setCurrentPlayer(player);
        entityManager.addNewObject(player);
    }
    public String worldFileName(){
        return world.getMetaData().filename;
    }

}
