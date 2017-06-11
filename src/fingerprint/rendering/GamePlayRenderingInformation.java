/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.rendering;

import fingerprint.gameplay.items.Equipments;
import fingerprint.gameplay.items.Inventory;
import fingerprint.gameplay.objects.Portal;
import fingerprint.gameplay.objects.lootbag.GameItemWrapper;
import fingerprint.gameplay.objects.lootbag.LootBag;
import fingerprint.gameplay.objects.player.StatContainer;
import fingerprint.states.menu.enums.CharacterClass;
import java.util.List;

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
    private LootBag lootToRender;
    private Equipments equipmentToRender;
    private Inventory inventoryToRender;
    private Portal portalToRender;
    
    public GamePlayRenderingInformation() {
    }

    public Equipments getEquipmentToRender() {
        return equipmentToRender;
    }

    public void setEquipmentToRender(Equipments equipmentToRender) {
        this.equipmentToRender = equipmentToRender;
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

    public LootBag getLootToRender() {
        return lootToRender;
    }

    public void setLootToRender(LootBag lootToRender) {
        this.lootToRender = lootToRender;
    }

    public Inventory getInventoryToRender() {
        return inventoryToRender;
    }

    public void setInventoryToRender(Inventory inventoryToRender) {
        this.inventoryToRender = inventoryToRender;
    }


    public Portal getPortalToRender() {
        return portalToRender;
    }

    public void setPortalToRender(Portal portalToRender) {
        this.portalToRender = portalToRender;
    }
}
