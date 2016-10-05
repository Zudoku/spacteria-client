/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.rendering;

import fingerprint.gameplay.objects.player.StatContainer;
import fingerprint.states.menu.enums.CharacterClass;

/**
 *
 * @author arska
 */
public class GamePlayRenderingInformation {
    private double cameraRotation;
    private String myName;
    private int level;
    private int experience;
    private StatContainer myStats;
    private String mapName;
    private CharacterClass charClass;
    
    public GamePlayRenderingInformation() {
    }

    public void setCameraRotation(double cameraRotation) {
        this.cameraRotation = cameraRotation;
    }

    public double getCameraRotation() {
        return cameraRotation;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public String getMyName() {
        return myName;
    }

    public StatContainer getMyStats() {
        return myStats;
    }

    public void setMyStats(StatContainer myStats) {
        this.myStats = myStats;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }

    public CharacterClass getCharClass() {
        return charClass;
    }

    public void setCharClass(CharacterClass charClass) {
        this.charClass = charClass;
    }
    
    
    
    
    
    
}
