package fingerprint.gameplay.objects;

import org.newdawn.slick.geom.Shape;

public class CollidingObject extends GameObject{
    protected transient Shape collideShape;
    protected boolean collideToTerrain;
    
    public CollidingObject(double initX , double initY,Shape collidingShape) {
        super(initX, initY);
        this.collideShape = collidingShape;
    }
    

    public boolean isColliding(CollidingObject object){
        return collideShape.intersects(object.getCollideShape());
    }
    public Shape getCollideShape() {
        return collideShape;
    }
    protected void onCollision(CollidingObject collidedWith){
        
    }
}
