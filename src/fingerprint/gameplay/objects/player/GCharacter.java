package fingerprint.gameplay.objects.player;

import fingerprint.gameplay.items.Currencies;
import java.util.logging.Logger;

import fingerprint.gameplay.items.Equipments;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import fingerprint.gameplay.items.Inventory;
import fingerprint.gameplay.objects.CollidingObject;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.particles.ParticleObject;
import fingerprint.gameplay.objects.particles.ShootParticle;
import fingerprint.gameplay.objects.projectiles.NewGameObjectSpawnedEvent;
import fingerprint.rendering.manager.UIRenderingUtil;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;


public class GCharacter extends CollidingObject{
    private transient static final Logger logger = Logger.getLogger(GCharacter.class.getName());
    
    private int level;
    private int experience;
    private int userid;
    private int uniqueid;
    private String created;
    private String status;

    private String name = "";
    private Inventory inventory;
    private Equipments equipment;
    private transient StatManager statManager;
    private Currencies currencies;
    
    private transient Animation animation;
    
    
    
    public GCharacter() {
        super(0,0, new Rectangle(0,0, CharacterContainer.playerCollisionWidth,CharacterContainer.playerCollisionHeight));
        collideToTerrain = true;
        statManager = new StatManager();
        
    }
    
    public void init(){
        collideToTerrain = true;
        if(status == null){
            status = "ALIVE";
        }
    }
    
    public GCharacter(double initX, double initY) {
        super(initX,initY,new Rectangle((float)initX,(float) initY, CharacterContainer.playerCollisionWidth,CharacterContainer.playerCollisionHeight));
        collideToTerrain = true;
        inventory = new Inventory();
        
    }
    
    @Override
    public void draw(Graphics graphics) {
        if(animation == null) {
            animation = new Animation(new Image[]{
                UIRenderingUtil.getSpriteImage(26), UIRenderingUtil.getSpriteImage(27), UIRenderingUtil.getSpriteImage(28)
            }, 300);
            
        }
        //drawDebug(graphics);
        //graphics.rotate(level, level, level);
        double[] coordinates = getDrawingCoordinates();
        //Image sprite = UIRenderingUtil.getSpriteImage(26);
        animation.draw((float) coordinates[0] - 8, (float) coordinates[1] - 8);
        //sprite.draw((float) coordinates[0] - 8, (float) coordinates[1] - 8);
    }
    
    @Override
    public void drawDebug(Graphics graphics){
        double[] coordinates = getDrawingCoordinates();
        //Draw the collision rectangle
        graphics.setColor(Color.white);
        graphics.fillRect((float)coordinates[0],(float)coordinates[1], CharacterContainer.playerCollisionWidth,CharacterContainer.playerCollisionHeight);
        
    }
    
    @Override
    public void move(int delta, CollisionManager collisionManager) {
        // TODO Auto-generated method stub
        super.move(delta, collisionManager);
    }
    
    public Inventory getInventory() {
        return inventory;
    }


    /**
     * @return the charactername
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the charactername to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public StatManager getStatManager() {
        return statManager;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(int uniqueid) {
        this.uniqueid = uniqueid;
    }
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Equipments getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipments equipment) {
        this.equipment = equipment;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setStatManager(StatManager statManager) {
        this.statManager = statManager;
    }

    public Currencies getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Currencies currencies) {
        this.currencies = currencies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
