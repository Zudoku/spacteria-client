package fingerprint.gameplay.items;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class InventoryDeserializer implements JsonDeserializer<Inventory>{
    
    

    @Override
    public Inventory deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        
        final JsonObject jo = je.getAsJsonObject();
        
        Inventory result = new Inventory();
        
        ArrayList<Item> itemOrder = jdc.deserialize(jo.get("itemOrder"), new TypeToken<ArrayList<Item>>(){}.getType());
        result.setItemOrder(itemOrder);
        
        final JsonObject itemsObject = jo.getAsJsonObject("items");
        
        HashMap<Item,Integer> items = new HashMap<>();
        
        for(java.util.Map.Entry<String, JsonElement> entry : itemsObject.entrySet()){
            Item newItem = ItemFactory.getItem(Integer.parseInt(entry.getKey()));
            items.put(newItem, entry.getValue().getAsInt());
            
        }
        
        result.setItems(items);
        
        return result;
    }

}
