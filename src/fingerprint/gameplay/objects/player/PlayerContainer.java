package fingerprint.gameplay.objects.player;

import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.Direction;
import fingerprint.rendering.RenderingManager;
import fingerprint.rendering.SetScreenStartCoordinatesEvent;

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
            currentPlayer.setDeltaY(currentPlayer.getDeltaY() - currentPlayer.getSpeed());
            currentPlayer.setDirection(Direction.NORTH);
        }
        if(inputManager.isKeyBindDown(KeyBindAction.DOWN, true)){
            currentPlayer.setDeltaY(currentPlayer.getDeltaY() + currentPlayer.getSpeed());
            currentPlayer.setDirection(Direction.SOUTH);
        }
        if(inputManager.isKeyBindDown(KeyBindAction.RIGHT, true)){
            currentPlayer.setDeltaX(currentPlayer.getDeltaX() + currentPlayer.getSpeed());
            currentPlayer.setDirection(Direction.EAST);
        }
        if(inputManager.isKeyBindDown(KeyBindAction.LEFT, true)){
            currentPlayer.setDeltaX(currentPlayer.getDeltaX() - currentPlayer.getSpeed());
            currentPlayer.setDirection(Direction.WEST);
        }
        
        if(inputManager.isKeyBindDown(KeyBindAction.A, true)){
            //use
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
