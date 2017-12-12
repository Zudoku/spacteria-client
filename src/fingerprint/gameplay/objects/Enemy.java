/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects;

import fingerprint.gameplay.objects.player.StatContainer;
import fingerprint.rendering.manager.UIRenderingUtil;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;


/**
 *
 * @author arska
 */
public class Enemy extends CollidingObject{
    
    private transient String zone = "0-0";
    
    private int image;
    private String type;
    private StatContainer stats;
    private String hash;
    private String hitsound;
    private String deathsound;
    private Shape shape;
    
    public Enemy(double initX, double initY) {
        super(initX, initY, new Rectangle(0, 0, 1, 1));
    }
    public void initialize(){
        this.collideShape = new Rectangle((float)shape.getPos().getX(), (float)shape.getPos().getX(), shape.getW(), shape.getH());
        setX(shape.getPos().getX());
        setY(shape.getPos().getY());
        this.zone = "0-0";
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    @Override
    public void draw(Graphics graphics) {
        double[] drawinCords = getDrawingCoordinates();
        
        if(image == 0){
            graphics.setColor(Color.black);
            graphics.fillRect((float)drawinCords[0] , (float)drawinCords[1], shape.getW(), shape.getH());
            graphics.setColor(Color.cyan);
            graphics.fillRect((float)drawinCords[0]+ 2, (float)drawinCords[1] + 2, shape.getW() -4, shape.getH()-4);
        } else {
            Image sprite = UIRenderingUtil.getSpriteImage(image);
            sprite.drawCentered((float) drawinCords[0] + (shape.getW() / 2), (float) drawinCords[1] + (shape.getH() / 2));
        }
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        this.collideShape = new Rectangle((float)shape.getPos().getX(), (float)shape.getPos().getX(), shape.getW(), shape.getH());
        setX(shape.getPos().getX());
        setY(shape.getPos().getY());
    }
    
    protected void updateZone(CollisionManager collisionManager){
        //zone = collisionManager.updateEnemyLocation(getX(), getY(), this);
    }
    
    public String getHash() {
        return hash;
    }

    public String getHitsound() {
        return hitsound;
    }

    public String getDeathsound() {
        return deathsound;
    }

    public String getZone() {
        return zone;
    }
    
    
}
