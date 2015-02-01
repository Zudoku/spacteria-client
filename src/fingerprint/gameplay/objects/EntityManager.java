package fingerprint.gameplay.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Singleton;

@Singleton
public class EntityManager {
    private static final Logger logger = Logger.getLogger(EntityManager.class.getName());
    private transient Map<Integer,GameObject> idMap=new HashMap<>();
    private int currentID;
    
    public EntityManager() {
        currentID = 1;
    }
    /**
     * Reserves ID for you. It is used to identify the object later.
     * Remember to add the object to idMap so it can be looked up.
     * @return unique int.
     */
    public int reserveID(){
        currentID+=1;
        logger.log(Level.FINEST,"RESERVED ID TO YOU.YOUR ID IS {1}",new Object[]{currentID,currentID-1});
        return currentID-1;
    }
    /**
     * This is a straight forward way of reserving ID and adding it later on.
     * This reserves ID to you, then adds it to idMap and returns you the ID that object was assigned to.
     * @param value Object to assign.
     * @return ID that was used to assign value.
     */
    public int addNewObject(GameObject value){
        idMap.put(currentID, value);
        currentID+=1;
        logger.log(Level.FINEST,"Added {0} with ID {1} to MAP. CurrentID = {2}",new Object[]{value.toString(),currentID -1,currentID});
        return currentID-1;
    }
    /**
     * Add value to idMap using ID.
     * @param ID unique ID that was reserved from reserveID()
     * @param value Object to assign.
     */
    public void addOldObject(int ID,GameObject value){
        idMap.put(ID, value);
        logger.log(Level.FINEST,"Added {0} with ID {1} to MAP",new Object[]{value.toString(),ID});
    }
    public Map<Integer, GameObject> getIdMap() {
        return idMap;
    }
    /**
     * Attempts to return Object that is assigned to that ID.
     * Returns null if there is no Object assigned with that ID.
     * @param ID unique ID.
     * @return Object.
     */
    public GameObject getObjectWithID(int ID){
        GameObject object=null;
        if(idMap.containsKey(ID)){
            object=idMap.get(ID);
        }
        return object;
    }
    public int getCurrentID() {
        return currentID;
    }

    public void setCurrentID(int currentID) {
        this.currentID = currentID;
    }
    public void removeObjectWithID(int ID){
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
    
}
