/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects.player;

import fingerprint.gameplay.objects.GameObject;
import static fingerprint.rendering.RenderingManager.unScaledScreenHeight;
import static fingerprint.rendering.RenderingManager.unScaledScreenWidth;
import fingerprint.states.menu.enums.CharacterClass;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author arska
 */
public class DummyPlayer extends GameObject{
    private transient static final Logger logger = Logger.getLogger(Player.class.getName());
    
    private String charactername = "";
    private CharacterClass characterClass;
    private String id;

    public DummyPlayer() {
        super(0, 0);
    }

    public String getId() {
        return id;
    }

    @Override
    public void draw(Graphics graphics) {
        double[] coordinates = getDrawingCoordinates();
        //Draw the collision rectangle
        graphics.setColor(Color.white);
        graphics.fillRect((float)coordinates[0] + 2,(float)coordinates[1] + 2, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight);
        graphics.setColor(Color.blue);
        graphics.fillRect((float)coordinates[0] + 6,(float)coordinates[1] + 6, PlayerContainer.playerCollisionWidth - 8,PlayerContainer.playerCollisionHeight -8);
        graphics.setColor(Color.magenta);
        int titleLenght = graphics.getFont().getWidth(charactername);
        graphics.drawString(charactername, (float)coordinates[0] - titleLenght/2, (float)coordinates[1]);

    }
    
    
    
    

    public DummyPlayer(CharacterClass characterClass,String charactername, double initX, double initY) {
        super(initX,initY);
        this.characterClass = characterClass;
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
