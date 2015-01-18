package fingerprint.inout;

import fingerprint.rendering.RenderingResolutions;

public class GameSettings {
    /**
     * RENDERING
     */
    public boolean fullScreen;
    public RenderingResolutions resolution;
    public int frameCap;
    public boolean vSync;
    
    
    public GameSettings() {
        
    }
    public void resetDefaultSettings(){
        fullScreen = false;
        resolution = RenderingResolutions.RES_1280_1024;
        frameCap = 60;
        vSync = true;
    }
    
}
