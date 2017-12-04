package fingerprint.gameplay.objects.interact;

import fingerprint.gameplay.objects.CollidingObject;
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.rendering.manager.UIRenderingUtil;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.*;

public class Portal extends CollidingObject implements Interactable{

    private int to;
    private String hash;

    public Portal() {
        super(0, 0, new Rectangle(0, 0, 48, 48));
    }

    public void initialize() {
        this.getCollideShape().setLocation((float)getX(), (float) getY());
    }

    @Override
    public void draw(Graphics graphics) {

        double[] coordinates = getDrawingCoordinates();
        //Draw the collision rectangle
        Image sprite = UIRenderingUtil.getSpriteImage(19);
        sprite.drawCentered((float) coordinates[0] + (48 / 2), (float) coordinates[1] + (48 / 2));
    }

    @Override
    public void onCollision(CollidingObject collidedWith) {
        if(collidedWith instanceof GCharacter) {
            eventBus.post(new InteractableCollisionEvent(this));
        }
    }

    public String getHash() {
        return hash;
    }

    public int getTo() {
        return to;
    }

    @Override
    public String getInteractionText() {
        return "Enter " + to;
    }

    @Override
    public int getInteractionLevel() {
        return 3;
    }
}
