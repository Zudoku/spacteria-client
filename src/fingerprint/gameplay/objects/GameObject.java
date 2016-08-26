package fingerprint.gameplay.objects;

import org.newdawn.slick.Graphics;

import fingerprint.rendering.RenderingManager;


public class GameObject {

    private double x;
    private double y;
    private double speed;
    private double deltaX = 0;
    private double deltaY = 0;
    public double displaySpeedX;
    public double displaySpeedY;
    
    public GameObject(double initX, double initY,double speed) {
        this.x = initX;
        this.y = initY;
        this.speed = speed;
    }
    public void move(int delta,CollisionManager collisionManager){
        if(needMove()){
            //double[] destination = moveDestination(delta);
            //setX(destination[0]);
            //setY(destination[1]);
        }else{
            displaySpeedX = 0;
            displaySpeedY = 0;
        }
        
    }
    protected double[] moveDestinationX(int delta){
        double movechunk = 0;
        if(getDeltaX() > 0){
            movechunk = 0.1d;
        } else {
            movechunk = -0.1d;
        }
        
        double destinationY =  (double)(getY());
        double destinationX =  (double)(getX() + (movechunk / delta));
        
        return new double[]{destinationX,destinationY};
        
    }
    protected double[] moveDestinationY(int delta){
        double movechunk = 0;
        if(getDeltaY() > 0){
            movechunk = 0.1d;
        } else {
            movechunk = -0.1d;
        }
        
        double destinationY =  (double)(getY() + (movechunk / delta));
        double destinationX =  (double)(getX());
        
        return new double[]{destinationX,destinationY};
        
    }
    protected boolean needMove(){
        if(deltaX != 0 || deltaY != 0){
            return true;
        }
        return false;
    }
    public void draw(Graphics graphics) {

    }
    public void drawDebug(Graphics graphics) {

    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public double getSpeed() {
        return speed;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getDeltaX() {
        return deltaX;
    }
    public double getDeltaY() {
        return deltaY;
    }
    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }
    public void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    protected double[] getDrawingCoordinates(){
        
        return new double[]{getX() - RenderingManager.getScreenStartX(), getY() - RenderingManager.getScreenStartY() };
    }
}
