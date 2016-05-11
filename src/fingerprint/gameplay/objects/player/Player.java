package fingerprint.gameplay.objects.player;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;


import fingerprint.gameplay.objects.CollidingObject;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.Direction;


public class Player extends CollidingObject{

    private transient final static double playerSpeed = 5.3d;
    private Direction direction;
    
    public Player(double initX, double initY,int areaID) {
        super(initX,initY,playerSpeed,areaID,new Rectangle((float)initX,(float) initY, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight));
        setCollideToTerrain(true);
    }
    
    @Override
    public void draw(Graphics graphics) {
        drawDebug(graphics);
    }
    @Override
    public void drawDebug(Graphics graphics){
        double[] coordinates = getDrawingCoordinates();
        graphics.setColor(Color.pink);
        graphics.fillRect((float)coordinates[0] + 2,(float)coordinates[1] + 2, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight);
        graphics.setColor(Color.red);
        graphics.fillRect((float)coordinates[0] + 6,(float)coordinates[1] + 6, PlayerContainer.playerCollisionWidth - 8,PlayerContainer.playerCollisionHeight -8);
    }
    @Override
    public void move(int delta, CollisionManager collisionManager) {
        // TODO Auto-generated method stub
        super.move(delta, collisionManager);
    }
    
    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
