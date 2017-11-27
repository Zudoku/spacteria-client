/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.interact;

import fingerprint.gameplay.objects.CollidingObject;
import java.util.ArrayList;
import java.util.List;


import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.rendering.manager.UIRenderingUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * Created Apr 9, 2017
 * @author arska
 */
public class LootBag extends CollidingObject implements Interactable {
    private int quality;
    private transient String guid;
    private List<GameItemWrapper> items = new ArrayList<>();

    public LootBag(int quality, double initX, double initY, Shape collidingShape) {
        super(initX, initY, collidingShape);
        this.quality = quality;
    }

    public LootBag() {
        super(0, 0, new Rectangle(0,0,48,48));
    }

    public void flushShape() {
        this.collideShape = new Rectangle((float)getX(),(float) getY(), 48, 48);
    }

    @Override
    public void draw(Graphics graphics) {
        double[] drawinCords = getDrawingCoordinates();
        
        Image sprite = UIRenderingUtil.getSpriteImage(11);
        sprite.drawCentered((float) drawinCords[0] + (48 / 2), (float) drawinCords[1] + (48 / 2));
    }


    public List<GameItemWrapper> getItems() {
        return items;
    }

    @Override
    protected void onCollision(CollidingObject collidedWith) {
        if(collidedWith instanceof GCharacter) {
            eventBus.post(new InteractableCollisionEvent(this));
        }
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    public void setItems(List<GameItemWrapper> items) {
        this.items = items;
    }

    @Override
    public String getInteractionText() {
        return "";
    }

    @Override
    public int getInteractionLevel() {
        return 5;
    }
}
