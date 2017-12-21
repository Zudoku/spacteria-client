/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.states;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.core.GameLauncher;
import fingerprint.inout.GameFileHandler;
import fingerprint.inout.GameSettings;
import fingerprint.inout.GameSettingsProvider;
import fingerprint.rendering.gui.ClickableMouseOverArea;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.rendering.manager.UIRenderingUtil;
import fingerprint.rendering.util.RenderingResolutions;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.GiveSocketInfoEvent;
import io.socket.client.Socket;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created Dec 20, 2017
 * @author arska
 */
public class OptionsState extends BasicGameState {
    
    @Inject private RenderingManager renderingManager;
    @Inject private EventBus eventBus;
    private GameSettingsProvider settingsLoader = new GameSettingsProvider();
    @Inject private InputManager inputManager;
    
    private Socket socket;
    
    private GameSettings currentSettings = settingsLoader.loadGameSettings();
    private boolean saved = true;
    
    private ClickableMouseOverArea fullscreenButton;
    private ClickableMouseOverArea borderlessButton;
    private ClickableMouseOverArea vSyncButton;
    private ClickableMouseOverArea resolutionButton;
    private ClickableMouseOverArea framecapButton;
    private ClickableMouseOverArea keyboardActiveButton;
    private ClickableMouseOverArea soundVolumeButton;
    private ClickableMouseOverArea musicVolumeButton;
    
    private ClickableMouseOverArea savesettingsButton;
    
    private ClickableMouseOverArea aControlButton;
    private ClickableMouseOverArea bControlButton;
    private ClickableMouseOverArea cControlButton;
    private ClickableMouseOverArea dControlButton;
    
    private ClickableMouseOverArea upControlButton;
    private ClickableMouseOverArea downControlButton;
    private ClickableMouseOverArea leftControlButton;
    private ClickableMouseOverArea rightControlButton;
    
    private ClickableMouseOverArea skipControlButton;
    private ClickableMouseOverArea menuControlButton;
    private ClickableMouseOverArea debugControlButton;
    private ClickableMouseOverArea exitControlButton;
    
    int scroll = 0;

