package fingerprint.gameplay.objects;

public enum Direction {
    
    
    
    SOUTH(0,1),
    NORTH(0,-1),
    WEST(-1,0),
    EAST(1,0);
    
    private Direction(int deltaX,int deltaY){
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }
    
    private int deltaX;
    private int deltaY;
    
    
    public int getDeltaX() {
        return deltaX;
    }
    public int getDeltaY() {
        return deltaY;
    }
}
