package fingerprint.gameplay.objects;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import fingerprint.core.GameLauncher;
import org.newdawn.slick.Graphics;

import fingerprint.rendering.RenderingManager;


public class GameObject {

    private double x;
    private double y;
    private double deltaX = 0;
    private double deltaY = 0;
    public double displaySpeedX;
    public double displaySpeedY;
    
    @Inject protected transient EventBus eventBus;
    
    public GameObject(double initX, double initY) {
        this.x = initX;
        this.y = initY;
        //TODO: Dirty trick
        GameLauncher.injector.injectMembers(this);
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
        double destinationX =  (double)(getX() + (movechunk / 1));
        
        return new double[]{destinationX,destinationY};
        
    }
    protected double[] moveDestinationY(int delta){
        double movechunk = 0;
        if(getDeltaY() > 0){
            movechunk = 0.1d;
        } else {
            movechunk = -0.1d;
        }
        
        double destinationY =  (double)(getY() + (movechunk / 1));
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
    
    public void setDeltaX(double deltaX,double slowAmount) {
        this.deltaX = deltaX;
    }
    public void setDeltaY(double deltaY, double slowAmount) {
        this.deltaY = deltaY;
    }
    protected double[] getDrawingCoordinates(){
        
        return new double[]{getX() - RenderingManager.getScreenStartX(), getY() - RenderingManager.getScreenStartY() };
    }
}
