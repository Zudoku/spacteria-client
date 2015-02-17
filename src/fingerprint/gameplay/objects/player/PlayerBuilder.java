package fingerprint.gameplay.objects.player;

public class PlayerBuilder {

    public static Player spawnGenericPlayer() {
        int AREAID = 0;
        Player player = new Player(1000, 1000,AREAID);
        player.setSpeed(0.3d);
        return player;
    }
}
