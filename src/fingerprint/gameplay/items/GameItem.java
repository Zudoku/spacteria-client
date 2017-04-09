/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.items;

/**
 * Created Apr 9, 2017
 * @author arska
 */
public class GameItem {
    private int uniqueid;
    private String displayname;
    private String description;
    private int itemtypeid;
    private boolean stackable;
    private int levelreq;
    private boolean tradeable;
    private int rarity;
    private int sellvalue;

    public GameItem() {
    }

    /**
     * @return the uniqueid
     */
    public int getUniqueid() {
        return uniqueid;
    }

    /**
     * @param uniqueid the uniqueid to set
     */
    public void setUniqueid(int uniqueid) {
        this.uniqueid = uniqueid;
    }

    /**
     * @return the displayname
     */
    public String getDisplayname() {
        return displayname;
    }

    /**
     * @param displayname the displayname to set
     */
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the itemtypeid
     */
    public int getItemtypeid() {
        return itemtypeid;
    }

    /**
     * @param itemtypeid the itemtypeid to set
     */
    public void setItemtypeid(int itemtypeid) {
        this.itemtypeid = itemtypeid;
    }

    /**
     * @return the stackable
     */
    public boolean isStackable() {
        return stackable;
    }

    /**
     * @param stackable the stackable to set
     */
    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    /**
     * @return the levelreq
     */
    public int getLevelreq() {
        return levelreq;
    }

    /**
     * @param levelreq the levelreq to set
     */
    public void setLevelreq(int levelreq) {
        this.levelreq = levelreq;
    }

    /**
     * @return the tradeable
     */
    public boolean isTradeable() {
        return tradeable;
    }

    /**
     * @param tradeable the tradeable to set
     */
    public void setTradeable(boolean tradeable) {
        this.tradeable = tradeable;
    }

    /**
     * @return the rarity
     */
    public int getRarity() {
        return rarity;
    }

    /**
     * @param rarity the rarity to set
     */
    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    /**
     * @return the sellvalue
     */
    public int getSellvalue() {
        return sellvalue;
    }

    /**
     * @param sellvalue the sellvalue to set
     */
    public void setSellvalue(int sellvalue) {
        this.sellvalue = sellvalue;
    }
    
    

}
