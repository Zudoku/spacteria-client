package fingerprint.gameplay.objects.player;

import org.newdawn.slick.geom.Rectangle;


import fingerprint.gameplay.objects.CollidingObject;


public class Player extends CollidingObject{

    public Player(double initX, double initY) {
        super(initX,initY,new Rectangle((float)initX,(float) initY, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight));
        
    }
    
    @Override
    public void draw() {

    }

}
