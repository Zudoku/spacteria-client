package fingerprint.gameplay.objects;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public class CollidingObject extends GameObject{
    protected transient Shape collideShape;
    protected boolean collideToTerrain;
    
    public CollidingObject(double initX , double initY,double speed,int areaID,Shape collidingShape) {
        super(initX, initY,speed,areaID);
        this.collideShape = collidingShape;
    }
    

    public boolean isColliding(CollidingObject object){
        return collideShape.intersects(object.getCollideShape());
    }
    public Shape getCollideShape() {
        return collideShape;
    }
    @Override
    public void move(int delta, CollisionManager collisionManager) {
        if(!needMove()){
            return;
        }
        double[] destination = moveDestination(delta);
        if(collideToTerrain){
            Shape clonedShape = new Shape() {
                
                @Override
                public Shape transform(Transform arg0) {
                    return null;
                }
                
                @Override
                protected void createPoints() {
                    points = collideShape.getPoints();
                    
                }
            };
            clonedShape.setLocation((float)destination[0],(float) destination[1]);
            if(!collisionManager.collideWithTerrain(clonedShape)){
                setX(destination[0]);
                setY(destination[1]);
            }
            return;
        }else{
            setX(destination[0]);
            setY(destination[1]);
        }
    }
    protected void onCollision(CollidingObject collidedWith){
        
    }
    @Override
    public void setX(double x) {
        super.setX(x);
        collideShape.setX((float)getX());
    }
    @Override
    public void setY(double y) {
        super.setY(y);
        collideShape.setY((float)getY());
    }
}
