package fingerprint.rendering;

public class SetScreenStartCoordinatesEvent {
    public double newX;
    public double newY;
    public SetScreenStartCoordinatesEvent(double x,double y) {
        this.newX = x;
        this.newY = y;
    }
}
