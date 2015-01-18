package fingerprint.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.google.inject.Guice;
import com.google.inject.Injector;

import fingerprint.inout.GameSettings;
import fingerprint.inout.GameSettingsProvider;
import fingerprint.states.MainMenuState;

public class GameLauncher extends StateBasedGame {

    private static final Logger logger = Logger.getLogger(GameLauncher.class.getName());
    
    private static GameSettings gameSettings;
    
    private static final String GAME_VERSION = "1";
    
    
    
    private static final String PROGRAM_NAME = "Project Fingerprint Version "+GAME_VERSION;
    
    public static Injector injector;
    
    public GameLauncher(String title) {
        super(title);
        injector = Guice.createInjector(new GameModule());
    }
    public static void main(String[] arguments) {
        
        GameSettingsProvider settingsLoader = new GameSettingsProvider();
        gameSettings = settingsLoader.loadGameSettings();
        if(gameSettings == null){
            logger.log(Level.SEVERE, "Couldn't load gamesettings. Exiting.");
            return;
        }
        
        try {
            AppGameContainer app = new AppGameContainer(new GameLauncher(PROGRAM_NAME));
            applyGameSettings(app, gameSettings);
            app.start();  //Start the application
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    public static void applyGameSettings(AppGameContainer app, GameSettings appliedSettings) throws SlickException{
        app.setDisplayMode(appliedSettings.resolution.getWidth(), appliedSettings.resolution.getHeight(), appliedSettings.fullScreen);
        app.setAlwaysRender(true);
        app.setTargetFrameRate(appliedSettings.frameCap);
        app.setVSync(appliedSettings.vSync);
    }
    
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
            addState(new MainMenuState());
    }


}