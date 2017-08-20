/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects.player;

import fingerprint.gameplay.objects.GameObject;
import fingerprint.states.menu.enums.CharacterClass;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author arska
 */
public class DummyCharacter extends GameObject{
    private transient static final Logger logger = Logger.getLogger(GCharacter.class.getName());
    
    private String charactername = "";
    private CharacterClass characterClass;
    private String id;
    
    private DummyCharacterCharacterData characterdata = null;

    public DummyCharacter() {
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
        graphics.fillRect((float)coordinates[0] + 2,(float)coordinates[1] + 2, CharacterContainer.playerCollisionWidth,CharacterContainer.playerCollisionHeight);
        graphics.setColor(Color.blue);
        graphics.fillRect((float)coordinates[0] + 6,(float)coordinates[1] + 6, CharacterContainer.playerCollisionWidth - 8,CharacterContainer.playerCollisionHeight -8);
        graphics.setColor(Color.magenta);
        int titleLenght = graphics.getFont().getWidth(charactername);
        graphics.drawString(charactername, (float)coordinates[0] - titleLenght/2, (float)coordinates[1]);

    }
    
    
    
    

    public DummyCharacter(CharacterClass characterClass,String charactername, double initX, double initY) {
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

    @Override
    public String toString() {
        if(characterdata != null) {
            return characterdata.getName();
        } else {
            return super.toString();
        }
    }

    public DummyCharacterCharacterData getCharacterdata() {
        return characterdata;
    }


    
    

    
    
}
