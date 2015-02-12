package fingerprint.gameplay.objects;

import org.newdawn.slick.geom.Shape;

public class NPC extends CollidingObject{

    public NPC(double initX,double initY,double speed,int areaID,Shape collidingShape) {
        super(initX,initY,speed,areaID,collidingShape);
        
    }

}
