/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.states;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import fingerprint.controls.InputManager;
import fingerprint.controls.KeyBindAction;
import fingerprint.networking.NetworkEnvironment;
import fingerprint.networking.NetworkEvents;
import fingerprint.rendering.gui.FocusableTextField;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.rendering.util.ConnectionRenderingInformation;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.CloseProgramEvent;
import fingerprint.states.events.GiveSocketInfoEvent;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
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

    @Inject private EventBus eventBus;
    
    private Socket socket;
    private OkHttpClient okHttpClient;
    

    private FocusableTextField usernameTextField;
    private FocusableTextField passwordTextField;
    private static NetworkEnvironment environment;

    
    private String lastMessageFromServer = "";
    public static String SOCKETSTATUS;
    
    private String versionString  = "";
    private String versionChangelog = "";

    public LoginState() {
        environment = NetworkEnvironment.PRODUCTION;
    }

    @Override
    public int getID() {
        return State_IDs.LOGIN_SCREEN_ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        
        try {
            reCreateTextFields(gc);
            
            initializeSocketToLoginMode();
        } catch (Exception ex) {
            Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
    }
    
    private final X509TrustManager trustAllCerts = new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {
        }
    };
    
    private void initializeSocketToLoginMode() throws NoSuchAlgorithmException, KeyManagementException {
        try {
            //TODO: Do not trust all certificates.
            
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, new TrustManager[] { trustAllCerts }, null);
            
            okHttpClient = new OkHttpClient.Builder()
            .hostnameVerifier((String hostname, SSLSession session) -> true)
            .sslSocketFactory(sslContext.getSocketFactory(), trustAllCerts)
            .build();
            
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);
            
            
            IO.Options options = new IO.Options();
            options.webSocketFactory = okHttpClient;
            options.callFactory = okHttpClient;
            options.port = 443;
            options.secure = true;
            options.upgrade = true;
            
            
            socket = IO.socket(environment.getServerlURL(), options);
            socket.on(Socket.EVENT_CONNECT, (Object... args) -> {
                SOCKETSTATUS = "CONNECTED";
            }).on(Socket.EVENT_ERROR, (Object... args) -> {
                Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, args);
            }).on(Socket.EVENT_DISCONNECT, (Object... args) -> {
                eventBus.post(new ChangeStateEvent(getID(), State_IDs.LOGIN_SCREEN_ID));
                SOCKETSTATUS = "NO CONNECTION (S)";
            }).on(Socket.EVENT_CONNECTING, (Object... args) -> {
                SOCKETSTATUS = "TRYING TO CONNECT";
            }).on(Socket.EVENT_CONNECT_ERROR, (Object... args) -> {
                SOCKETSTATUS = "NO CONNECTION (E)";
            }).on(Socket.EVENT_CONNECT_TIMEOUT, (Object... args) -> {
                SOCKETSTATUS = "NO CONNECTION (T)";
            }).on(Socket.EVENT_RECONNECT, (Object... args) -> {
                SOCKETSTATUS = "CONNECTED";
            }).on(Socket.EVENT_RECONNECTING, (Object... args) -> {
                SOCKETSTATUS = "TRYING TO CONNECT";
            }).on(Socket.EVENT_RECONNECT_ATTEMPT, (Object... args) -> {
                SOCKETSTATUS = "TRYING TO CONNECT";
            }).on(Socket.EVENT_RECONNECT_ERROR, (Object... args) -> {
                SOCKETSTATUS = "NO CONNECTION (RE)";
            }).on(Socket.EVENT_RECONNECT_FAILED, (Object... args) -> {
                SOCKETSTATUS = "NO CONNECTION (RF)";
            }).on(NetworkEvents.SERVER_LOGIN_SUCCESS, (Object... args) -> {
                eventBus.post(new GiveSocketInfoEvent(socket.id(), socket, State_IDs.MAIN_MENU_ID));
                eventBus.post(new ChangeStateEvent(getID(), State_IDs.MAIN_MENU_ID));
                cleanUpSocket();
            }).on(NetworkEvents.SERVER_LOGIN_FAIL, (Object... args) -> {
                try {
                    JSONObject payload = (JSONObject) args[0];
                    String reason = payload.getString("reason");
                    lastMessageFromServer = reason;
                    logger.log(Level.SEVERE, Arrays.toString(args));
                } catch (JSONException ex) {
                    Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).on(NetworkEvents.SERVER_VERSION_DATA, (Object... args) -> {
                try {
                    JSONObject payload = (JSONObject) args[0];
                    versionString = payload.getString("version");
                    versionChangelog = payload.getString("changelog");
                } catch (JSONException ex) {
                    Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            socket.connect();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void reCreateTextFields(GameContainer gc){
        Font font = new Font("Arial Bold", Font.BOLD, 32);
        TrueTypeFont ttf = new TrueTypeFont(font, true);
        String oldUserText = usernameTextField == null ? "testuser" : usernameTextField.getText();
        String oldPassText = passwordTextField == null ? "1234567" : usernameTextField.getText();
        
        usernameTextField = new FocusableTextField(gc, ttf, 200, 520, 500, 34);
        usernameTextField.setBackgroundColor(Color.gray);
        usernameTextField.setBorderColor(Color.darkGray);
        usernameTextField.setText(oldUserText);
        usernameTextField.setCursorVisible(true);
        usernameTextField.setAcceptingInput(true);
        passwordTextField = new FocusableTextField(gc, ttf, 200, 620, 500, 34);
        passwordTextField.setBackgroundColor(Color.gray);
        passwordTextField.setBorderColor(Color.darkGray);
        passwordTextField.setText(oldPassText);
        passwordTextField.setCursorVisible(true);
        passwordTextField.setAcceptingInput(true);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        ConnectionRenderingInformation info = new ConnectionRenderingInformation(socket, environment.getServerlURL(), lastMessageFromServer, SOCKETSTATUS);
        info.setVersion(versionString);
        info.setChangelog(versionChangelog);
        info.setEnvironment(environment);
        if(socket != null){
            renderingManager.drawLogin(grphcs, gc, usernameTextField, passwordTextField, info);
        }
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        usernameTextField.setInput(gc.getInput());
        passwordTextField.setInput(gc.getInput());
        
        if(inputManager.isKeyBindPressed(KeyBindAction.MENU,true)){
            //Try to login to the game
            identifyToServer();
        }
        if(inputManager.isKeyBindPressed(KeyBindAction.SKIP,true)){
            if (!usernameTextField.hasFocus() && !passwordTextField.hasFocus()){
                usernameTextField.doFocus();
            } else if(usernameTextField.hasFocus()){
                passwordTextField.doFocus();
            } else {
                usernameTextField.doFocus();
            }
        }
        
        if(inputManager.isCtrlVPressed()) {
            try {
                String data = (String) Toolkit.getDefaultToolkit()
                        .getSystemClipboard().getData(DataFlavor.stringFlavor);
                if(usernameTextField.hasFocus()){
                    usernameTextField.dopaste(data);
                }
                if(passwordTextField.hasFocus()){
                    passwordTextField.dopaste(data);
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(inputManager.isKeyBindPressed(KeyBindAction.C,true) && (!usernameTextField.hasFocus() || !passwordTextField.hasFocus())){
            int next = environment.ordinal() + 1;
            int amount = NetworkEnvironment.values().length;
            int index = next % amount;
            environment = NetworkEnvironment.values()[index];
            
            refreshSocket();
        }
        
        if(inputManager.isKeyBindPressed(KeyBindAction.DEBUG_TOGGLE, true)) {
            refreshSocket();
            lastMessageFromServer = "Refreshed socket";
        }
        
        if(inputManager.isKeyBindPressed(KeyBindAction.EXIT,true)){
             if(usernameTextField.hasFocus()){
                usernameTextField.setFocus(false);
            } else if(passwordTextField.hasFocus()) {
                passwordTextField.setFocus(false);
            } else {
                eventBus.post(new CloseProgramEvent(false, true));
            }
        }
        
    }
    
    private void refreshSocket() {
        socket.disconnect();
        socket.off();
        socket.close();
        try {
            initializeSocketToLoginMode();
        } catch (Exception ex) {
            Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Refreshed socket!!!");
    }
    
    
    public void identifyToServer(){
        JSONObject identifyObject = new JSONObject();
        try {
            identifyObject.put("type", "game-client");
            GsonBuilder ga=new GsonBuilder();
            Gson gson = ga.create();
            identifyObject.put("username", usernameTextField.getText());
            identifyObject.put("password", passwordTextField.getText());
        } catch (JSONException ex) {
            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
        }

        socket.emit(NetworkEvents.CLIENT_IDENTIFY, identifyObject, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
    }
    
    private void cleanUpSocket() {
        socket.off(NetworkEvents.SERVER_LOGIN_FAIL);
        socket.off(NetworkEvents.SERVER_LOGIN_SUCCESS);
    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        passwordTextField.setAcceptingInput(false);
        usernameTextField.setAcceptingInput(false);
    }
    
    private X509TrustManager getX509() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // Using null here initialises the TMF with the default trust store.
        tmf.init((KeyStore) null);

        // Get hold of the default trust manager
        X509TrustManager x509Tm = null;
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                x509Tm = (X509TrustManager) tm;
                break;
            }
        }

        // Wrap it in your own class.
        final X509TrustManager finalTm = x509Tm;
        X509TrustManager customTm = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return finalTm.getAcceptedIssuers();
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
                finalTm.checkServerTrusted(chain, authType);
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
                finalTm.checkClientTrusted(chain, authType);
            }
        };

        

        return customTm;
    }

}
