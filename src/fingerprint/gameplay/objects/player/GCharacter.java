package fingerprint.gameplay.objects.player;

import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import fingerprint.gameplay.items.Inventory;
import fingerprint.gameplay.map.gameworld.CharacterSaveFile;
import fingerprint.gameplay.objects.CollidingObject;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.states.menu.enums.CharacterClass;


public class GCharacter extends CollidingObject{
    private transient static final Logger logger = Logger.getLogger(GCharacter.class.getName());
    
    private int level;
    private int experience;
    private int userid;
    private int uniqueid;
    private int cclass;
    private String created;

    private transient CharacterSaveFile saveFile;
    private String name = "";
    private Inventory inventory;
    private CharacterClass characterClass;
    private transient StatManager statManager;
    
    
    
    public GCharacter() {
        super(0,0, new Rectangle(0,0, CharacterContainer.playerCollisionWidth,CharacterContainer.playerCollisionHeight));
        collideToTerrain = true;
    }
    
    public void init(){
        statManager = new StatManager(characterClass, level);
        collideToTerrain = true;
    }
    
    public GCharacter(double initX, double initY) {
        super(initX,initY,new Rectangle((float)initX,(float) initY, CharacterContainer.playerCollisionWidth,CharacterContainer.playerCollisionHeight));
        collideToTerrain = true;
        inventory = new Inventory();
        
    }
    
    @Override
    public void draw(Graphics graphics) {
        drawDebug(graphics);
    }
    
    @Override
    public void drawDebug(Graphics graphics){
        double[] coordinates = getDrawingCoordinates();
        //Draw the collision rectangle
        graphics.setColor(Color.white);
        graphics.fillRect((float)coordinates[0] + 2,(float)coordinates[1] + 2, CharacterContainer.playerCollisionWidth,CharacterContainer.playerCollisionHeight);
        graphics.setColor(Color.red);
        graphics.fillRect((float)coordinates[0] + 6,(float)coordinates[1] + 6, CharacterContainer.playerCollisionWidth - 8,CharacterContainer.playerCollisionHeight -8);
        
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
     * @return the saveFile
     */
    public CharacterSaveFile getSaveFile() {
        return saveFile;
    }

    /**
     * @param saveFile the saveFile to set
     */
    public void setSaveFile(CharacterSaveFile saveFile) {
        this.saveFile = saveFile;
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

    /**
     * @return the characterClass
     */
    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    /**
     * @param characterClass the characterClass to set
     */
    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
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

    public int getCclass() {
        return cclass;
    }

    public void setCclass(int cclass) {
        this.cclass = cclass;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
