/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.mainmenus.serverlist;

import fingerprint.gameplay.objects.Enemy;
import fingerprint.gameplay.objects.GameObject;
import fingerprint.gameplay.objects.projectiles.Projectile;

/**
 *
 * @author arska
 */
public class MapDescription {
    private String filename;
    private int startX;
    private int startY;

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the initX
     */
    public int getStartX() {
        return startX;
    }

    /**
     * @param startX the initX to set
     */
    public void setStartX(int startX) {
        this.startX = startX;
    }

    /**
     * @return the initY
     */
    public int getStartY() {
        return startY;
    }

    /**
     * @param startY the initY to set
     */
    public void setStartY(int startY) {
        this.startY = startY;
    }

    
    
    
    
    
    
}
