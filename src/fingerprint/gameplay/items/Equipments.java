package fingerprint.gameplay.items;

import java.io.Serializable;
import java.util.HashMap;

public class Equipments implements Serializable {
    private HashMap<String, GameItem> data = new HashMap<>();
    
    public GameItem getItem(int index) {
        GameItem result = data.get(Integer.toString(index));
        return result;
    }
    
}
