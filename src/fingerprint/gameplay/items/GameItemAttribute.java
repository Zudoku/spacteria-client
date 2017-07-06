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
public class GameItemAttribute {
    public static transient final String[] AttributeStrings = new String[]{
            "Health", "Vitality", "Strength", "Dexterity", "Defence", "Speed"
    };


    private int attributeid;
    private int attributevalue;

    public int getAttributeid() {
        return attributeid;
    }

    public int getAttributevalue() {
        return attributevalue;
    }

    public void setAttributeid(int attributeid) {
        this.attributeid = attributeid;
    }

    public void setAttributevalue(int attributevalue) {
        this.attributevalue = attributevalue;
    }

    @Override
    public String toString() {
        return AttributeStrings[this.attributeid - 1] + ": +" + this.attributevalue;
    }
}
