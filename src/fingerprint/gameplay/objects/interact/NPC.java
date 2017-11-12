/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.interact;

import fingerprint.gameplay.objects.CollidingObject;
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.rendering.manager.UIRenderingUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created Nov 5, 2017
 * @author arska
 */
public class NPC extends CollidingObject implements Interactable{
    private String type;
    private int image;
    private int width;
    private int height;
    private String name;

    public NPC() {
        super(0, 0, new Rectangle(0, 0, 2, 2));
    }
    
    public void flushShape() {
        this.collideShape = new Rectangle((float)getX(),(float) getY(), width, height);
    }

    @Override
    public void draw(Graphics graphics) {
        double[] drawinCords = getDrawingCoordinates();
        
        if(image == 0){
            graphics.setColor(Color.green);
            graphics.fillRect((float)drawinCords[0] , (float)drawinCords[1], width, height);
        } else {
            Image sprite = UIRenderingUtil.getSpriteImage(image);
            sprite.drawCentered((float) drawinCords[0] + (width / 2), (float) drawinCords[1] + (height / 2));
        }
    }
    
    @Override
    protected void onCollision(CollidingObject collidedWith) {
        if(collidedWith instanceof GCharacter) {
            eventBus.post(new InteractableCollisionEvent(this));
        }
    }
    

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the image
     */
    public int getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(int image) {
        this.image = image;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String getInteractionText() {
        return "Talk to " + name;
    }

    @Override
    public int getInteractionLevel() {
        return 4;
    }
    
    

    
    

}
