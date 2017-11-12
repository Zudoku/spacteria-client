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
public class InteractableManager {
    
    private Interactable currentInteractable;
    private Interactable lastInteractable;
    
    public void collideWithInteractable(Interactable collided){
        if(currentInteractable == null){
            currentInteractable = collided;
        } else if(currentInteractable.getInteractionLevel() < collided.getInteractionLevel()){
            currentInteractable = collided;
        }
    }

    public Interactable getCurrentInteractable() {
        return currentInteractable;
    }

    public Interactable getLastInteractable() {
        return lastInteractable;
    }
    
    public void cycle(){
        lastInteractable = currentInteractable;
        currentInteractable = null;
    }
    
    public Interactable getPersistentInteractable(){
        if(currentInteractable == lastInteractable){
            return currentInteractable;
        }
        return null;
    }
    
    

}
