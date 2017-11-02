package fingerprint.gameplay.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fingerprint.inout.FileUtil;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.google.inject.Inject;
import com.google.inject.Singleton;



import fingerprint.rendering.map.TilemapRenderer;
import java.util.Arrays;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMapPlus;

@Singleton
public class CollisionManager {
    private static final Logger logger = Logger.getLogger(CollisionManager.class.getName());
    private TiledMapPlus map;

    private EntityManager entityManager;
    
    private List<Integer> blockingTiles = new ArrayList<>(Arrays.asList(new Integer[]{
        1,2,4,6,7,8,10,11,15,16,18
    }));
    
    
    private String filename = "";
    
    private int zoneAmount = 10;
    
    private ArrayList<GameObject>[][] zones;

    
    @Inject
    public CollisionManager(EntityManager entityManager) {
        this.zones = new ArrayList[zoneAmount][zoneAmount];
        
        this.entityManager = entityManager;
    }
    
    public void setMap(String filename) {
        this.filename = filename;
        this.map = null;
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
        if(map == null){
            if(filename.equals("")){
                return true;
            }else {
                try {
                    map = new TiledMapPlus(FileUtil.TILEDMAPS_PATH + "/" + filename + FileUtil.TILEDMAP_FILE_EXTENSION);
                } catch (SlickException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        List<int[][]> arrayPoints = new ArrayList<>();
        for(int i = 0; i < collider.getPointCount() ; i ++){
            float[] point = collider.getPoint(i);
            //
            if(point[0] < 0 || point[1] < 0) {
                return true;
            }
            int arrayPosX = (int)Math.floor(((int)Math.floor(point[0]))/TilemapRenderer.tileSize);
            int arrayPosY = (int)Math.floor(((int)Math.floor(point[1]))/TilemapRenderer.tileSize);
            int[][] arrayPos = new int[2][1];
            arrayPos[0][0] = arrayPosX;
            arrayPos[1][0] = arrayPosY;
            if(!arrayPoints.contains(arrayPos)){
                arrayPoints.add(arrayPos);
            }
        }

        for (int[][] arrayPos : arrayPoints) {
            if(arrayPos[0][0] >= map.getWidth() || arrayPos[1][0] >= map.getHeight() || arrayPos[0][0] < 0 || arrayPos[1][0] < 0) {
                return true;
            }
            int tileID = map.getTileId(arrayPos[0][0], arrayPos[1][0], 0);
            if (blockingTiles.contains((Integer) tileID)) {
                int rectangleX = arrayPos[0][0] * TilemapRenderer.tileSize;
                int rectangleY = arrayPos[1][0] * TilemapRenderer.tileSize;
                Rectangle blockRectangle = new Rectangle(rectangleX,
                        rectangleY, TilemapRenderer.tileSize,
                        TilemapRenderer.tileSize);
                if (blockRectangle.intersects(collider)) {
                    //logger.log(Level.INFO, "collides", new Object[]{});
                    return true;
                }
            } else {
                continue;
            }
        }

        return false;
    }
    
}
