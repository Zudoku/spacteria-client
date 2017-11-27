/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.rendering.util;

import fingerprint.gameplay.items.Currencies;
import fingerprint.gameplay.items.Equipments;
import fingerprint.gameplay.items.GameItem;
import fingerprint.gameplay.items.Inventory;
import fingerprint.gameplay.map.gameworld.UIMode;
import fingerprint.gameplay.objects.interact.Interactable;
import fingerprint.gameplay.objects.interact.LootBag;
import fingerprint.gameplay.objects.player.StatContainer;
import fingerprint.inout.Chatline;
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
    private LootBag lootToRender;
    private Equipments equipmentToRender;
    private Inventory inventoryToRender;
    private Interactable collidedInteractable;
    private GameItem hoverGameItem;
    private List<Chatline> chat;
    private UIMode uiMode;
    private Currencies currencies;
    private byte[][] minimap;
    
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

    public List<Chatline> getChat() {
        return chat;
    }

    public void setChat(List<Chatline> chat) {
        this.chat = chat;
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

    public void setCollidedInteractable(Interactable collidedInteractable) {
        this.collidedInteractable = collidedInteractable;
    }

    public Interactable getCollidedInteractable() {
        return collidedInteractable;
    }

    public GameItem getHoverGameItem() {
        return hoverGameItem;
    }

    public void setHoverGameItem(GameItem hoverGameItem) {
        this.hoverGameItem = hoverGameItem;
    }

    public UIMode getUiMode() {
        return uiMode;
    }

    public void setUiMode(UIMode uiMode) {
        this.uiMode = uiMode;
    }

    public Currencies getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Currencies currencies) {
        this.currencies = currencies;
    }

    public byte[][] getMinimap() {
        return minimap;
    }

    public void setMinimap(byte[][] minimap) {
        this.minimap = minimap;
    }
}
