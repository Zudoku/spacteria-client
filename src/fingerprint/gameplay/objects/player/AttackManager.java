/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects.player;

/**
 *
 * @author arska
 */
public class AttackManager {
    private long lastAttacked = 0;
    private long attackSpeed = 500;

    public AttackManager() {
        
    }
    
    public void setDexterity(int dexterity){
        attackSpeed =(long) (1000D / (double)((100D * (double) dexterity) / 1000D));
    }
    
    public void attack(){
        long timeNow = System.currentTimeMillis();
        lastAttacked = timeNow;
    }
    
    public boolean canAttack(){
        long timeNow = System.currentTimeMillis();
        if(timeNow - lastAttacked > attackSpeed){
            return true;
        }
        
        return false;
    }
    
}
