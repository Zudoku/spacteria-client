/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.networking.events;

/**
 *
 * @author arska
 */
public class PlayerLeftEvent {
    private String id;

    public PlayerLeftEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
    
    
}
