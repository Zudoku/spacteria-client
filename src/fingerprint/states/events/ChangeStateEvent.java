package fingerprint.states.events;

public class ChangeStateEvent {
    private int fromState;
    private int toState;
    public ChangeStateEvent(int from, int to) {
        this.fromState = from;
        this.toState = to;
    }
    public int getFromState() {
        return fromState;
    }
    public int getToState() {
        return toState;
    }
}
