package fingerprint.gameplay.objects;

import org.newdawn.slick.Graphics;

import fingerprint.rendering.RenderingManager;


public class GameObject {

    private int areaID;
    private double x;
    private double y;
    private double speed;
    private double deltaX = 0;
    private double deltaY = 0;
    public double displaySpeedX;
    public double displaySpeedY;
    
    public GameObject(double initX, double initY,double speed,int areaID) {
        this.x = initX;
        this.y = initY;
        this.speed = speed;
        this.areaID = areaID;
    }
    public void move(int delta,CollisionManager collisionManager){
        if(needMove()){
            double[] destination = moveDestination(delta);
            setX(destination[0]);
            setY(destination[1]);
        }else{
            displaySpeedX = 0;
            displaySpeedY = 0;
        }
        
    }
    protected double[] moveDestination(int delta){
        double rad = Math.atan2(getDeltaY(),getDeltaX());
        setDeltaX(0);
        setDeltaY(0);
        
        double destinationY =  (double)(getY() + Math.sin(rad) * speed * delta);
        double destinationX =  (double)(getX() + Math.cos(rad) * speed * delta);
        
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
    public int getAreaID() {
        return areaID;
    }
    public void setAreaID(int areaID) {
        this.areaID = areaID;
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
