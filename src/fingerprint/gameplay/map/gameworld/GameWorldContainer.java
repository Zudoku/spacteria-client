package fingerprint.gameplay.map.gameworld;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMapPlus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.gameplay.interaction.UseInteractionManager;
import fingerprint.gameplay.interaction.UseItemEvent;
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
    private UseInteractionManager interactionManager;
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
    }
    public void setWorld(GameWorld world) {
        this.world = world;
        tileFileHandler.init(world.getMetaData().filename);
        collisionManager.setMap(world.getMap());
        for(GameObject object: this.world.getObjects()){
            entityManager.addNewObject(object);
        }
        interactionManager = new UseInteractionManager(getWorld(), tileFileHandler);
        eventBus.register(this);
        if(this.getWorld().getPlayer() != null){
            //this.setPlayer(this.world.getPlayer());
        }
        
    }
    public void setPlayer(Player player){
        playerContainer.setCurrentPlayer(player);
        world.setPlayer(player);
        entityManager.addNewObject(player);
    }
    public String worldFileName(){
        return world.getMetaData().filename;
    }
    public GameWorld getWorld() {
        return world;
    }
    
    
    @Subscribe
    public void listenUseItemEvents(UseItemEvent event){
        byte functionalTile = world.getMap().getData()[event.getxTileCoord()][event.getyTileCoord()];
        
        interactionManager.use(event.getOnHand(), functionalTile, event.getxTileCoord(), event.getyTileCoord());
    }

}
