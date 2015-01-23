package fingerprint.gameplay.time;

public class Clock {
    private int dayLength;
    private int currentTime;
    public Clock() {
        
    }
    
    public void addCurrentTime(){
        currentTime++;
        if(currentTime >= dayLength){
            
        }
    }
    public int getCurrentTime() {
        return currentTime;
    }
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }
    public void setDayLength(int dayLength) {
        this.dayLength = dayLength;
    }
    public int getDayLength() {
        return dayLength;
    }
}
