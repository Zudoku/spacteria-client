package fingerprint.states.events;

public class PlayerOutlookEvent {
    private boolean onWorld;
    public PlayerOutlookEvent(boolean onWorld) {
        this.onWorld = onWorld;
    }
    public boolean isOnWorld() {
        return onWorld;
    }
}
