package fingerprint.gameplay.objects;

import org.newdawn.slick.geom.Shape;

public class CollidingObject {
    private Shape collideShape;
    public CollidingObject(Shape collidingShape) {
        this.collideShape = collidingShape;
    }
    
}
