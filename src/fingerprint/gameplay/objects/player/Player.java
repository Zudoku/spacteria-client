package fingerprint.gameplay.objects.player;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import fingerprint.gameplay.items.Inventory;
import fingerprint.gameplay.map.FunctionalMap;
import fingerprint.gameplay.map.generation.village.VillageGenerator;
import fingerprint.gameplay.objects.CollidingObject;
import fingerprint.gameplay.objects.CollisionManager;
import fingerprint.gameplay.objects.Direction;
import fingerprint.rendering.RenderingManager;
import fingerprint.rendering.map.TilemapRenderer;


public class Player extends CollidingObject{
    private transient static final Logger logger = Logger.getLogger(Player.class.getName());

    private transient final static double playerSpeed = 5.3d;
    private transient PlayerState state = PlayerState.PLAYING;
    private Direction direction;
    private Inventory inventory;
    
    
    public Player() {
        super(0,0,playerSpeed,0, new Rectangle(0,0, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight));
    }
    
    public Player(double initX, double initY,int areaID) {
        super(initX,initY,playerSpeed,areaID,new Rectangle((float)initX,(float) initY, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight));
        setCollideToTerrain(true);
        direction = Direction.NORTH;
        inventory = new Inventory();
    }
    
    @Override
    public void draw(Graphics graphics) {
        drawDebug(graphics);
    }
    @Override
    public void drawDebug(Graphics graphics){
        double[] coordinates = getDrawingCoordinates();
        //Draw the collision rectangle
        graphics.setColor(Color.pink);
        graphics.fillRect((float)coordinates[0] + 2,(float)coordinates[1] + 2, PlayerContainer.playerCollisionWidth,PlayerContainer.playerCollisionHeight);
        graphics.setColor(Color.red);
        graphics.fillRect((float)coordinates[0] + 6,(float)coordinates[1] + 6, PlayerContainer.playerCollisionWidth - 8,PlayerContainer.playerCollisionHeight -8);
        //Draw the use rectangle
        
        //Figure out players position
        int[] useTilePositions = getUseTilePosition();
        double drawingCoordX = ((double)(useTilePositions[0] * TilemapRenderer.tileSize)) - RenderingManager.getScreenStartX();
        double drawingCoordY = ((double)(useTilePositions[1] * TilemapRenderer.tileSize)) - RenderingManager.getScreenStartY();
        
        graphics.setColor(Color.black);
        graphics.fillRect((float)drawingCoordX, (float)drawingCoordY, TilemapRenderer.tileSize, TilemapRenderer.tileSize);

    }
    @Override
    public void move(int delta, CollisionManager collisionManager) {
        // TODO Auto-generated method stub
        super.move(delta, collisionManager);
    }
    
    public Direction getDirection() {
        return direction;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    public int[] getUseTilePosition(){
        int tileX = (int)Math.round(getX()/ TilemapRenderer.tileSize);
        int tileY = (int)Math.round(getY()/ TilemapRenderer.tileSize);
        
        tileX += direction.getDeltaX();
        tileY += direction.getDeltaY();
        
        if(tileX < 0){
            tileX = 0;
        }
        if(tileX > FunctionalMap.SIZE){
            tileX = FunctionalMap.SIZE;
        }
        if(tileY < 0){
            tileY = 0;
        }
        if(tileY > FunctionalMap.SIZE){
            tileY = FunctionalMap.SIZE;
        }
        
        return new int[]{tileX,tileY};
        
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public PlayerState getState() {
        return state;
    }
    public void setState(PlayerState state) {
        this.state = state;
    }

}
