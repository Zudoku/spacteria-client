package fingerprint.gameplay.map.gameworld;

import java.util.Collection;
import java.util.logging.Logger;


import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.Enemy;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.gameplay.objects.player.DummyPlayer;
import fingerprint.gameplay.objects.player.Player;
import fingerprint.gameplay.objects.player.PlayerContainer;
import fingerprint.gameplay.objects.player.StatContainer;
import fingerprint.mainmenus.serverlist.RoomDescription;
import fingerprint.states.menu.enums.CharacterClass;


public class GameWorldContainer {
    private static final Logger logger = Logger.getLogger(GameWorldContainer.class.getName());
    
    @Inject private EntityManager entityManager;
    @Inject private CollisionManager collisionManager;
    private RoomDescription world;
    @Inject private PlayerContainer playerContainer;
    @Inject private EventBus eventBus;
    
    public GameWorldContainer() {
        playerContainer = new PlayerContainer();

        //eventBus.register(this);
        
    }
    public void updateWorld(InputManager inputManager,int delta){
        playerContainer.updatePlayer(inputManager,delta);
        playerContainer.updateCamera();
        entityManager.updateProjectileDelta(delta);
        entityManager.updateEntities(delta,collisionManager);

    }
    public void setMyCharacter(Player player, String id) {
        playerContainer.setCurrentPlayer(player);
        entityManager.addNewObject(id,player);
        //eventBus.register(this);
    }
    public void setCurrentRoom(RoomDescription roomDescription){
        world = roomDescription;
        collisionManager.setMap(roomDescription.getMapDescription().getFilename());
        for(DummyPlayer dp : roomDescription.getPlayers()){
            entityManager.addNewObject(dp.getId(),dp);
        }
        for(Enemy enemy : roomDescription.getEnemies()){
            entityManager.addNewObject(java.util.UUID.randomUUID().toString(), enemy);
        }
        
    }
    public StatContainer getMyStats(){
        return playerContainer.getCurrentPlayer().getStatManager().getStats();
    }
    
    public String getMyName(){
        return playerContainer.getCurrentPlayer().getCharactername();
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
    
    
    
    public double getCameraAngle(){
        return playerContainer.getAngle();
    }
    

}
