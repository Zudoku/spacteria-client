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
import fingerprint.gameplay.objects.interact.LootBag;
import fingerprint.gameplay.objects.interact.NPC;
import fingerprint.gameplay.objects.interact.Portal;
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.gameplay.objects.projectiles.Projectile;



import fingerprint.rendering.map.TilemapRenderer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMapPlus;

@Singleton
public class CollisionManager {
    private static final Logger logger = Logger.getLogger(CollisionManager.class.getName());
    private TiledMapPlus map;

    private final EntityManager entityManager;
    
    private final List<Integer> blockingTiles = new ArrayList<>(Arrays.asList(new Integer[]{
        1,2,4,6,7,8,10,11,15,16,18,22,23,24
    }));
    
    
    private String filename = "";
    
    private int zoneAmount = 40;
    
    private Zone[][] zones;
    
    private byte[][] minimap;
    
    private ExecutorService executor;

    
    @Inject
    public CollisionManager(EntityManager entityManager) {
        this.zones = new Zone[zoneAmount][zoneAmount];
        for(int y=0; y < zoneAmount; y++){
            for(int x=0; x < zoneAmount; x++){
                this.zones[y][x] = new Zone(new ArrayList<>(), x, y);
            }
        }
        this.entityManager = entityManager;
        executor = Executors.newFixedThreadPool(5);
    }
    
    public void setMap(String filename) {
        this.filename = filename;
        this.map = null;
        for(int y=0; y < zoneAmount; y++){
            for(int x=0; x < zoneAmount; x++){
                this.zones[y][x].clear();
            }
        }
    }

    public TiledMapPlus getMap() {
        return map;
    }
    public void checkCollision() {
        executor.execute(() -> {
            for (Projectile projectile : entityManager.get(Projectile.class)) {
                if (projectile.getTeam() == Projectile.PLAYER_PROJECTILE_SIDE) {
                    for (Enemy enemy : entityManager.get(Enemy.class)) {
                        if (enemy.isColliding(projectile)) {
                            enemy.onCollision(projectile);
                            projectile.onCollision(enemy);
                        }
                    };
                } else if (projectile.getTeam() == Projectile.ENEMY_PROJECTILE_SIDE) {
                    for (GCharacter me : entityManager.get(GCharacter.class)) {
                        if (projectile.isColliding(me) && me.getStatus().equals("ALIVE")) {
                            projectile.onCollision(me);
                            me.onCollision(projectile);
                        }
                    };
                }
            };
        });

        entityManager.get(GCharacter.class).stream().forEach(me -> {
            entityManager.get(NPC.class).stream().forEach(npc -> {
                if(npc.isColliding(me)){
                    npc.onCollision(me);
                    me.onCollision(npc);
                }
            });
            
            entityManager.get(Portal.class).stream().forEach(portal -> {
                if(portal.isColliding(me)){
                    portal.onCollision(me);
                    me.onCollision(portal);
                }
            });
            
            entityManager.get(LootBag.class).stream().forEach(lootbag -> {
                if(lootbag.isColliding(me)){
                    lootbag.onCollision(me);
                    me.onCollision(lootbag);
                }
            });
        });
    }
    
    public void checkIfNeedInit() {
        if (!filename.equals("") && map == null) {
            try {
                map = new TiledMapPlus(FileUtil.TILEDMAPS_PATH + "/" + filename + FileUtil.TILEDMAP_FILE_EXTENSION);
                minimap = new byte[map.getHeight()][map.getWidth()];
                for (int y = 0; y < map.getHeight(); y++) {
                    for (int x = 0; x < map.getWidth(); x++) {
                        if (!blockingTiles.contains(map.getTileId(x, y, 0))) {
                            minimap[y][x] = 1;
                        }

                    }
                }
            } catch (SlickException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean collideWithTerrain(Shape collider){
        if(map == null){
            checkIfNeedInit();
        }
        if(collider == null){
            return false;
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

    public byte[][] getMinimap() {
        return minimap;
    }

    public String updateEnemyLocation(double x, double y, Enemy obj) {
        int zoneCordX = getZoneCordX(x);
        int zoneCordY = getZoneCordY(y);
        Zone newZone = zones[zoneCordY][zoneCordX];
        if(!newZone.getEnemies().contains(obj)){
            newZone.addEntity(obj);
            
            int oldZoneX = Integer.parseInt(obj.getZone().split("-")[0]);
            int oldZoneY = Integer.parseInt(obj.getZone().split("-")[1]);
            Zone oldZone = zones[oldZoneY][oldZoneX];
            oldZone.removeEntity(obj);
        }
        
        
        return ("" + zoneCordX + "-" + zoneCordY);
    }
    
    public String getZoneStringCoord(double x, double y){
        int zoneCordX = getZoneCordX(x);
        int zoneCordY = getZoneCordY(y);
        return ("" + zoneCordX + "-" + zoneCordY);
    }
    
    public int getZoneCordX (double x){
        double buffer = (double)(map.getWidth() * TilemapRenderer.tileSize) / (double)(zoneAmount);
        if(x < 0 || x > map.getWidth() * TilemapRenderer.tileSize){
            return 0;
        }
        return (int) Math.floor(x / buffer);
    }
    
    public int getZoneCordY (double y){
        double buffer = (double)(map.getHeight() * TilemapRenderer.tileSize) / (double)(zoneAmount);
        if(y < 0 || y > map.getHeight() * TilemapRenderer.tileSize){
            return 0;
        }
        return (int) Math.floor(y / buffer);
    }
}
