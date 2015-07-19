package fingerprint.gameplay.objects.player;

public class PlayerBuilder {

    public static Player spawnGenericPlayer() {
        int AREAID = 0;
        Player player = new Player(64 * 600, 64 * 600,AREAID);
        player.setSpeed(0.3d);
        return player;
    }
}
