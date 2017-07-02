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
    private transient StatContainer stats;

    public StatManager(CharacterClass characterClass) {
        this.characterClass = characterClass;
        stats = new StatContainer(1,1,1,1,1,100,1);
    }
    
    
    public StatContainer getStats(){
        return stats;
    }

    public void setStats(StatContainer stats) {
        this.stats = stats;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }
    
}
