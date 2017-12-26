/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.rendering.util;

import fingerprint.rendering.gui.ClickableMouseOverArea;
import fingerprint.rendering.gui.FocusableTextField;

/**
 * Created Dec 25, 2017
 * @author arska
 */
public class LoginRenderingInformation {
    private FocusableTextField logintokenTextField;
    private ClickableMouseOverArea registerButton;
    private ClickableMouseOverArea clearButton;
    private ClickableMouseOverArea loginButton;
    
    private boolean registering;
    private String registerToken;

    public LoginRenderingInformation(FocusableTextField logintokenTextField, ClickableMouseOverArea registerButton, ClickableMouseOverArea clearButton, ClickableMouseOverArea loginButton) {
        this.logintokenTextField = logintokenTextField;
        this.registerButton = registerButton;
        this.clearButton = clearButton;
        this.loginButton = loginButton;
    }

    public ClickableMouseOverArea getClearButton() {
        return clearButton;
    }

    public ClickableMouseOverArea getLoginButton() {
        return loginButton;
    }

    public FocusableTextField getLogintokenTextField() {
        return logintokenTextField;
    }

    public ClickableMouseOverArea getRegisterButton() {
        return registerButton;
    }

    public String getRegisterToken() {
        return registerToken;
    }

    public boolean isRegistering() {
        return registering;
    }

    public void setRegistering(boolean registering) {
        this.registering = registering;
    }

    public void setRegisterToken(String registerToken) {
        this.registerToken = registerToken;
    }
    
}
