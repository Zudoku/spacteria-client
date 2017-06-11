package fingerprint.gameplay.objects;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Singleton;
import fingerprint.gameplay.objects.events.DeleteEntityEvent;
import fingerprint.gameplay.objects.events.ModifyLootBagEvent;
import fingerprint.gameplay.objects.events.NewLootBagSpawnedEvent;
import fingerprint.gameplay.objects.lootbag.LootBag;
import fingerprint.gameplay.objects.player.DummyCharacter;
import fingerprint.gameplay.objects.projectiles.NewProjectileSpawnedEvent;
import fingerprint.gameplay.objects.projectiles.Projectile;
import fingerprint.networking.events.CorrectNPCPositionEvent;
import fingerprint.networking.events.PlayerJoinedEvent;
import fingerprint.networking.events.PlayerLeftEvent;
import fingerprint.rendering.DisplayConsoleMessageEvent;
import org.newdawn.slick.Color;

@Singleton
public class EntityManager {
    private static final Logger logger = Logger.getLogger(EntityManager.class.getName());
    private transient Map<String,GameObject> idMap=new HashMap<>();
    
    private EventBus eventBus;
    
    public EntityManager() {
        
    }
    /**
     * This is a straight forward way of reserving ID and adding it later on.
     * This reserves ID to you, then adds it to idMap and returns you the ID that object was assigned to.
     * @param key
     * @param value Object to assign.
     */
    public void addNewObject(String key, GameObject value){
        idMap.put(key, value);
        logger.log(Level.FINEST,"Added {0} with ID {1} to MAP.",new Object[]{value.toString(),key,});
    }
    public Map<String, GameObject> getIdMap() {
        return idMap;
    }
    /**
     * Attempts to return Object that is assigned to that ID.
     * Returns null if there is no Object assigned with that ID.
     * @param ID unique ID.
     * @return Object.
     */
    public GameObject getObjectWithID(String ID){
        GameObject object=null;
        if(idMap.containsKey(ID)){
            object=idMap.get(ID);
        }
        return object;
    }
    public void removeObjectWithID(String ID){
        if(idMap.containsKey(ID)){
            idMap.remove(ID);
            logger.log(Level.FINEST,"Removed object with ID {0} from MAP",new Object[]{ID});
            return;
        }
        logger.log(Level.FINER,"Couldn't find any object with ID {0} , didn't do anything.",new Object[]{ID});
    }
    public <T extends GameObject> Set<T> get(Class<T> type) {
        HashSet<T> objects = new HashSet<T>();

        for (GameObject co : idMap.values()) {
            if (type.isInstance(co)) {
                objects.add((T) co);
            }
        }
        return objects;
    }

    public void configure(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }
    
    public void clear(String myID){
        GameObject player = getObjectWithID(myID);
        idMap.clear();
        if(player != null){
            addNewObject(myID, player);
        }
        
    }
    
    @Subscribe
    public void listenPlayerJoinedEvent(PlayerJoinedEvent event){
        addNewObject(event.getPlayer().getId(), event.getPlayer());
        eventBus.post(new DisplayConsoleMessageEvent("Player " + event.getPlayer().getCharactername() + " joined game!", Color.yellow));
    }
    
    @Subscribe
    public void listenPlayerLeftEvent(PlayerLeftEvent event){
        DummyCharacter player = (DummyCharacter) getObjectWithID(event.getId());
        eventBus.post(new DisplayConsoleMessageEvent("Player " + player.getCharactername() + " left game!", Color.yellow));
        removeObjectWithID(event.getId());
    }
    
    @Subscribe
    public void listenCorrectPlayerPositionEvent(CorrectNPCPositionEvent event){
        GameObject handled = getObjectWithID(event.getId());
        if(handled instanceof DummyCharacter || handled instanceof Enemy){
            handled.setX(event.getX());
            handled.setY(event.getY());
        }
    }
    @Subscribe
    public void listenNewProjectileSpawnedEvent(NewProjectileSpawnedEvent event){
        addNewObject(event.getProjectile().getGuid(), event.getProjectile());
    }
    
    @Subscribe
    public void listenModifyLootBagEvent(ModifyLootBagEvent event) {
        LootBag bag = (LootBag) getObjectWithID(event.getGuid());
        bag.setItems(event.getLootbag());
    }
    
    @Subscribe
    public void listenNewLootBagSpawnedEvent(NewLootBagSpawnedEvent event){
        event.getLootbag().flushShape();
        event.getLootbag().setGuid(event.getGuid());
        addNewObject(event.getGuid(), event.getLootbag());
    }
    
    @Subscribe
    public void listenDeleteEntityEvent(DeleteEntityEvent event){
        removeObjectWithID(event.getId());
    }

    public void updateProjectileDelta(int delta) {
        for(Projectile projectile : get(Projectile.class)){
            double angleInRadians = (projectile.getAngle() / 360) * (2 * Math.PI);
            double scaler = ((double)projectile.getSpeed() * delta / 200d);
            
            double deltaX = -Math.sin(angleInRadians);
            double deltaY = -Math.cos(angleInRadians);
            
            //Apply speed buffs
            
            deltaX *= scaler;
            deltaY *= scaler;
            
            projectile.setDeltaX(deltaX);
            projectile.setDeltaY(deltaY);
        }
    }
    public void updateEntities(int delta, CollisionManager collisionManager){
        updateProjectiles(delta, collisionManager);
        collisionManager.checkCollision();
    }
    
    private void updateProjectiles(int delta, CollisionManager collisionManager){
        for(Projectile projectile : get(Projectile.class)){
            projectile.move(delta, collisionManager);
        }
    }
    
    
}
