package fingerprint.gameplay.objects.player;

import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.interaction.UseItemEvent;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.Direction;
import fingerprint.rendering.RenderingManager;
import fingerprint.rendering.SetScreenStartCoordinatesEvent;
import fingerprint.states.events.CloseProgramEvent;
import fingerprint.states.events.SaveAndExitWorldEvent;

public class PlayerContainer {
    private static final Logger logger = Logger.getLogger(PlayerContainer.class.getName());
    
    @Inject private EventBus eventBus;
    @Inject private CollisionManager collisionManager;
    
    public transient static final int playerCollisionWidth = 62;
    public transient static final int playerCollisionHeight = 62;
    public transient static final int playerRenderWidth = 64;
    public transient static final int playerRenderHeight = 64;
    
    
    
    
    
    private Player currentPlayer;
    
    public PlayerContainer() {
        
    }
    public void updatePlayer(InputManager inputManager, int delta){
        if(currentPlayer == null){
            return;
        }
        updateInput(inputManager,delta);
    }
    
    private void updateInput(InputManager inputManager,int delta) {
        if(inputManager.isKeyBindDown(KeyBindAction.UP, true)){
            switch (currentPlayer.getState()) {
            case PLAYING:
                currentPlayer.setDeltaY(currentPlayer.getDeltaY() - currentPlayer.getSpeed());
                currentPlayer.setDirection(Direction.NORTH);
                break;
                
            case INVENTORY:
                
                break;

            default:
                break;
            }
            
        }
        if(inputManager.isKeyBindDown(KeyBindAction.DOWN, true)){
            switch (currentPlayer.getState()) {
            case PLAYING:
                currentPlayer.setDeltaY(currentPlayer.getDeltaY() + currentPlayer.getSpeed());
                currentPlayer.setDirection(Direction.SOUTH);
                break;
                
            case INVENTORY:
                
                break;

            default:
                break;
            }
            
        }
        if(inputManager.isKeyBindDown(KeyBindAction.RIGHT, false)){
            switch (currentPlayer.getState()) {
            case PLAYING:
                currentPlayer.setDeltaX(currentPlayer.getDeltaX() + currentPlayer.getSpeed());
                currentPlayer.setDirection(Direction.EAST);
                break;
                
            case INVENTORY:
                if(inputManager.isKeyBindPressed(KeyBindAction.RIGHT, true)){
                    currentPlayer.getInventory().right();
                }
                break;

            default:
                break;
            }
            
        }
        if(inputManager.isKeyBindDown(KeyBindAction.LEFT, false)){
            switch (currentPlayer.getState()) {
            case PLAYING:
                currentPlayer.setDeltaX(currentPlayer.getDeltaX() - currentPlayer.getSpeed());
                currentPlayer.setDirection(Direction.WEST);
                break;
                
            case INVENTORY:
                if(inputManager.isKeyBindPressed(KeyBindAction.LEFT, true)){
                    currentPlayer.getInventory().left();
                }
                break;

            default:
                break;
            }
            
        }
        
        if(inputManager.isKeyBindPressed(KeyBindAction.A, true)){
            //use
            switch (currentPlayer.getState()) {
            case PLAYING:
                eventBus.post(new UseItemEvent(currentPlayer.getInventory().getOnHand(), currentPlayer.getUseTilePosition()[0], currentPlayer.getUseTilePosition()[1]));
                break;
                
            case INVENTORY:
                currentPlayer.getInventory().select();
                break;

            default:
                break;
            }
            
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.C, true)){
            //Inventory toggle
            switch (currentPlayer.getState()) {
            case PLAYING:
                currentPlayer.setState(PlayerState.INVENTORY);
                break;
                
            case INVENTORY:
                currentPlayer.setState(PlayerState.PLAYING);
                break;

            default:
                break;
            }
        }
        
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT, true)){
            eventBus.post(new SaveAndExitWorldEvent());
        }
        
    }
    
    
    public void updateCamera(){
        int offsetX =  -(RenderingManager.unScaledScreenWidth/2 - playerCollisionWidth/2 - 1);
        int offsetY =  -(RenderingManager.unScaledScreenHeight/2 - playerCollisionHeight/2 - 1);
        double x = currentPlayer.getX();
        double y = currentPlayer.getY();
        eventBus.post(new SetScreenStartCoordinatesEvent(x + offsetX, y + offsetY));
    }
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
}
