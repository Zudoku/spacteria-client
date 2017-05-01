package fingerprint.gameplay.map.gameworld;

import java.util.Collection;
import java.util.logging.Logger;


import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.gameplay.items.Equipments;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.Enemy;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.gameplay.objects.lootbag.LootBag;
import fingerprint.gameplay.objects.player.DummyCharacter;
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.gameplay.objects.player.CharacterContainer;
import fingerprint.gameplay.objects.player.StatContainer;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.states.menu.enums.CharacterClass;


public class GameWorldContainer {
    private static final Logger logger = Logger.getLogger(GameWorldContainer.class.getName());
    
    @Inject private EntityManager entityManager;
    @Inject private CollisionManager collisionManager;
    private RoomDescription world;
    @Inject private CharacterContainer playerContainer;
    @Inject private EventBus eventBus;

    private LootBag lootToRender;
    
    private boolean thereWasLoot = false;
    
    public GameWorldContainer() {
        playerContainer = new CharacterContainer();

        //eventBus.register(this);
        
    }
    public void updateWorld(InputManager inputManager,int delta){
        playerContainer.updatePlayer(inputManager,delta);
        playerContainer.updateCamera();
        entityManager.updateProjectileDelta(delta);
        entityManager.updateEntities(delta,collisionManager);

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
        
    }
    public StatContainer getMyStats(){
        return playerContainer.getCurrentPlayer().getStatManager().getStats();
    }
    
    public String getMyName(){
        return playerContainer.getCurrentPlayer().getName();
    }
    
    public int getMyLevel(){
        return playerContainer.getCurrentPlayer().getLevel();
    }
    
    public CharacterClass getMyClass(){
        return playerContainer.getCurrentPlayer().getCharacterClass();
    }
    
    public int getMyExp(){
        return playerContainer.getCurrentPlayer().getExperience();
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


    public LootBag getLootToRender() {
        return lootToRender;
    }

    public void setLootToRender(LootBag lootToRender) {
        this.lootToRender = lootToRender;
    }

    public double getCameraAngle(){
        return playerContainer.getAngle();
    }

    public Equipments getCharacterEquipment() {
        return playerContainer.getCurrentPlayer().getEquipment();
    }

    public boolean isThereWasLoot() {
        return thereWasLoot;
    }

    public void setThereWasLoot(boolean thereWasLoot) {
        this.thereWasLoot = thereWasLoot;
    }
    
    
    

}
