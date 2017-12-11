/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.items;

import java.util.HashMap;
import java.util.Map;

/**
 * Created Apr 9, 2017
 * @author arska
 */
public class GameItemAttribute {
    
    public static transient final Map<Integer, String> ATTRIBUTE_STRINGS; 
    static {
        ATTRIBUTE_STRINGS = new HashMap<>();
        ATTRIBUTE_STRINGS.put(1, "Health");
        ATTRIBUTE_STRINGS.put(2, "Vitality");
        ATTRIBUTE_STRINGS.put(3, "Strength");
        ATTRIBUTE_STRINGS.put(4, "Dexterity");
        ATTRIBUTE_STRINGS.put(5, "Defence");
        ATTRIBUTE_STRINGS.put(6, "Speed");
        ATTRIBUTE_STRINGS.put(10, "ProjectileID");
        ATTRIBUTE_STRINGS.put(11, "Projectile Speed");
        ATTRIBUTE_STRINGS.put(12, "Distance");
    }


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
        if( ATTRIBUTE_STRINGS.containsKey(attributeid)){
            return ATTRIBUTE_STRINGS.get(attributeid) + ": " + getOperand() + this.attributevalue;
        } else {
            return this.attributeid + ": +" + this.attributevalue;
        }
    }
    
    public String getOperand(){
        if(attributeid >= 0 && attributeid <= 6 && attributevalue > 0){
            return "+";
        }
        return "";
    }
}
