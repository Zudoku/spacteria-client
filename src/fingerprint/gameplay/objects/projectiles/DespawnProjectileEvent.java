/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects.projectiles;

/**
 *
 * @author arska
 */
public class DespawnProjectileEvent {
    private String hash;

    public DespawnProjectileEvent(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }
    
    
}
