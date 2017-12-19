package fingerprint.gameplay.objects.player;

import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.gameplay.items.GameItem;
import fingerprint.gameplay.items.GameItemAttribute;
import fingerprint.gameplay.items.ItemType;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.EntityManager;
import fingerprint.gameplay.objects.events.ModifyCharacterEvent;
import fingerprint.gameplay.objects.projectiles.Projectile;
import fingerprint.gameplay.objects.projectiles.ProjectileImage;
import fingerprint.gameplay.objects.projectiles.SpawnProjectileEvent;
import fingerprint.networking.events.UpdatePositionEvent;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.rendering.gui.event.SetScreenStartCoordinatesEvent;
import fingerprint.sound.PlaySoundEvent;
import fingerprint.sound.SoundEffect;
import fingerprint.states.events.SaveAndExitWorldEvent;
import java.util.Arrays;
import org.newdawn.slick.geom.Point;

public class CharacterContainer {
    private static final Logger logger = Logger.getLogger(CharacterContainer.class.getName());
    
    @Inject private EventBus eventBus;
    @Inject private CollisionManager collisionManager;
    @Inject private EntityManager entityManager;
    
    public transient static final int playerCollisionWidth = 32;
    public transient static final int playerCollisionHeight = 32;
    public transient static final int playerRenderWidth = 64;
    public transient static final int playerRenderHeight = 64;
    
    private double angle = 0;
    
    private int lastPosOnServerX = 0;
    private int lastPosOnServerY = 0;
    private final int updateTreshhold = 2;
    
    private final transient AttackManager attackManager;
    
    
    private GCharacter currentPlayer;
    
    public CharacterContainer() {
        attackManager = new AttackManager();
    }
    public void updatePlayer(InputManager inputManager, int delta){
        if(currentPlayer == null){
            return;
        }
        updateInput(inputManager,delta);
        currentPlayer.move(delta, collisionManager);
        
        int currX = (int) Math.floor(currentPlayer.getX());
        int currY = (int) Math.floor(currentPlayer.getY());
        
        int deltaX = Math.abs(lastPosOnServerX - currX);
        int deltaY = Math.abs(lastPosOnServerY - currY);
        
        if(deltaX + deltaY > updateTreshhold){
            lastPosOnServerX = currX;
            lastPosOnServerY = currY;
            eventBus.post(new UpdatePositionEvent(currX, currY));
        }
        
    }
    
    int getAngleI(){
        return (int) angle;
    }

    public String getPlayerStatus() {
        if(currentPlayer == null){
            return "";
        }
        return currentPlayer.getStatus();
    }
    
    public void setStatus(String status){
        if(currentPlayer != null){
            currentPlayer.setStatus(status);
        }
    }
    