    @Override
    public int getID() {
        return State_IDs.OPTIONS_ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        fullscreenButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 50 - scroll, 500, 40,"FULLSCREEN: ", () -> {
            currentSettings.fullScreen = !currentSettings.fullScreen;
            saved = false;
            return true;
        });
        borderlessButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 100 - scroll, 500, 40,"BORDERLESS: ", () -> {
            currentSettings.borderless = !currentSettings.borderless;
            saved = false;
            return true;
        });
        vSyncButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 150 - scroll, 500, 40,"VSYNC: ", () -> {
            currentSettings.vSync = !currentSettings.vSync;
            saved = false;
            return true;
        });
        resolutionButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 200 - scroll, 500, 40,"RESOLUTION: ", () -> {
            int nextIndex = currentSettings.resolution.ordinal() + 1;
            if(nextIndex >= RenderingResolutions.values().length){
                nextIndex = 0;
            }
            currentSettings.resolution = RenderingResolutions.values()[nextIndex];
            saved = false;
            return true;
        });
        framecapButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 250 - scroll, 500, 40,"FRAMECAP: ", () -> {
            if(currentSettings.frameCap == 65){
                currentSettings.frameCap = 30;
            } else {
                currentSettings.frameCap = 65;
            }
            return true;
        });
        keyboardActiveButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 300 - scroll, 500, 40,"KEYBOARD ACTIVE: ", () -> {
            return true;
        });
        soundVolumeButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 350 - scroll, 500, 40,"SOUND VOLUME: ", () -> {
            int currentVolume = currentSettings.soundVolume;
            currentVolume += 5;
            if(currentVolume > 100){
                currentVolume = 0;
            }
            currentSettings.soundVolume = currentVolume;
            saved = false;
            return true;
        });
        musicVolumeButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 400 - scroll, 500, 40,"MUSIC VOLUME: ", () -> {
            int currentVolume = currentSettings.musicVolume;
            currentVolume += 5;
            if(currentVolume > 100){
                currentVolume = 0;
            }
            currentSettings.musicVolume = currentVolume;
            saved = false;
            return true;
        });
        
        savesettingsButton = new ClickableMouseOverArea(gc, new Image(1, 1), 700, 100, 500, 40,"SAVE (requires restart to affect) ", () -> {
            settingsLoader.saveGameSettings(currentSettings);
            saved = true;
            return true;
        });
        
        aControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 600 - scroll, 500, 40,"TURN LEFT: ", () -> {
            return true;
        });
        
        bControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 700, 600 - scroll, 500, 40,"TURN RIGHT: ", () -> {
            return true;
        });
        
        cControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 650 - scroll, 500, 40,"TELEPORT: ", () -> {
            return true;
        });
        
        dControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 700, 650 - scroll, 500, 40,"INTERACT: ", () -> {
            return true;
        });
        
        upControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 700 - scroll, 500, 40,"UP: ", () -> {
            return true;
        });
        
        downControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 700, 700 - scroll, 500, 40,"DOWN: ", () -> {
            return true;
        });
        
        leftControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 750 - scroll, 500, 40,"LEFT: ", () -> {
            return true;
        });
        
        rightControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 700, 750 - scroll, 500, 40,"RIGHT: ", () -> {
            return true;
        });
        
        skipControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 800 - scroll, 500, 40,"NEXT / SKIP: ", () -> {
            return true;
        });
        
        menuControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 700, 800 - scroll, 500, 40,"INTERACT: ", () -> {
            return true;
        });
        debugControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 50, 850 - scroll, 500, 40,"DEBUG: ", () -> {
            return true;
        });
        
        exitControlButton = new ClickableMouseOverArea(gc, new Image(1, 1), 700, 850 - scroll, 500, 40,"EXIT: ", () -> {
            return true;
        });
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        grphcs.setFont(UIRenderingUtil.largeVerdanaFont);
        grphcs.setColor(Color.gray);
        
        fullscreenButton.render(gc, grphcs, scroll, currentSettings.fullScreen);
        borderlessButton.render(gc, grphcs, scroll, currentSettings.borderless);
        vSyncButton.render(gc, grphcs, scroll, currentSettings.vSync);
        resolutionButton.render(gc, grphcs, scroll, currentSettings.resolution);
        framecapButton.render(gc, grphcs, scroll, currentSettings.frameCap);
        keyboardActiveButton.render(gc, grphcs, scroll, currentSettings.keyboardActive);
        soundVolumeButton.render(gc, grphcs, scroll, currentSettings.soundVolume);
        musicVolumeButton.render(gc, grphcs, scroll, currentSettings.musicVolume);
        
        aControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.A)));
        bControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.B)));
        cControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.C)));
        dControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.D)));
        
        upControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.UP)));
        downControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.DOWN)));
        leftControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.LEFT)));
        rightControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.RIGHT)));
        
        skipControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.SKIP)));
        menuControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.MENU)));
        debugControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.DEBUG_TOGGLE)));
        exitControlButton.render(gc, grphcs, scroll, Keyboard.getKeyName(currentSettings.keyboardKeyBinds.get(KeyBindAction.EXIT)));
        
        if(!saved){
            grphcs.setColor(Color.red);
        }
        grphcs.drawString("UNSAVED CHANGES: " + !saved, 700, 50);
        
        savesettingsButton.render(gc, grphcs, scroll, "");
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT,true)){
            eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.MAIN_MENU_ID));
            eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
        }
    }
    
    @Subscribe
    public void listenInitGameInfoEvent(GiveSocketInfoEvent event){
        if(event.getState() != getID()) {
            return;
        }

        this.socket = event.getSocket();
    }

}
