/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects;

import fingerprint.gameplay.objects.player.StatContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


/**
 *
 * @author arska
 */
public class Enemy extends GameObject{
    
    private String image;
    private String type;
    private StatContainer stats;
    private String id;
    private Shape shape;
    
    public Enemy(double initX, double initY) {
        super(initX, initY);
    }
    public void initialize(){
        setX(shape.getPos().getX());
        setY(shape.getPos().getY());
    }

    @Override
    public void draw(Graphics graphics) {
        double[] drawinCords = getDrawingCoordinates();
        
        graphics.setColor(Color.black);
        graphics.fillRect((float)drawinCords[0] , (float)drawinCords[1], shape.getW(), shape.getH());
        graphics.setColor(Color.cyan);
        graphics.fillRect((float)drawinCords[0]+ 2, (float)drawinCords[1] + 2, shape.getW() -4, shape.getH()-4);
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        setX(shape.getPos().getX());
        setY(shape.getPos().getY());
    }
    
    
    
}
