package fingerprint.gameplay.objects;


public class GameObject {

    protected transient int ID;
    protected double x;
    protected double y;
    
    public GameObject(double initX, double initY) {
        this.x = initX;
        this.y = initY;
    }
    
    public void draw() {

    }
}
