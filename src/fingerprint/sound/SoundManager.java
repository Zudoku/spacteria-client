package fingerprint.sound;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Singleton;
import fingerprint.core.GameLauncher;
import fingerprint.inout.FileUtil;
import fingerprint.inout.GameSettings;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

@Singleton
public class SoundManager {
    
    private GameSettings settings = GameLauncher.gameSettings;
    private EventBus eventBus;
    
    public SoundManager(EventBus eventBus){
        this.eventBus = eventBus;
        eventBus.register(this);
    }
    
    @Subscribe
    public void listenPlaySoundEvent(PlaySoundEvent event){
        try {
            Sound playedSound = new Sound(FileUtil.SOUNDS_PATH + "/" + event.getSoundToPlay().getFilename());
            if (event.lowVolume) {
                playedSound.play(1, 0.1f);
            } else {
                playedSound.play(1, settings.soundVolume);
            }
        } catch (SlickException ex) {
            Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }

}
