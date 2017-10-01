package fingerprint.gameplay.objects;

import fingerprint.gameplay.objects.player.CharacterContainer;
import java.math.BigDecimal;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

public class CollidingObject extends GameObject{
    protected transient Shape collideShape;
    protected boolean collideToTerrain = false;
    
    public CollidingObject(double initX , double initY,Shape collidingShape) {
        super(initX, initY);
        this.collideShape = collidingShape;
    }
    

    public boolean isColliding(CollidingObject object){
        if(collideShape == null || object.getCollideShape() == null) {
            return false;
        }
        return collideShape.intersects(object.getCollideShape());
    }
    public Shape getCollideShape() {
        return collideShape;
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.gray);
        graphics.fill(collideShape);
        
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
        
        //boolean blockedXMovement = false;
        //boolean blockedYMovement = false;
        
        double maxDelta = Math.max(Math.abs(getDeltaX()), Math.abs(getDeltaY()));
        //Move in 0.1 chunks
        for(double currentDelta = 0.0d ; currentDelta < maxDelta ; currentDelta += 0.1d){
            //Move in X 
            if(currentDelta < Math.abs(getDeltaX())){
                double[] destination = moveDestinationX(delta);
                if(collideToTerrain){
                    Shape clonedShape = new Polygon(collideShape.getPoints());
                    
                    float locationX = (float)Math.floor(destination[0]);
                    float locationY = (float)Math.floor(destination[1]);
                    
                    clonedShape.setLocation(locationX,locationY);
                    if(!collisionManager.collideWithTerrain(clonedShape)){
                        setX(destination[0]);
                        setY(destination[1]);
                    }else {
                        onTerrainCollision();
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
                    } else {
                        onTerrainCollision();
                    }
                    
                }else{
                    setX(destination[0]);
                    setY(destination[1]);
                }
            }
        }
        
        float speed = 5.5f;
      //Reduce speed X 
        if(getDeltaX() < 0){
            if(-getDeltaX() > speed){
                setDeltaX(getDeltaX()  + speed);
            }else{
                setDeltaX(0);
            }
        }
        
        if(getDeltaX() > 0){
            if(getDeltaX() > speed){
                setDeltaX(getDeltaX()  - speed);
            }else{
                setDeltaX(0);
            }
        }
        //Reduce speed Y 
        if(getDeltaY() < 0){
            if(-getDeltaY() > speed){
                setDeltaY(getDeltaY()  + speed);
            }else{
                setDeltaY(0);
            }
        }
        
        if(getDeltaY() > 0){
            if(getDeltaY() > speed){
                setDeltaY(getDeltaY()  - speed);
            }else{
                setDeltaY(0);
            }
        }
        
        
        
    }
    protected void onCollision(CollidingObject collidedWith){
        
    }
    
    protected void onTerrainCollision(){
        
    }
    @Override
    public void setX(double x) {
        x = new BigDecimal(x).setScale(3, BigDecimal.ROUND_FLOOR).doubleValue();
        super.setX(x);
        collideShape.setX((float)getX());
    }
    @Override
    public void setY(double y) {
        y = new BigDecimal(y).setScale(3, BigDecimal.ROUND_FLOOR).doubleValue();
        super.setY(y);
        collideShape.setY((float)getY());
    }
    
    public void setCollideToTerrain(boolean collideToTerrain) {
        this.collideToTerrain = collideToTerrain;
    }
}
