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
import fingerprint.gameplay.objects.player.DummyPlayer;
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
     * @param value Object to assign.
     * @return ID that was used to assign value.
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
            if (co.getClass().equals(type)) {
                objects.add((T) co);
            }
        }
        return objects;
    }

    public void configure(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }
    
    @Subscribe
    public void listenPlayerJoinedEvent(PlayerJoinedEvent event){
        addNewObject(event.getPlayer().getId(), event.getPlayer());
        eventBus.post(new DisplayConsoleMessageEvent("Player " + event.getPlayer().getCharactername() + " joined game!", Color.yellow));
    }
    
    @Subscribe
    public void listenPlayerLeftEvent(PlayerLeftEvent event){
        DummyPlayer player = (DummyPlayer) getObjectWithID(event.getId());
        eventBus.post(new DisplayConsoleMessageEvent("Player " + player.getCharactername() + " left game!", Color.yellow));
        removeObjectWithID(event.getId());
    }
    
}
