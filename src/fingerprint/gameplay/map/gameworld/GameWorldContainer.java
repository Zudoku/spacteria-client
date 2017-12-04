package fingerprint.gameplay.map.gameworld;

import fingerprint.gameplay.objects.interact.Portal;
import fingerprint.gameplay.objects.interact.NPC;
import java.util.logging.Logger;


import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.items.Currencies;
import fingerprint.gameplay.items.Equipments;
import fingerprint.gameplay.items.Inventory;
import fingerprint.gameplay.objects.*;
import fingerprint.gameplay.objects.events.ModifyCharacterEvent;
import fingerprint.gameplay.objects.events.gui.EnterPortalEvent;
import fingerprint.gameplay.objects.events.gui.TeleportCampEvent;
import fingerprint.gameplay.objects.interact.Interactable;
import fingerprint.gameplay.objects.interact.InteractableManager;
import fingerprint.gameplay.objects.interact.LootBag;
import fingerprint.gameplay.objects.player.DummyCharacter;
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.gameplay.objects.player.CharacterContainer;
import fingerprint.gameplay.objects.player.StatContainer;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.rendering.gui.event.DisplayConsoleMessageEvent;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Color;


public class GameWorldContainer {
    private static final Logger logger = Logger.getLogger(GameWorldContainer.class.getName());
    
    @Inject private EntityManager entityManager;
    @Inject private CollisionManager collisionManager;
    private RoomDescription world;
    @Inject private CharacterContainer playerContainer;
    @Inject private EventBus eventBus;
    private InteractableManager interactableManager;
    
    private UIMode uiMode;
    

    public GameWorldContainer() {
        playerContainer = new CharacterContainer();
        interactableManager = new InteractableManager();
        uiMode = UIMode.NORMAL;
        
    }
    public void updateWorld(InputManager inputManager,int delta, boolean canResurrect){
        collisionManager.checkIfNeedInit();
        if(playerContainer.getPlayerStatus().equals("ALIVE")){
            playerContainer.updatePlayer(inputManager,delta);
            interactUpdate(inputManager);
        }
        if(canResurrect){
            if(inputManager.isKeyBindPressed(KeyBindAction.D, true )) {
                eventBus.post(new TeleportCampEvent());
            }
        }
        playerContainer.updateCamera();
        entityManager.updateProjectileDelta(delta);
        entityManager.updateEntities(delta,collisionManager);
        checkNotInShop();

    }

    private void interactUpdate(InputManager inputManager) {
        if(inputManager.isKeyBindPressed(KeyBindAction.D, true )) {
            Interactable currentInteractable = interactableManager.getLastInteractable();
            if(currentInteractable instanceof Portal){
                Portal portalInteractable = (Portal) currentInteractable;
                eventBus.post(new EnterPortalEvent(portalInteractable.getHash(), portalInteractable.getTo()));
            }
            if(currentInteractable instanceof NPC){
                NPC npc = (NPC) currentInteractable;
                eventBus.post(new DisplayConsoleMessageEvent(npc.getInteractChatLine(), Color.yellow));
                if("VENDOR".equals(npc.getType())){
                    uiMode = UIMode.SHOP;
                }
            }
        }
        
        if(inputManager.isKeyBindPressed(KeyBindAction.C, true )) {
            eventBus.post(new TeleportCampEvent());
        }
    }
    
    public void setCharacterStatus(String status){
        if(playerContainer != null){
            playerContainer.setStatus(status);
        }
    }
    
    public void setMyCharacter(GCharacter player, String id) {
        playerContainer.setCurrentPlayer(player);
        entityManager.addNewObject(id,player);
        //eventBus.register(this);
    }
    public void setCurrentRoom(RoomDescription roomDescription, String myID){
        entityManager.clear(myID);
        world = roomDescription;
        collisionManager.setMap(roomDescription.getMapDescription().getFilename());
        for(DummyCharacter dp : roomDescription.getPlayers()){
            entityManager.addNewObject(dp.getId(),dp);
        }
        for(Enemy enemy : roomDescription.getEnemies()){
            enemy.initialize();
            entityManager.addNewObject(enemy.getHash(), enemy);
        }
        for(GameObjectWrapper ggaow : roomDescription.getGameobjects()) {
            GameObject actualGGAO = ggaow.getGObject();
            if(actualGGAO instanceof Portal) {
                Portal portalObj = (Portal) actualGGAO;
                portalObj.initialize();
                entityManager.addNewObject(ggaow.getHash(), portalObj);
            }
            if(actualGGAO instanceof LootBag) {
                LootBag lootBagObj = (LootBag) actualGGAO;
                lootBagObj.flushShape();
                entityManager.addNewObject(ggaow.getHash(), lootBagObj);
            }
            if(actualGGAO instanceof NPC) {
                NPC npcObj = (NPC) actualGGAO;
                npcObj.flushShape();
                entityManager.addNewObject(ggaow.getHash(), npcObj);
            }
        }

        
    }
    public StatContainer getMyStats(){
        return playerContainer.getCurrentPlayer().getStatManager().getStats();
    }
    
