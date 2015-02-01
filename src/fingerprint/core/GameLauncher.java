package fingerprint.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import fingerprint.inout.GameSettings;
import fingerprint.inout.GameSettingsProvider;
import fingerprint.states.CharacterCreationState;
import fingerprint.states.GamePlayState;
import fingerprint.states.MainMenuState;
import fingerprint.states.WorldSelectionState;
import fingerprint.states.events.ChangeStateEvent;

public class GameLauncher extends StateBasedGame {
    private static final Logger logger = Logger.getLogger(GameLauncher.class.getName());
    public static final String GAME_VERSION = "2";
    public static final String PROGRAM_NAME = "Project Fingerprint Version "+GAME_VERSION;
    
    public static GameSettings gameSettings;
    public static Injector injector;
    @Inject private EventBus eventBus;
    
    private List<BasicGameState> gameStates = new ArrayList<>();
    
    
    public GameLauncher(String title) {
        super(title);
        injector = Guice.createInjector(new GameModule());
        injector.injectMembers(this);
        eventBus.register(this);
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
        MainMenuState mainMenu = new MainMenuState();
        injector.injectMembers(mainMenu);
        gameStates.add(mainMenu);
        addState(mainMenu);

        WorldSelectionState worldSelection = new WorldSelectionState();
        injector.injectMembers(worldSelection);
        gameStates.add(worldSelection);
        addState(worldSelection);
        
        CharacterCreationState characterCreation = new CharacterCreationState();
        injector.injectMembers(characterCreation);
        gameStates.add(characterCreation);
        addState(characterCreation);
        
        GamePlayState gamePlayState = new GamePlayState();
        injector.injectMembers(gamePlayState);
        gameStates.add(gamePlayState);
        addState(gamePlayState);
    }
    @Subscribe
    public void listenToChangeStateEvent(ChangeStateEvent event){
        enterState(event.getToState());
    }
    


}