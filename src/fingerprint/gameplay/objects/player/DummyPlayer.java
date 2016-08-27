/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.gameplay.objects.player;

import fingerprint.gameplay.objects.GameObject;
import fingerprint.states.menu.enums.CharacterClass;
import java.util.logging.Logger;

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
