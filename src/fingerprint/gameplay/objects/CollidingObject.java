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
        
        double maxDelta = Math.max(Math.abs(getDeltaX()), Math.abs(getDeltaY()));
        //Move in 0.1 chunks
        for(double currentDelta = 0.0d ; currentDelta < maxDelta ; currentDelta += 0.1d){
            //Move in X 
            if(currentDelta < Math.abs(getDeltaX())){
                double[] destination = moveDestinationX(delta);
                if(collideToTerrain){
                    Shape clonedShape = new Polygon(collideShape.getPoints());
                    clonedShape.setLocation((float)Math.floor(destination[0]),(float) Math.floor(destination[1]));
                    if(!collisionManager.collideWithTerrain(clonedShape)){
                        setX(destination[0]);
                        setY(destination[1]);
                    }
                    
                }else{
                    setX(destination[0]);
                    setY(destination[1]);
                }
            }
            
            //Move in Y 
            if(currentDelta < Math.abs(getDeltaY())){
                double[] destination = moveDestinationY(delta);
                if(collideToTerrain){
                    Shape clonedShape = new Polygon(collideShape.getPoints());
                    clonedShape.setLocation((float)Math.floor(destination[0]),(float) Math.floor(destination[1]));
                    if(!collisionManager.collideWithTerrain(clonedShape)){
                        setX(destination[0]);
                        setY(destination[1]);
                    }
                    
                }else{
                    setX(destination[0]);
                    setY(destination[1]);
                }
            }
        }
        
        
      //Reduce speed X 
        if(getDeltaX() < 0){
            if(-getDeltaX() > getSpeed()){
                setDeltaX(getDeltaX()  + getSpeed());
            }else{
                setDeltaX(0);
            }
        }
        
        if(getDeltaX() > 0){
            if(getDeltaX() > getSpeed()){
                setDeltaX(getDeltaX()  - getSpeed());
            }else{
                setDeltaX(0);
            }
        }
        //Reduce speed Y 
        if(getDeltaY() < 0){
            if(-getDeltaY() > getSpeed()){
                setDeltaY(getDeltaY()  + getSpeed());
            }else{
                setDeltaY(0);
            }
        }
        
        if(getDeltaY() > 0){
            if(getDeltaY() > getSpeed()){
                setDeltaY(getDeltaY()  - getSpeed());
            }else{
                setDeltaY(0);
            }
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
