/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects.player;

import fingerprint.states.menu.enums.CharacterClass;

/**
 *
 * @author arska
 */
public class StatManager {

    private transient CharacterClass characterClass;
    private transient int level;

    public StatManager(CharacterClass characterClass, int level) {
        this.characterClass = characterClass;
        this.level = level;
    }
    
    
    public StatContainer getStats(){
        int health = 100 + level * characterClass.getBaseHealth();
        int dexterity = 100 + level * characterClass.getBaseDexterity();
        int strength = 100 + level * characterClass.getBaseStrength();
        int vitality = 100 + level * characterClass.getBaseVitality();
        int defence = 100 + level * characterClass.getBaseDefence();
        int speed = 100;
        
        
        
        return new StatContainer(health, vitality, strength, dexterity, defence, speed);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }
    
}
