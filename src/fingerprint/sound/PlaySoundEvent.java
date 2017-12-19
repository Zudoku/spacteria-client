package fingerprint.sound;

public class PlaySoundEvent {
    
    
    public SoundEffect soundToPlay;
    public boolean lowVolume;
    
    public PlaySoundEvent(SoundEffect soundToPlay, boolean lowVolume) {
        this.soundToPlay = soundToPlay;
        this.lowVolume = lowVolume;
    }

    public SoundEffect getSoundToPlay() {
        return soundToPlay;
    }
    
    

}