    public String getMyName(){
        return playerContainer.getCurrentPlayer().getName();
    }
    
    public Interactable getInteractable(){
        return interactableManager.getCurrentInteractable();
    }
    
    public int getMyLevel(){
        return playerContainer.getCurrentPlayer().getLevel();
    }
    
    public int getMyExp(){
        return playerContainer.getCurrentPlayer().getExperience();
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }
    
    public String getCharacterStatus(){
        return playerContainer.getPlayerStatus();
    }
    
    public String getMapName(){
        return world.getMapDescription().getFilename() + " - (" + world.getDifficulty() + ")";
    }
    
    public void setPlayerCoords(double x, double y){
        if(playerContainer.getCurrentPlayer() != null){
            playerContainer.getCurrentPlayer().setX(x);
            playerContainer.getCurrentPlayer().setY(y);
        }
        
    }

    public double getCameraAngle(){
        return playerContainer.getAngle();
    }

    public Equipments getCharacterEquipment() {
        return playerContainer.getCurrentPlayer().getEquipment();
    }
    
    public void characterStatusUpdate(ModifyCharacterEvent event) {
        playerContainer.characterStatusUpdate(event);
    }
    public Inventory getInventoryToRender() {
        return playerContainer.getCurrentPlayer().getInventory();
    }

    public LootBag getLootToRender() {
        if(interactableManager.getPersistentInteractable() != null && interactableManager.getPersistentInteractable() instanceof LootBag){
            return (LootBag) interactableManager.getPersistentInteractable();
        }
        if(interactableManager.getCurrentInteractable() == null 
                && interactableManager.getLastInteractable() != null 
                && interactableManager.getLastInteractable() instanceof LootBag){
            return (LootBag) interactableManager.getLastInteractable();
        }
        return null;
    }

    public void collidedWithInteractable(Interactable i) {
        interactableManager.collideWithInteractable(i);
    }

    public void cycleIM() {
        interactableManager.cycle();
    }

    private void checkNotInShop() {
        if(interactableManager.getPersistentInteractable() == null && uiMode == UIMode.SHOP){
            uiMode = UIMode.NORMAL;
        }
    }

    public UIMode getUiMode() {
        return uiMode;
    }
    
    public Currencies getCharacterCurrencies(){
        return playerContainer.getCurrentPlayer().getCurrencies();
    }
    public byte[][] getMinimap(){
        if(collisionManager.getMinimap() != null){
            int pX =  (int) Math.floor(playerContainer.getCurrentPlayer().getX() / 64);
            int pY =  (int) Math.floor(playerContainer.getCurrentPlayer().getY() / 64);
            return getMinimapView(collisionManager.getMinimap(),pX , pY, new ArrayList<>(entityManager.get(Portal.class)));
        }
        return null;
    }
    
    private byte[][] getMinimapView(byte[][] original, int startX, int startY, List<GameObject> gameObjects){
        final int minimapSize = 128;
        byte[][] result = new byte[minimapSize][minimapSize];
        int arrayPosStartX = startX - 64;
        int arrayPosStartY = startY - 64;
        for (int y = 0; y < minimapSize; y++) {
            for (int x = 0; x < minimapSize; x++) {
                int currentPosX = x + arrayPosStartX;
                int currentPosY = y + arrayPosStartY;
                boolean overBound = (currentPosX < 0 
                        || currentPosX >= original[0].length
                        || currentPosY < 0
                        || currentPosY >= original.length);
                if(!overBound){
                    result[y][x] = original[currentPosY][currentPosX];
                    if(startX == currentPosX && startY == currentPosY){
                        result[y][x] = 2;
                    }
                }
            }
        }
        for(GameObject obj : gameObjects){
            int currentPosX = (int) Math.floor(obj.getX() / 64) - startX + 64;
            int currentPosY = (int) Math.floor(obj.getY() / 64) - startY + 64;
                boolean overBound = (currentPosX < 0 
                        || currentPosX >= minimapSize
                        || currentPosY < 0
                        || currentPosY >= minimapSize);
                if(!overBound){
                    result[currentPosY][currentPosX] = 3;
                }
        }
        return result;
    }
    
}
