package fingerprint.gameplay.objects.player;

import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.rendering.RenderingManager;
import fingerprint.rendering.SetScreenStartCoordinatesEvent;
import fingerprint.states.events.SaveAndExitWorldEvent;

public class PlayerContainer {
    private static final Logger logger = Logger.getLogger(PlayerContainer.class.getName());
    
    @Inject private EventBus eventBus;
    @Inject private CollisionManager collisionManager;
    
    public transient static final int playerCollisionWidth = 32;
    public transient static final int playerCollisionHeight = 32;
    public transient static final int playerRenderWidth = 64;
    public transient static final int playerRenderHeight = 64;
    
    
    private double angle = 45;
    
    
    private Player currentPlayer;
    
    public PlayerContainer() {
        
    }
    public void updatePlayer(InputManager inputManager, int delta){
        if(currentPlayer == null){
            return;
        }
        updateInput(inputManager,delta);
        currentPlayer.move(delta, collisionManager);
    }
    
    private void updateInput(InputManager inputManager,int delta) {
        double angleInRadians = (angle / 360) * (2 * Math.PI);
        double scaler = ((double)getStats().getSpeed() * delta / 20d);
        
        currentPlayer.setDeltaX(0);
        currentPlayer.setDeltaY(0);
        
        if(inputManager.isKeyBindDown(KeyBindAction.UP, true)){
            
            double deltaX = -Math.sin(angleInRadians);
            double deltaY = -Math.cos(angleInRadians);
            
            //Apply speed buffs
            
            
            deltaX *= scaler;
            deltaY *= scaler;
            
            currentPlayer.setDeltaX(currentPlayer.getDeltaX() + deltaX);
            currentPlayer.setDeltaY(currentPlayer.getDeltaY() + deltaY);
        }
        if(inputManager.isKeyBindDown(KeyBindAction.DOWN, true)){
            double deltaX = Math.sin(angleInRadians);
            double deltaY = Math.cos(angleInRadians);
            
            
            //Apply speed buffs

            
            deltaX *= scaler;
            deltaY *= scaler;
            
            currentPlayer.setDeltaX(currentPlayer.getDeltaX() + deltaX);
            currentPlayer.setDeltaY(currentPlayer.getDeltaY() + deltaY);
        }
        if(inputManager.isKeyBindDown(KeyBindAction.RIGHT, false)){
            double deltaY = -Math.sin(angleInRadians);
            double deltaX = Math.cos(angleInRadians);
            
            //Apply speed buffs
            
            deltaX *= scaler;
            deltaY *= scaler;
            
            currentPlayer.setDeltaX(currentPlayer.getDeltaX() + deltaX);
            currentPlayer.setDeltaY(currentPlayer.getDeltaY() + deltaY);
        }
        if(inputManager.isKeyBindDown(KeyBindAction.LEFT, false)){
            double deltaY = Math.sin(angleInRadians);
            double deltaX = -Math.cos(angleInRadians);
            
            //Apply speed buffs
            
            deltaX *= scaler;
            deltaY *= scaler;
            
            currentPlayer.setDeltaX(currentPlayer.getDeltaX() + deltaX);
            currentPlayer.setDeltaY(currentPlayer.getDeltaY() + deltaY);
        }
        if(inputManager.isKeyBindDown(KeyBindAction.A, false)){
            rotateCameraRight(delta);
        }
        if(inputManager.isKeyBindDown(KeyBindAction.B, false)){
            rotateCameraLeft(delta);
        }
        
        
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT, true)){
            eventBus.post(new SaveAndExitWorldEvent());
        }
        
    }
    private void rotateCameraLeft(int delta){
        double amount = ((double)delta) * 12d / 60d;
        angle -= amount;
        if(angle < 0){
            angle = 360 - angle;
        }
        
    }
    private void rotateCameraRight(int delta){
        double amount = ((double)delta) * 12d / 60d;
        angle += amount;
        if(angle > 360){
            angle = angle - 360;
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
        this.currentPlayer.init();
    }

    public double getAngle() {
        return angle;
    }
    
    private StatContainer getStats(){
        return currentPlayer.getStatManager().getStats();
    }
}
