package fingerprint.sound;

public class PlaySoundEvent {
    
    
    public SoundEffect soundToPlay;
    
    public PlaySoundEvent(SoundEffect soundToPlay) {
        this.soundToPlay = soundToPlay;
    }

    public SoundEffect getSoundToPlay() {
        return soundToPlay;
    }
    
    

}
