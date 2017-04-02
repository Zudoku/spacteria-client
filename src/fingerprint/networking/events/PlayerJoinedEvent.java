/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.networking.events;

import fingerprint.gameplay.objects.player.DummyCharacter;

/**
 *
 * @author arska
 */
public class PlayerJoinedEvent {
    
    private DummyCharacter player;

    public PlayerJoinedEvent(DummyCharacter player) {
        this.player = player;
    }

    public DummyCharacter getPlayer() {
        return player;
    }
    
    
}
