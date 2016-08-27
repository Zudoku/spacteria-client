/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects.player;

/**
 *
 * @author arska
 */
public class StatContainer {
    
    private int health;
    
    private int vitality;
    private int strength;
    private int dexterity;
    private int defence;
    private int speed;

    public StatContainer(int health, int vitality, int strength, int dexterity, int defence, int speed) {
        this.health = health;
        this.vitality = vitality;
        this.strength = strength;
        this.dexterity = dexterity;
        this.defence = defence;
        this.speed = speed;
    }

    public int getDefence() {
        return defence;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return strength;
    }

    public int getVitality() {
        return vitality;
    }

    public int getSpeed() {
        return speed;
    }
    
    
}
