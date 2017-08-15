package fingerprint.inout;

import java.util.HashMap;
import java.util.Map;

import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.rendering.util.RenderingResolutions;

public class GameSettings {
    /**
     * RENDERING
     */
    public boolean fullScreen;
    public boolean borderless;
    public RenderingResolutions resolution;
    public int frameCap;
    public boolean vSync;
    
    /**
     * INPUT
     */
    public Map<KeyBindAction,Integer> keyboardKeyBinds = new HashMap<>();
    public Map<KeyBindAction,Integer> controllerKeyBinds = new HashMap<>();
    
    public boolean keyboardActive;
    public int CURRENT_CONTROLLER = 0;
    
    /**
     * SOUND
     */
    
    public int soundVolume;
    public int musicVolume;
    
    
    public GameSettings() {
        
    }
    public void resetDefaultSettings(){
        fullScreen = false;
        borderless = false;
        resolution = RenderingResolutions.RES_1280_1024;
        frameCap = 60;
        vSync = true;
        
        keyboardKeyBinds = InputManager.getDefaultKeyBoardBinds();
        controllerKeyBinds = InputManager.getDefaultControllerBinds();
        
        keyboardActive = true;
        CURRENT_CONTROLLER = 0;
        soundVolume = 100;
        musicVolume = 100;
        
    }
    
}
