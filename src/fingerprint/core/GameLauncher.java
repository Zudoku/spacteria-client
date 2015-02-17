package fingerprint.core;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
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

import fingerprint.gameplay.loader.PreGamePlayStateLoader;
import fingerprint.inout.GameSettings;
import fingerprint.inout.GameSettingsProvider;
import fingerprint.rendering.RenderingResolutions;
import fingerprint.states.CharacterCreationState;
import fingerprint.states.GamePlayState;
import fingerprint.states.MainMenuState;
import fingerprint.states.State_IDs;
import fingerprint.states.WorldCreationState;
import fingerprint.states.WorldSelectionState;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.CloseProgramEvent;

public class GameLauncher extends StateBasedGame {
    private static final Logger logger = Logger.getLogger(GameLauncher.class.getName());
    public static final int GAME_VERSION = 2;
    public static final String PROGRAM_STATE = "DEV";
    public static final String PROGRAM_NAME = "Project Fingerprint Version "+GAME_VERSION + " " + PROGRAM_STATE;
    
    public static GameSettings gameSettings;
    public static Injector injector;
    @Inject private EventBus eventBus;
    
    private List<BasicGameState> gameStates = new ArrayList<>();
    private PreGamePlayStateLoader gamePlayStateLoader = new PreGamePlayStateLoader();
    private static GameModule gamemodule;
    private static AppGameContainer application;
    
    public GameLauncher(String title) {
        super(title);
        injector = Guice.createInjector(gamemodule);
        injector.injectMembers(this);
        eventBus.register(this);
        eventBus.register(gamePlayStateLoader);
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
            application.start();  //Start the application
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
        MainMenuState mainMenu = new MainMenuState();
        initState(mainMenu);

        WorldSelectionState worldSelection = new WorldSelectionState();
        initState(worldSelection);
        
        CharacterCreationState characterCreation = new CharacterCreationState();
        initState(characterCreation);
        
        GamePlayState gamePlay = new GamePlayState();
        initState(gamePlay);
        
        WorldCreationState worldCreation = new WorldCreationState();
        initState(worldCreation);
    }
    private void initState(BasicGameState state){
        injector.injectMembers(state);
        gameStates.add(state);
        addState(state);
    }
    @Subscribe
    public void listenToChangeStateEvent(ChangeStateEvent event){
        if (gamePlayStateLoader.isOn()) {
            if (event.getToState() == State_IDs.GAME_PLAY_ID) {
                if (gamePlayStateLoader.getWorld() == null) {
                    gamePlayStateLoader.reset();
                    logger.log(Level.SEVERE,"Couldn't change to gameplaymode, because world was null. Reseting.");
                    enterState(State_IDs.WORLD_SELECTION_ID);
                    return;
                }
                if(!gamePlayStateLoader.isCharacterDone()){
                    logger.log(Level.INFO,"Character isn't done. Going to character creation screen.");
                    enterState(State_IDs.CHARACTER_SCREEN_ID);
                    return;
                }
                logger.log(Level.INFO,"Switching into gameplayState");
                GamePlayState gpState = (GamePlayState)gameStates.get(3);
                gpState.setGameWorld(gamePlayStateLoader.getWorld());
                gpState.setPlayer(gamePlayStateLoader.getPlayer());
                enterState(State_IDs.GAME_PLAY_ID);
                gamePlayStateLoader.reset();
                gamePlayStateLoader.setOn(false);
                return;
            }
        }
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