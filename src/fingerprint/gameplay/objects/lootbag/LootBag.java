/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.lootbag;

import fingerprint.gameplay.objects.CollidingObject;
import fingerprint.gameplay.objects.GameObject;
import java.util.ArrayList;
import java.util.List;

import fingerprint.gameplay.objects.events.RenderLootBagEvent;
import fingerprint.gameplay.objects.player.GCharacter;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * Created Apr 9, 2017
 * @author arska
 */
public class LootBag extends CollidingObject {
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
        
        graphics.setColor(Color.black);
        graphics.fillRect((float)drawinCords[0] , (float)drawinCords[1], getCollideShape().getWidth(), getCollideShape().getHeight());
        graphics.setColor(Color.red);
        graphics.fillRect((float)drawinCords[0]+ 2, (float)drawinCords[1] + 2, getCollideShape().getWidth() -4, getCollideShape().getHeight()-4);
    }


    public List<GameItemWrapper> getItems() {
        return items;
    }

    @Override
    protected void onCollision(CollidingObject collidedWith) {
        if(collidedWith instanceof GCharacter) {
            eventBus.post(new RenderLootBagEvent(this));
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
}
