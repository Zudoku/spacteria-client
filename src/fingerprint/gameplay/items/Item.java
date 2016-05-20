package fingerprint.gameplay.items;

import java.io.Serializable;

public class Item implements Serializable{
    
    private String filename;
    private int ID;
    
    public Item(String filename,int ID) {
        this.filename = filename;
        this.ID = ID;
    }
    
    
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Item)){
            return false;
        }
        Item otherItem = (Item) obj;
        
        if(otherItem.filename.equals(this.filename)){
            return true;
        }
        return false;
    } 
    
    
    public String getName() {
        return filename;
    }
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public int getID() {
        return ID;
    }
    public void setID(int iD) {
        ID = iD;
    }
    
    @Override
    public int hashCode() {
        return filename.hashCode();
    }
    
    @Override
    public String toString() {
        return "" + ID;
    }
    
    

}
