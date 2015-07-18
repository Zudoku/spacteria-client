package fingerprint.gameplay.objects.player;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;


import fingerprint.gameplay.objects.CollidingObject;
import fingerprint.gameplay.objects.CollisionManager;


public class Player extends CollidingObject{

    private transient final static double playerSpeed = 0.3d;
    
    public Player(double initX, double initY,int areaID) {
        super(initX,initY,playerSpeed,areaID,new Rectangle((float)initX,(float) initY, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight));
        setCollideToTerrain(true);
    }
    
    @Override
    public void draw(Graphics graphics) {
        drawDebug(graphics);
    }
    private void drawDebug(Graphics graphics){
        double[] coordinates = getDrawingCoordinates();
        graphics.setColor(Color.red);
        graphics.fillRect((float)coordinates[0] +1,(float)coordinates[1] +1 , PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight);
        graphics.setColor(Color.black);
        graphics.drawRect((float)coordinates[0] +1,(float)coordinates[1] +1, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight);
    }
    @Override
    public void move(int delta, CollisionManager collisionManager) {
        // TODO Auto-generated method stub
        super.move(delta, collisionManager);
    }

}
