/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.events;

import fingerprint.gameplay.objects.player.GCharacter;

/**
 * Created May 13, 2017
 * @author arska
 */
public class ModifyCharacterEvent {
    private GCharacter character;

    public GCharacter getCharacter() {
        return character;
    }

    public void setCharacter(GCharacter character) {
        this.character = character;
    }
    
    
    
}
