package fingerprint.gameplay.items;

import fingerprint.gameplay.objects.lootbag.GameItemWrapper;

import java.io.Serializable;
import java.util.HashMap;

public class Inventory implements Serializable {

    private HashMap<String, GameItemWrapper> data = new HashMap<>();
    
    
    public GameItemWrapper getItem(int index) {
        GameItemWrapper result = data.get(Integer.toString(index));
        return result;
    }


}
