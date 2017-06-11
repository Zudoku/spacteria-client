package fingerprint.gameplay.objects.events.gui;

public class EnterPortalEvent {

    private String hash;
    private int to;

    public EnterPortalEvent(String hash, int to) {
        this.hash = hash;
        this.to = to;
    }
}
