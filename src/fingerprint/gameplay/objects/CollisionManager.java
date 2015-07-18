package fingerprint.gameplay.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.google.inject.Inject;

import fingerprint.gameplay.map.FunctionalMap;
import fingerprint.gameplay.map.blocks.Block;
import fingerprint.gameplay.map.blocks.BlockManager;
import fingerprint.gameplay.map.generation.GameWorldBuilder;
import fingerprint.rendering.map.TilemapRenderer;


public class CollisionManager {
    private static final Logger logger = Logger.getLogger(CollisionManager.class.getName());
    private FunctionalMap map;

    private EntityManager entityManager;
    private BlockManager blockManager;
    
    @Inject
    public CollisionManager(BlockManager blockManager,EntityManager entityManager) {
        this.blockManager = blockManager;
        this.entityManager = entityManager;
    }
    public void setMap(FunctionalMap map) {
        this.map = map;
    }

    public void checkCollision(){
        //TODO: optimize this 
        for(CollidingObject object_1 : entityManager.get(CollidingObject.class)){
            for(CollidingObject object_2 : entityManager.get(CollidingObject.class)){
                if(object_1 != object_2){
                    if(object_1.isColliding(object_2)){
                        object_1.onCollision(object_2);
                    }
                }
            }
        }
    }
    public boolean collideWithTerrain(Shape collider){
        List<int[][]> arrayPoints = new ArrayList<>();
        for(int i = 0; i < collider.getPointCount() ; i ++){
            float[] point = collider.getPoint(i);
            int arrayPosX = (int)Math.floor(((int)Math.round(point[0]))/TilemapRenderer.tileSize);
            int arrayPosY = (int)Math.floor(((int)Math.round(point[1]))/TilemapRenderer.tileSize);
            int[][] arrayPos = new int[2][1];
            arrayPos[0][0] = arrayPosX;
            arrayPos[1][0] = arrayPosY;
            if(!arrayPoints.contains(arrayPos)){
                arrayPoints.add(arrayPos);
            }
        }

        for (int[][] arrayPos : arrayPoints) {
            Block foundBlock = blockManager
                    .getBlock(map.getData()[arrayPos[0][0]][arrayPos[1][0]]);
            if (foundBlock == null) {
                continue;
            }
            if (foundBlock.isBlocking()) {
                int rectangleX = arrayPos[0][0] * TilemapRenderer.tileSize;
                int rectangleY = arrayPos[1][0] * TilemapRenderer.tileSize;
                Rectangle blockRectangle = new Rectangle(rectangleX,
                        rectangleY, TilemapRenderer.tileSize,
                        TilemapRenderer.tileSize);
                if (blockRectangle.intersects(collider)) {
                    return true;
                }
            } else {
                continue;
            }
        }

        return false;
    }
    
}
