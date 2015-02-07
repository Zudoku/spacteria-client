package fingerprint.states.events;

public class CloseProgramEvent {
    public boolean forceClose;
    public boolean save;
    public CloseProgramEvent(boolean forceClose, boolean save) {
        this.forceClose = forceClose;
        this.save = save;
    }
}
