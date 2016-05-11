package fingerprint.gameplay.objects.player;

public class PlayerBuilder {

    public static Player spawnGenericPlayer() {
        int AREAID = 0;
        Player player = new Player(64 * 650, 64 * 615,AREAID);
        player.setSpeed(20d);
        return player;
    }
}
