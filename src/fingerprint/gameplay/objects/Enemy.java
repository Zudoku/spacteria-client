/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects;

import fingerprint.gameplay.objects.player.StatContainer;
import org.newdawn.slick.geom.Shape;

/**
 *
 * @author arska
 */
public class Enemy extends CollidingObject{
    
    private String image;
    private String type;
    private StatContainer stats;
    private String id;
    
    public Enemy(double initX, double initY, Shape collidingShape) {
        super(initX, initY, collidingShape);
    }
    
}
