package fingerprint.gameplay.map;

public class ChangeGameAreaEvent {

    private int changeToID ;
    public ChangeGameAreaEvent(int id) {
        this.changeToID =id;
    }
    public int getChangeToID() {
        return changeToID;
    }
}
