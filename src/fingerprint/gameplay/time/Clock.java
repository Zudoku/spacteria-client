package fingerprint.gameplay.time;

public class Clock {
    private double time;
    public Clock() {
        time = 0.0d;
    }
    
    public void addTime(double passed){
        time += passed;
    }
    
    public double getTime() {
        return time;
    }
    public void setTime(double time) {
        this.time = time;
    }
    
}
