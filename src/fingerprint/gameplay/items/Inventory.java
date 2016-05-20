package fingerprint.gameplay.items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory implements Serializable {
    
    private HashMap<Item,Integer> items;
    private ArrayList<Item> itemOrder;
    
    private Item onHand;
    
    private int selection = 0;
    private int index = 0;
    
    public Inventory() {
        items = new HashMap<Item, Integer>();
        itemOrder = new ArrayList<>();
        Item hand = ItemFactory.getItem(ItemFactory.HAND);
        addItem(hand);
        onHand = hand;
    }
    
    
    public void left(){
        if(selection > 0){
            selection--;
            if(selection < index){
                index = selection;
            }
        }
    }
    
    public void right(){
        if(selection < items.keySet().size() - 1){
            selection++;
            if(index + 3 < selection){
                index++;
            }
        }
    }
    
    public void select(){
        onHand = itemOrder.get(selection);
    }
    
    public Item[] getUIItems(){
        Item[] result = new Item[4];
        for(int x = 0; x < 4; x++){
            if(x + index < itemOrder.size()){
                result[x] = itemOrder.get(x + index);
            } else {
                result[x] = null;
            }
        }
        return result;
    }
    
    
    
    public void addItem(Item item){
        if(items.containsKey(item)){
            int quantity = items.get(item);
            quantity++;
            items.put(item, quantity);
        } else {
            items.put(item, 1);
            itemOrder.add(item);
        }
    }
    
    public void useItem(){
        if(onHand != null){
            int quantity = items.get(onHand);
            if(quantity == 1){
                items.remove(onHand);
                itemOrder.remove(onHand);
            } else {
                quantity--;
                items.put(onHand, quantity);
            }
        }
    }
    
    public int getQuantity(Item item){
        return items.get(item);
    }
    
    public void removeItem(Item item){
        if(items.containsKey(item)){
            int quantity = items.get(item);
            if(quantity == 1){
                items.remove(item);
                itemOrder.remove(item);
            } else {
                quantity--;
                items.put(item, quantity);
            }
        }
    }
    
    public void removeAll(Item item){
        if(items.containsKey(item)){
            items.remove(item);
            itemOrder.remove(item);
        }
    }
    
    public Item getOnHand() {
        return onHand;
    }
    
    public int getSelection() {
        return selection;
    }
    
    public int getIndex() {
        return index;
    }
    
    
    public void setItems(HashMap<Item, Integer> items) {
        this.items = items;
    }
    public void setItemOrder(ArrayList<Item> itemOrder) {
        this.itemOrder = itemOrder;
    }
    

}
