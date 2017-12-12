package fingerprint.gameplay.objects;

import fingerprint.gameplay.objects.projectiles.Projectile;
import fingerprint.rendering.manager.RenderingManager;
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
        
        final double MOVE_CYCLE_AMOUNT = 0.3d;

        displaySpeedX = getDeltaX();
        displaySpeedY = getDeltaY();
        
        //boolean blockedXMovement = false;
        //boolean blockedYMovement = false;
        
        
        double maxDelta = Math.max(Math.abs(getDeltaX()), Math.abs(getDeltaY()));
        //Move in small chunks
        for(double currentDelta = 0.00001d ; currentDelta < maxDelta ; currentDelta += MOVE_CYCLE_AMOUNT){
            boolean checkCollideToTerrain = (this instanceof Projectile) 
                    ? (collideToTerrain && Math.floor(currentDelta / MOVE_CYCLE_AMOUNT) % 3 == 0)
                    : collideToTerrain; 
            //Move in X 
            if(currentDelta < Math.abs(getDeltaX())){
                double[] destination = moveDestinationX(delta, Math.abs(getDeltaX()), currentDelta);
                if(checkCollideToTerrain){
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
                double[] destination = moveDestinationY(delta, Math.abs(getDeltaY()), currentDelta);
                if(checkCollideToTerrain){
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
        setDeltaX(0);
        setDeltaY(0);
    }
    public void onCollision(CollidingObject collidedWith){
        
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
    @Override
    public double[] getDrawingCoordinates(){
        
        return new double[]{getX() - RenderingManager.getScreenStartX(), getY()  - RenderingManager.getScreenStartY() };
    }
}
