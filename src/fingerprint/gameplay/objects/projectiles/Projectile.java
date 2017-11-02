/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects.projectiles;

import fingerprint.gameplay.objects.CollidingObject;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.Enemy;
import fingerprint.gameplay.objects.events.DeleteEntityEvent;
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.inout.FileUtil;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.sound.PlaySoundEvent;
import fingerprint.sound.SoundEffect;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author arska
 */
public class Projectile extends CollidingObject{
    
    private ProjectileImage image = ProjectileImage.BASIC;
    private double angle;
    private double speed;
    private transient double currentTravelDistance;
    private double maxTravelDistance;
    private ProjectilePath path = ProjectilePath.STRAIGHT;
    private boolean friendly;
    private int damage;
    private String guid;
    private boolean destroyed = false;
    private int width;
    private int height;
    
    private byte team;
    
    private transient Image imageRef = null;

    public Projectile(double angle, double speed, double maxTravelDistance, double initX, double initY) {
        super(initX, initY, new Rectangle((float)initX, (float)initX, 2, 2));
        this.angle = angle;
        this.speed = speed;
        this.maxTravelDistance = maxTravelDistance;
        this.currentTravelDistance = 0;
        this.damage = 0;
        super.setCollideToTerrain(true);
    }

    public Projectile() {
        super(0, 0, new Rectangle(0,0,2,2));
        super.setCollideToTerrain(true);
        this.currentTravelDistance = 0;
        this.damage = 0;
    }
    
    void init() {
        this.collideShape = new Rectangle((float)getX(), (float)getY(), width, height);
    }
    
    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
    
    

    @Override
    public void draw(Graphics graphics) {
        try {
            double[] drawingCoords = getDrawingCoordinates();
            
            if(imageRef == null) {
                imageRef = new Image(FileUtil.PROJECTILES_PATH + "/" + image.getFilename());
                imageRef.setCenterOfRotation(imageRef.getWidth() / 2, imageRef.getHeight() / 2);
            }
            
            float offsetX = (imageRef.getWidth() - width) / 2;
            float offsetY = (imageRef.getHeight() - height) / 2;
            
            imageRef.setRotation(360 - (int)getAngle());
            imageRef.draw((float)drawingCoords[0] - offsetX, (float)drawingCoords[1] - offsetY);
            graphics.drawRect((float)drawingCoords[0], (float)drawingCoords[1], width, height);
            
        } catch (SlickException ex) {
           Logger.getLogger(Projectile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void move(int delta, CollisionManager collisionManager) {
        double deltaMovX = getX();
        double deltaMovY = getY();
        super.move(delta, collisionManager);
        deltaMovX -= getX();
        deltaMovY -= getY();
        
        double diff = Math.sqrt(Math.abs(deltaMovX)* Math.abs(deltaMovX) + Math.abs(deltaMovY) * Math.abs(deltaMovY));
        currentTravelDistance += diff;
        if(maxTravelDistance < currentTravelDistance){
            destroyed = true;
            eventBus.post(new DeleteEntityEvent(guid));
        }
    }

    @Override
    protected void onCollision(CollidingObject collidedWith) {
        
        if(collidedWith instanceof Enemy && this.team == 1){
            Enemy enemy = (Enemy) collidedWith;
            eventBus.post(new PlaySoundEvent(SoundEffect.valueOf(enemy.getHitsound())));
            destroyed = true;
            eventBus.post(new DeleteEntityEvent(guid));
        }
        
        if(collidedWith instanceof GCharacter && this.team == 2){
            eventBus.post(new PlaySoundEvent(SoundEffect.CHARHIT));
            destroyed = true;
            eventBus.post(new DeleteEntityEvent(guid));
        }
    }

    @Override
    protected void onTerrainCollision() {
        if(!destroyed){
            destroyed = true;
            eventBus.post(new DeleteEntityEvent(guid));
        }
    }
   
    

    public double getAngle() {
        return angle;
    }

    public double getCurrentTravelDistance() {
        return currentTravelDistance;
    }

    public ProjectileImage getImage() {
        return image;
    }

    public double getMaxTravelDistance() {
        return maxTravelDistance;
    }

    public ProjectilePath getPath() {
        return path;
    }

    public double getSpeed() {
        return speed;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setCurrentTravelDistance(double currentTravelDistance) {
        this.currentTravelDistance = currentTravelDistance;
    }

    public void setImage(ProjectileImage image) {
        this.image = image;
    }

    public void setMaxTravelDistance(double maxTravelDistance) {
        this.maxTravelDistance = maxTravelDistance;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setPath(ProjectilePath path) {
        this.path = path;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public byte getTeam() {
        return team;
    }

    public void setTeam(byte team) {
        this.team = team;
    }

    
    
}