    private void updateInput(InputManager inputManager,int delta) {
        double angleInRadians = ((double)getAngle() / 360) * (2 * Math.PI);
        double scaler = ((double)getStats().getSpeed() * delta / 200d);
        
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
        
        if(inputManager.getInput().isMouseButtonDown(0)){
            if(attackManager.canAttack()){
                //Spawn projectile
                attackManager.attack();
                attack(inputManager);
            }
        }
        
        
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT, true)){
            eventBus.post(new SaveAndExitWorldEvent());
        }
        
    }
    private void rotateCameraLeft(int delta){
        double amount = ((double)delta) * 12d / 60d;
        angle -= amount;
        if(angle < 0){
            angle = 359d - Math.abs(angle);
        }
        angle = angle % 360;
        
    }
    private void rotateCameraRight(int delta){
        double amount = ((double)delta) * 12d / 60d;
        angle += amount;
        if(angle > 360){
            angle = angle - 360;
        }
        angle = angle % 360;
    }
    
    
    public void updateCamera(){
        int offsetX =  -(RenderingManager.unScaledGamePlayWidth/2 - playerCollisionWidth/2 - 1);
        int offsetY =  -(RenderingManager.unScaledGamePlayHeight/2 - playerCollisionHeight/2 - 1);
        double x = currentPlayer.getX();
        double y = currentPlayer.getY();
        eventBus.post(new SetScreenStartCoordinatesEvent(x + offsetX, y + offsetY));
    }
    public void setCurrentPlayer(GCharacter currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.currentPlayer.init();
        attackManager.setDexterity(currentPlayer.getStatManager().getStats().getDexterity());
    }

    public double getAngle() {
        return angle % 360;
    }
    
    private StatContainer getStats(){
        return currentPlayer.getStatManager().getStats();
    }
    //Spawn the projectile
    private void attack(InputManager inputManager){
        double mouseX = inputManager.getInput().getAbsoluteMouseX();
        double mouseY = inputManager.getInput().getAbsoluteMouseY();
        //Players position in screen
        Point playerPosition = new Point((RenderingManager.unScaledGamePlayWidth / 2), (RenderingManager.unScaledGamePlayHeight / 2));
        //Angle 0 position
        Point zeroPosition = new Point(RenderingManager.unScaledGamePlayWidth / 2,RenderingManager.unScaledGamePlayHeight);
        //Mouse offset to player
        Point mousePosition = new Point((float) (mouseX), (float) (mouseY));
        
        
        float projectileAngleInRadians = (float) angleBetweenTwoPointsWithFixedPoint(zeroPosition.getX(), zeroPosition.getY(), mousePosition.getX(), mousePosition.getY(), playerPosition.getX(), playerPosition.getY());
        float projectileAngle = (float) Math.toDegrees(projectileAngleInRadians) + 180f;
        projectileAngle += (float)angle;
        projectileAngle  = projectileAngle % 360;
        
        double projectileStartX = this.currentPlayer.getX() + (playerCollisionWidth / 2);
        double projectileStartY = this.currentPlayer.getY() + (playerCollisionHeight / 2);
        
        double projectileSpeed = 130d;
        double projectileMaxDistance = 500d;
        
        ProjectileImage image = ProjectileImage.BASIC; 
        
        if(currentPlayer.getEquipment().getItem(ItemType.WEAPON.ordinal()) != null){
            GameItem weapon = currentPlayer.getEquipment().getItem(ItemType.WEAPON.ordinal());
            for(GameItemAttribute attribute : Arrays.asList(weapon.getAttributes())){
                if(attribute.getAttributeid() == 10){
                    image = ProjectileImage.values()[attribute.getAttributevalue()];
                }
                if(attribute.getAttributeid() == 11){
                    projectileSpeed = attribute.getAttributevalue();
                }
                if(attribute.getAttributeid() == 12){
                    projectileMaxDistance = attribute.getAttributevalue();
                }
            }
        }

        String guid = java.util.UUID.randomUUID().toString();
        Projectile createdProjectile = new Projectile(projectileAngle,projectileSpeed,projectileMaxDistance, projectileStartX,projectileStartY);
        createdProjectile.setGuid(guid);
        createdProjectile.setTeam(Projectile.PLAYER_PROJECTILE_SIDE);
        createdProjectile.setImage(image);
        entityManager.addNewObject(guid, createdProjectile);
        //Launch event to server
        eventBus.post(new SpawnProjectileEvent(createdProjectile));
        eventBus.post(new PlaySoundEvent(SoundEffect.SHOOT, true));
        
        
        
    }
    
    public static double angleBetweenTwoPointsWithFixedPoint(double point1X, double point1Y, 
        double point2X, double point2Y, 
        double fixedX, double fixedY) {

        double angle1 = Math.atan2(point1Y - fixedY, point1X - fixedX);
        double angle2 = Math.atan2(point2Y - fixedY, point2X - fixedX);
    
        return angle1 - angle2; 
    }

    public GCharacter getCurrentPlayer() {
        return currentPlayer;
    }

    public void characterStatusUpdate(ModifyCharacterEvent event) {
        GCharacter newStatus = event.getCharacter();
        if(currentPlayer != null && currentPlayer.getUniqueid() == newStatus.getUniqueid()) {
            currentPlayer.setEquipment(newStatus.getEquipment());
            currentPlayer.setExperience(newStatus.getExperience());
            currentPlayer.setInventory(newStatus.getInventory());
            currentPlayer.setLevel(newStatus.getLevel());
            currentPlayer.setCurrencies(newStatus.getCurrencies());

            if(event.getStats() != null) {
                if(currentPlayer.getStatManager().getStats().getHealth() > event.getStats().getHealth()){
                    //Play sound
                    eventBus.post(new PlaySoundEvent(SoundEffect.CHARHIT, true));
                }
                currentPlayer.getStatManager().setStats(event.getStats());
                attackManager.setDexterity(event.getStats().getDexterity());
            }
            
            if(!event.getCharacter().getStatus().equals(currentPlayer.getStatus())){
                currentPlayer.setStatus(event.getCharacter().getStatus());
            }
        }
    }
    
    
    
    
}
