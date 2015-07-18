package fingerprint.gameplay.objects;

import java.math.BigDecimal;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
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
            displaySpeedX = getDeltaX();
            displaySpeedY = getDeltaY();
            return;
        }
        displaySpeedX = getDeltaX();
        displaySpeedY = getDeltaY();
        
        //Reduce speed X 
        if(getDeltaX() > 0){
            setDeltaX(getDeltaX()-getSpeed() < 0? 0 : getDeltaX()-getSpeed());
        }
        //Reduce speed Y
        if(getDeltaY() > 0){
            setDeltaY(getDeltaY()-getSpeed() < 0? 0 : getDeltaY()-getSpeed());
        }
        
        double[] destination = moveDestination(delta);
        if(collideToTerrain){
            Shape clonedShape = new Polygon(collideShape.getPoints());
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
        x = new BigDecimal(x).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        super.setX(x);
        collideShape.setX((float)getX());
    }
    @Override
    public void setY(double y) {
        y = new BigDecimal(y).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        super.setY(y);
        collideShape.setY((float)getY());
    }
    
    public void setCollideToTerrain(boolean collideToTerrain) {
        this.collideToTerrain = collideToTerrain;
    }
}
