package fingerprint.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

import com.google.inject.Singleton;

import fingerprint.inout.GameSettings;


@Singleton
public class InputManager {
    private static final Logger logger = Logger.getLogger(InputManager.class.getName());
    
    private Map<KeyBindAction,Integer> keyboardKeyBinds = new HashMap<>();
    private Map<KeyBindAction,Integer> controllerKeyBinds = new HashMap<>();
    
    private boolean keyboardActive;
    private int CURRENT_CONTROLLER = 0;
    
    private Input input;
    private List<KeyBindAction> pressedKeyBinds = new ArrayList<>();
    
    public InputManager() {
        
    }
    public void update(){
        List<KeyBindAction> notPressed = new ArrayList<>();
        for(KeyBindAction action : pressedKeyBinds){
            if(!isKeyBindDown(action,false)){
                notPressed.add(action);
            }
        }
        for(KeyBindAction action : notPressed){
            pressedKeyBinds.remove(action);
        }
    }
    public boolean isKeyBindDown(KeyBindAction action,boolean press){
        boolean result = false;
        if(keyboardActive){
            result = input.isKeyDown(keyboardKeyBinds.get(action));
        }else{
            result = input.isControlPressed(controllerKeyBinds.get(action), CURRENT_CONTROLLER);
        }
        if(result && press){
            pressedKeyBinds.add(action);
        }
        return result;
    }
    public boolean isKeyBindPressed(KeyBindAction action,boolean press){
        if(!isKeyBindDown(action,false)){
            return false;
        }
        if(pressedKeyBinds.contains(action)){
            return false;
        }else{
            if(press){
                pressedKeyBinds.add(action);
            }
            return true;
        }
    }
    public static Map<KeyBindAction,Integer> getDefaultKeyBoardBinds(){
        Map<KeyBindAction,Integer> result = new HashMap<>();
        result.put(KeyBindAction.UP, Keyboard.KEY_W);
        result.put(KeyBindAction.DOWN, Keyboard.KEY_S);
        result.put(KeyBindAction.LEFT, Keyboard.KEY_A);
        result.put(KeyBindAction.RIGHT, Keyboard.KEY_D);
        
        result.put(KeyBindAction.A, Keyboard.KEY_Q);
        result.put(KeyBindAction.B, Keyboard.KEY_E);
        result.put(KeyBindAction.C, Keyboard.KEY_C);
        result.put(KeyBindAction.D, Keyboard.KEY_SPACE);
        result.put(KeyBindAction.SKIP, Keyboard.KEY_TAB);
        result.put(KeyBindAction.DEBUG_TOGGLE, Keyboard.KEY_F2);
        result.put(KeyBindAction.MENU, Keyboard.KEY_RETURN);
        result.put(KeyBindAction.EXIT, Keyboard.KEY_ESCAPE);
        
        return result;
    }
    public static Map<KeyBindAction,Integer> getDefaultControllerBinds(){
        Map<KeyBindAction,Integer> result = new HashMap<>();
        result.put(KeyBindAction.UP, 1);
        result.put(KeyBindAction.DOWN, 2);
        result.put(KeyBindAction.LEFT, 3);
        result.put(KeyBindAction.RIGHT, 4);
        
        result.put(KeyBindAction.A, 5);
        result.put(KeyBindAction.B, 6);
        result.put(KeyBindAction.C, 7);
        result.put(KeyBindAction.D, 8);
        
        result.put(KeyBindAction.MENU, 9);
        result.put(KeyBindAction.EXIT, 10);
        
        return result;
    }
    
    public void loadKeyBinds(GameSettings settings){
        setKeyboardActive(settings.keyboardActive);
        CURRENT_CONTROLLER = settings.CURRENT_CONTROLLER;
        
        keyboardKeyBinds = settings.keyboardKeyBinds;
        controllerKeyBinds = settings.controllerKeyBinds;

    }
    
    public void setInput(Input input) {
        this.input = input;
    }
    public Input getInput() {
        return input;
    }
    public void setControllerKeyBinds(
            Map<KeyBindAction, Integer> controllerKeyBinds) {
        this.controllerKeyBinds = controllerKeyBinds;
    }
    public void setCURRENT_CONTROLLER(int CURRENT_CONTROLLER) {
        this.CURRENT_CONTROLLER = CURRENT_CONTROLLER;
    }
    public void setKeyboardActive(boolean keyboardActive) {
        this.keyboardActive = keyboardActive;
    }
    public void setKeyboardKeyBinds(Map<KeyBindAction, Integer> keyboardKeyBinds) {
        this.keyboardKeyBinds = keyboardKeyBinds;
    }
    
    public Map<KeyBindAction, Integer> getControllerKeyBinds() {
        return controllerKeyBinds;
    }
    public int getCURRENT_CONTROLLER() {
        return CURRENT_CONTROLLER;
    }
    public Map<KeyBindAction, Integer> getKeyboardKeyBinds() {
        return keyboardKeyBinds;
    }
}
