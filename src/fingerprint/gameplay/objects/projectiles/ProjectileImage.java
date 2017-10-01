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
public enum ProjectileImage {
    BASIC("basicprojectile.png"),
    MINIBLOB("miniblob.png");
    
    private String filename;

    private ProjectileImage(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
    
    
    
    
}
