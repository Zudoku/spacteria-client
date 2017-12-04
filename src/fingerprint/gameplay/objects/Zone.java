/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects;

import fingerprint.gameplay.objects.projectiles.Projectile;
import java.util.List;

/**
 * Created Dec 1, 2017
 * @author arska
 */
public class Zone {
    private List<Enemy> enemies;
    private int x;
    private int y;
    private boolean needCollisionCheck = false;

    public Zone(List<Enemy> enemies, int x, int y) {
        this.enemies = enemies;
        this.x = x;
        this.y = y;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void removeEntity(Enemy target){
        enemies.remove(target);
    }
    
    public void addEntity(Enemy target){
        enemies.add(target);
    }
    
    public void clear(){
        enemies.clear();
        needCollisionCheck = false;
    }

    public void setNeedCollisionCheck(boolean needCollisionCheck) {
        this.needCollisionCheck = needCollisionCheck;
    }

    public boolean isNeedCollisionCheck() {
        return needCollisionCheck;
    }
    
    
    
    
    
    
}
