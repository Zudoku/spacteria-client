/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.events;

import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.gameplay.objects.player.StatContainer;

/**
 * Created May 13, 2017
 * @author arska
 */
public class ModifyCharacterEvent {
    private GCharacter character;
    private StatContainer stats;

    public GCharacter getCharacter() {
        return character;
    }

    public void setCharacter(GCharacter character) {
        this.character = character;
    }

    public StatContainer getStats() {
        return stats;
    }

    public void setStats(StatContainer stats) {
        this.stats = stats;
    }
}
