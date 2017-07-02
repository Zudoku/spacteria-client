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
    private int maxhealth;
    
    private int vitality;
    private int strength;
    private int dexterity;
    private int defence;
    private int speed;

    public StatContainer(int health, int vitality, int strength, int dexterity, int defence, int speed, int maxhealth) {
        this.health = health;
        this.vitality = vitality;
        this.strength = strength;
        this.dexterity = dexterity;
        this.defence = defence;
        this.speed = speed;
        this.maxhealth = maxhealth;
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

    public int getMaxhealth() {
        return maxhealth;
    }

    public int[] getExpRequirements() {
        return new int[]{
                800, 2600, 4100, 7200, 10000,
                14800, 20400, 29000, 43000, 67600,
                90800, 145600, 210800, 306100, 454000,
                515000, 575800, 644400, 770000, 1200000
        };
    }
}
