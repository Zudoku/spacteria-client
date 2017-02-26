/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.states;

import com.google.inject.Inject;
import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.mainmenus.GenericGridController;
import fingerprint.rendering.RenderingManager;
import java.awt.Font;
import java.util.Arrays;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created Feb 13, 2017
 * @author arska
 */
public class LoginState  extends BasicGameState {
    
    private static final Logger logger = Logger.getLogger(LoginState.class.getName());
    
    @Inject private RenderingManager renderingManager;
    @Inject private InputManager inputManager;
    
    private GenericGridController controller;
    
    private TextField usernameTextField;
    private TextField passwordTextField;

    public LoginState() {
        controller = new GenericGridController(Arrays.asList(0,0,0), Arrays.asList(0,1));
    }
    
    

    @Override
    public int getID() {
        return State_IDs.LOGIN_SCREEN_ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        Font font = new Font("Arial Bold", Font.BOLD, 32);
        TrueTypeFont ttf = new TrueTypeFont(font, true);
        usernameTextField = new TextField(gc, ttf, 200, 520, 300, 34);
        passwordTextField = new TextField(gc, ttf, 200, 720, 300, 34);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        renderingManager.drawLogin(grphcs, gc, usernameTextField, passwordTextField);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if(inputManager.isKeyBindPressed(KeyBindAction.D,true) && controller.getSelectedRow() == 2){
            //Try to login to the game
            //TODO:
        }
        checkIfFileInputClose();
    }
    
    private void checkIfFileInputClose(){
        if(controller.getSelectedRow() != 0){
            usernameTextField.setAcceptingInput(false);
            usernameTextField.setFocus(false);
        }else{
            usernameTextField.setFocus(true);
            usernameTextField.setConsumeEvents(false);
            usernameTextField.setAcceptingInput(true);
        }
        if(controller.getSelectedRow() != 1){
            passwordTextField.setAcceptingInput(false);
            passwordTextField.setFocus(false);
        }else{
            passwordTextField.setFocus(true);
            passwordTextField.setConsumeEvents(false);
            passwordTextField.setAcceptingInput(true);
        }
    }

}
