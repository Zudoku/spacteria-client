/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.events;

import fingerprint.gameplay.objects.Enemy;

/**
 * Created Dec 12, 2017
 * @author arska
 */
public class NewEnemySpawnedEvent {
    private String hash;
    private Enemy enemy;

    public String getHash() {
        return hash;
    }

    public Enemy getEnemy() {
        return enemy;
    }
}
