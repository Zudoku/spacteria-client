/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.mainmenus.serverlist;

import fingerprint.gameplay.objects.Enemy;
import fingerprint.gameplay.objects.GameObjectWrapper;
import fingerprint.gameplay.objects.player.DummyCharacter;
import fingerprint.gameplay.objects.projectiles.Projectile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arska
 */
public class RoomDescription {
    private String name;
    private List<DummyCharacter> players = new ArrayList<>();
    private int difficulty = 1;
    private MapDescription mapDescription;
    private GameObjectWrapper[] gameobjects;
    private Enemy[] enemies;
    private Projectile[] projectiles;

    /**
     * @return the roomname
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the roomname to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * @param difficulty the difficulty to set
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @return the map
     */
    public MapDescription getMapDescription() {
        return mapDescription;
    }

    /**
     * @param mapDescription the map to set
     */
    public void setMapDescription(MapDescription mapDescription) {
        this.mapDescription = mapDescription;
    }

    /**
     * @return the players
     */
    public List<DummyCharacter> getPlayers() {
        return players;
    }

    /**
     * @param players the players to set
     */
    public void setPlayers(List<DummyCharacter> players) {
        this.players = players;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public GameObjectWrapper[] getGameobjects() {
        return gameobjects;
    }

    public Projectile[] getProjectiles() {
        return projectiles;
    }

    public void setEnemies(Enemy[] enemies) {
        this.enemies = enemies;
    }

    public void setGameobjects(GameObjectWrapper[] gameobjects) {
        this.gameobjects = gameobjects;
    }

    public void setProjectiles(Projectile[] projectiles) {
        this.projectiles = projectiles;
    }
    
    
    
}
