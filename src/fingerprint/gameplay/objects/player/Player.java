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


public class Player extends CollidingObject{
    private transient static final Logger logger = Logger.getLogger(Player.class.getName());

    private transient final static double playerSpeed = 5.3d;
    private transient CharacterSaveFile saveFile;
    private String charactername = "";
    private Inventory inventory;
    private CharacterClass characterClass;
    
    
    
    
    public Player() {
        super(0,0,playerSpeed, new Rectangle(0,0, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight));
    }
    
    public Player(double initX, double initY) {
        super(initX,initY,playerSpeed,new Rectangle((float)initX,(float) initY, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight));
        setCollideToTerrain(true);
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
        graphics.setColor(Color.pink);
        graphics.fillRect((float)coordinates[0] + 2,(float)coordinates[1] + 2, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight);
        graphics.setColor(Color.red);
        graphics.fillRect((float)coordinates[0] + 6,(float)coordinates[1] + 6, PlayerContainer.playerCollisionWidth - 8,PlayerContainer.playerCollisionHeight -8);

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
    public String getCharactername() {
        return charactername;
    }

    /**
     * @param charactername the charactername to set
     */
    public void setCharactername(String charactername) {
        this.charactername = charactername;
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

}
