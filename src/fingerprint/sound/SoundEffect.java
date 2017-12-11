package fingerprint.sound;

public enum SoundEffect {
    
    BLOB("blob.ogg"),
    CHARHIT("playerhit.ogg"),
    TELEPORT("teleport.ogg"),
    SHOOT("shoot.ogg");
    
    private String filename;
    
    private SoundEffect(String filename){
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
    
    

}
