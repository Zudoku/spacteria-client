package fingerprint.gameplay.objects;

import fingerprint.gameplay.objects.events.RenderEnterPortalEvent;
import fingerprint.gameplay.objects.events.RenderLootBagEvent;
import fingerprint.gameplay.objects.player.CharacterContainer;
import fingerprint.gameplay.objects.player.GCharacter;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.*;

public class Portal extends CollidingObject {

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
        graphics.setColor(Color.orange);
        graphics.fillRect((float)coordinates[0] ,(float)coordinates[1] + 2, 48, 48);
        graphics.setColor(Color.black);
        graphics.fillRect((float)coordinates[0] + 4,(float)coordinates[1] + 6, 48 - 8, 48 -8);
    }

    @Override
    protected void onCollision(CollidingObject collidedWith) {
        if(collidedWith instanceof GCharacter) {
            eventBus.post(new RenderEnterPortalEvent(this));
        }
    }

    public String getHash() {
        return hash;
    }

    public int getTo() {
        return to;
    }
}
