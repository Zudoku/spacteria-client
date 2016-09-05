/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects.events;

/**
 *
 * @author arska
 */
public class DeleteEntityEvent {
    private String id;

    public DeleteEntityEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
    
    
}
