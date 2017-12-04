package fingerprint.core;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import fingerprint.inout.FileUtil;

import fingerprint.inout.GameSettings;
import fingerprint.inout.GameSettingsProvider;
import fingerprint.rendering.util.RenderingResolutions;
import fingerprint.states.GamePlayState;
import fingerprint.states.MainMenuState;
import fingerprint.states.CharacterCreationState;
import fingerprint.states.CharacterSelectionState;
import fingerprint.states.LoginState;
import fingerprint.states.ServerListState;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.CloseProgramEvent;
import org.newdawn.slick.Sound;

public class GameLauncher extends StateBasedGame {
    private static final Logger logger = Logger.getLogger(GameLauncher.class.getName());
    public static final int GAME_VERSION = 3;
    public static final String PROGRAM_STATE = "DEV";
    public static final String PROGRAM_NAME = "Project Fingerprint Version "+GAME_VERSION + " " + PROGRAM_STATE;
    
    public static GameSettings gameSettings;
    public static Injector injector;
    @Inject private EventBus eventBus;
    
    private List<BasicGameState> gameStates = new ArrayList<>();
    private static GameModule gamemodule;
    private static AppGameContainer application;
    
    public static Sound MAINMENU_MUSIC;
    
    public GameLauncher(String title) {
        super(title);
        injector = Guice.createInjector(gamemodule);
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
        //Set inputManager keybinds
        gamemodule = new GameModule();
        gamemodule.setGameSettings(gameSettings);
        try {
            AppGameContainer application = new AppGameContainer(new GameLauncher(PROGRAM_NAME));
            applyGameSettings(application, gameSettings);
            MAINMENU_MUSIC = new Sound(FileUtil.MUSIC_PATH + "/" + "theoldarcade.ogg");
            MAINMENU_MUSIC.loop(1, gameSettings.soundVolume);
            application.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    public static void applyGameSettings(AppGameContainer app, GameSettings appliedSettings) throws SlickException{
        int screenWidth = appliedSettings.resolution.getWidth();
        int screenHeight = appliedSettings.resolution.getHeight();
        if (appliedSettings.resolution == RenderingResolutions.IDENTIFY_SCREEN) {
            Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            screenHeight = (int) dimension.getHeight();
            screenWidth = (int) dimension.getWidth();
            logger.log(Level.INFO,"Identified your screen resolution to be {0}x{1}",new Object[] { screenWidth, screenHeight });
        }
        if(appliedSettings.borderless && !appliedSettings.fullScreen){
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        }
        app.setDisplayMode(screenWidth, screenHeight, appliedSettings.fullScreen);
        app.setAlwaysRender(true);
        app.setTargetFrameRate(appliedSettings.frameCap);
        app.setVSync(appliedSettings.vSync);
    }
    
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        LoginState loginState = new LoginState();
        initState(loginState);
        
        MainMenuState mainMenu = new MainMenuState();
        initState(mainMenu);

        CharacterSelectionState worldSelection = new CharacterSelectionState();
        initState(worldSelection);
        
        ServerListState serverList = new ServerListState();
        initState(serverList);
        
        GamePlayState gamePlay = new GamePlayState();
        initState(gamePlay);
        
        CharacterCreationState worldCreation = new CharacterCreationState();
        initState(worldCreation);
        
        
    }
    private void initState(BasicGameState state){
        injector.injectMembers(state);
        eventBus.register(state); //??
        gameStates.add(state);
        addState(state);
    }
    @Subscribe
    public void listenToChangeStateEvent(ChangeStateEvent event){
        enterState(event.getToState());
    }
    @Subscribe
    public void listenToCloseDownGameEvent(CloseProgramEvent event){
        if(event.forceClose){
            
        }
        System.exit(0); // dirty exit! TODO: rework
    }
    /**
     * All events that don't catch anything. This is not supposed to happen ever.
     * @param event Event which was not delivered anywhere.
     */
    @Subscribe public void listenDeadEvents(DeadEvent event){
        logger.warning(String.format("%s was NOT delivered to its correct destination!",event.getEvent().toString()));

    }
    


}