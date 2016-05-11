package fingerprint.gameplay.objects.player;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    
    private Map<Item,Integer> items;
    
    private Item onHand;
    
    public Inventory() {
        items = new HashMap<Item, Integer>();
    }
    
    public void addItem(Item item){
        if(items.containsKey(item)){
            int quantity = items.get(item);
            quantity++;
            items.put(item, quantity);
        } else {
            items.put(item, 1);
        }
    }
    
    public void useItem(){
        if(onHand != null){
            int quantity = items.get(onHand);
            if(quantity == 1){
                items.remove(onHand);
            } else {
                quantity--;
                items.put(onHand, quantity);
            }
        }
    }
    
    public void removeItem(Item item){
        if(items.containsKey(item)){
            int quantity = items.get(item);
            if(quantity == 1){
                items.remove(item);
            } else {
                quantity--;
                items.put(item, quantity);
            }
        }
    }
    
    public void removeAll(Item item){
        if(items.containsKey(item)){
            items.remove(item);
        }
    }
    
    

}
