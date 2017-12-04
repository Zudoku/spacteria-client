/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.networking.events;

import fingerprint.gameplay.objects.CollisionManager;

/**
 *
 * @author arska
 */
public class CorrectNPCPositionEvent {
    private String id;
    private double x;
    private double y;
    private transient CollisionManager collisionManager;

    public CorrectNPCPositionEvent() {
        
    }

    public CorrectNPCPositionEvent(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public void setCollisionManager(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    

    public void setId(String id) {
        this.id = id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    
}
