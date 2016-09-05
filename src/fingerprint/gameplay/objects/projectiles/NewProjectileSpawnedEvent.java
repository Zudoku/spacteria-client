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
public class NewProjectileSpawnedEvent {
    private Projectile projectile;

    public NewProjectileSpawnedEvent(Projectile projectile) {
        this.projectile = projectile;
    }

    public Projectile getProjectile() {
        return projectile;
    }
    
    
}
