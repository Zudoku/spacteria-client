/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.networking.events;

import fingerprint.gameplay.objects.player.DummyPlayer;

/**
 *
 * @author arska
 */
public class PlayerJoinedEvent {
    
    private DummyPlayer player;

    public PlayerJoinedEvent(DummyPlayer player) {
        this.player = player;
    }

    public DummyPlayer getPlayer() {
        return player;
    }
    
    
}
