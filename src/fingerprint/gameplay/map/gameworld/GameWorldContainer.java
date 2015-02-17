package fingerprint.gameplay.map.gameworld;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMapPlus;

import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.gameplay.map.GameArea;
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
    private TiledMapPlus renderableTileMap;
    private int currentAreaID = 0;
    
    public GameWorldContainer() {
        playerContainer = new PlayerContainer();
        
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
        for(GameObject object: this.world.getObjects()){
            entityManager.addNewObject(object);
        }
        //setPlayer(this.world.getPlayer());
    }
    public void reloadTileMap(){
        try {
            renderableTileMap = new TiledMapPlus("resources/rendermap.tmx");
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    public void setPlayer(Player player){
        playerContainer.setCurrentPlayer(player);
        entityManager.addNewObject(player);
    }
    public TiledMapPlus getRenderableTileMap() {
        return renderableTileMap;
    }
    public GameArea getAreaByID(int id){
        for(GameArea area :world.getAreas() ){
            if(area.getAreaID() == id){
                return area;
            }
        }
        return null;
    }
    public void setCurrentAreaID(int id){
        this.currentAreaID = id;
    }
    
}
