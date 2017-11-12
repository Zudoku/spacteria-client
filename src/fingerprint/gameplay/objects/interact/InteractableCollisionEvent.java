/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.interact;

/**
 * Created Nov 12, 2017
 * @author arska
 */
public class InteractableCollisionEvent {
    private Interactable i;

    public InteractableCollisionEvent(Interactable i) {
        this.i = i;
    }
    
    public Interactable getI() {
        return i;
    }
    
    

}
