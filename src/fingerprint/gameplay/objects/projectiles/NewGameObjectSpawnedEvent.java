/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects.projectiles;

import fingerprint.gameplay.objects.GameObject;
import fingerprint.gameplay.objects.particles.ParticleObject;

/**
 *
 * @author arska
 */
public class NewGameObjectSpawnedEvent {
    private GameObject gameobject;
    private String guid;

    public NewGameObjectSpawnedEvent(GameObject projectile, String guid) {
        this.gameobject = projectile;
        this.guid = guid;
        if(gameobject instanceof ParticleObject) {
            ((ParticleObject)gameobject).setGuid(guid);
        }
    }
    
    public NewGameObjectSpawnedEvent(Projectile projectile) {
        this.gameobject = projectile;
        this.guid = projectile.getGuid();
    }

    public GameObject getGameobject() {
        if(gameobject instanceof Projectile) {
            ((Projectile)gameobject).init();
        }
        return gameobject;
    }

    public String getGuid() {
        return guid;
    }
    
    
    
    
}
