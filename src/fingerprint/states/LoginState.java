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
import fingerprint.inout.GameSettingsProvider;
import fingerprint.inout.LoginToken;
import fingerprint.networking.NetworkEnvironment;
import fingerprint.networking.NetworkEvents;
import fingerprint.rendering.gui.ClickableMouseOverArea;
import fingerprint.rendering.gui.FocusableTextField;
import fingerprint.rendering.manager.RenderingManager;
import fingerprint.rendering.util.ConnectionRenderingInformation;
import fingerprint.rendering.util.LoginRenderingInformation;
import fingerprint.states.events.ChangeStateEvent;
import fingerprint.states.events.CloseProgramEvent;
import fingerprint.states.events.GiveSocketInfoEvent;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.awt.Desktop;
import java.net.URI;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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
    
    private GameSettingsProvider gameSettingsProvider = new GameSettingsProvider();
    
    private Socket socket;
    private OkHttpClient okHttpClient;
    
    private FocusableTextField logintokenTextField;
    private ClickableMouseOverArea registerButton;
    private ClickableMouseOverArea clearButton;
    private ClickableMouseOverArea loginButton;
    public static NetworkEnvironment environment;

    
    public static String lastMessageFromServer = "";
    public static String SOCKETSTATUS;
    
    public static String versionString  = "";
    public static String versionChangelog = "";
    
    private String registerToken = "";
    private boolean registering = false;
    
    private long sentAutomaticLogin = 0;

    public LoginState() {
    }

    @Override
    public int getID() {
        return State_IDs.LOGIN_SCREEN_ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        
        try {
            LoginToken savedLoginToken = gameSettingsProvider.loadLoginToken();
            reCreateTextFields(gc);
            logintokenTextField.setText( savedLoginToken == null ? "" : savedLoginToken.token);
            environment = savedLoginToken == null ? NetworkEnvironment.PRODUCTION : savedLoginToken.environment;
            initializeSocketToLoginMode();
            if (!logintokenTextField.getText().isEmpty()) {
                identifyToServer();
                sentAutomaticLogin = System.currentTimeMillis();
            }
        } catch (SlickException | NoSuchAlgorithmException | KeyManagementException ex) {
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
                LoginToken loginToken = new LoginToken();
                loginToken.token = logintokenTextField.getText();
                loginToken.environment = environment;
                gameSettingsProvider.saveLoginToken(loginToken);
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
                    parseChangelog(payload);
                } catch (JSONException ex) {
                    Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).on(NetworkEvents.SERVER_GIVE_REGISTER_TOKEN, (Object... args) -> {
                try {
                    JSONObject payload = (JSONObject) args[0];
                    registerToken = payload.getString("token");
                    registering = true;
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI("https://spacteria.com/register?token=" + registerToken));
                    }
                    StringSelection selection = new StringSelection("https://spacteria.com/register?token=" + registerToken);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, selection);
                } catch (JSONException | IOException | URISyntaxException ex) {
                    Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).on(NetworkEvents.SERVER_GIVE_LOGIN_TOKEN, (Object... args) -> {
                try {
                    JSONObject payload = (JSONObject) args[0];
                    String loginToken = payload.getString("token");
                    logintokenTextField.setText(loginToken);
                    identifyToServer();
                    
                    
                } catch (JSONException ex) {
                    Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            socket.connect();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void parseChangelog(JSONObject payload) throws JSONException {
        versionString = (payload.getString("version") + " - " + payload.getString("date"));
        String resultChangelogText = "Server:;";
        JSONArray serverChanges = payload.getJSONArray("serverChanges");
        for(int i = 0; i < serverChanges.length(); i++){
            String change = serverChanges.getString(i);
            List<String> changes = splitEqually(change, 40);
            String[] rows = changes.toArray(new String[changes.size()]);
            resultChangelogText += " - ";
            for(String row : rows) {
                resultChangelogText += row;
                resultChangelogText += ";";
            }
        }
        resultChangelogText += "Client:;";
        JSONArray clientChanges = payload.getJSONArray("clientChanges");
        for(int i = 0; i < clientChanges.length(); i++){
            String change = clientChanges.getString(i);
            List<String> changes = splitEqually(change, 40);
            String[] rows = changes.toArray(new String[changes.size()]);
            resultChangelogText += " - ";
            for(String row : rows) {
                resultChangelogText += row;
                resultChangelogText += ";";
            }
        }
        
        
        versionChangelog = resultChangelogText;
    }
    
    public static List<String> splitEqually(String text, int size) {
        List<String> ret = new ArrayList<>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }
    
    private void reCreateTextFields(GameContainer gc) throws SlickException{
        Font font = new Font("Arial Bold", Font.BOLD, 32);
        TrueTypeFont ttf = new TrueTypeFont(font, true);
        String loginToken = logintokenTextField == null ? "" : logintokenTextField.getText();
       
        
        logintokenTextField = new FocusableTextField(gc, ttf, 200, 540, 500, 34);
        logintokenTextField.setBackgroundColor(Color.gray);
        logintokenTextField.setText(loginToken);
        logintokenTextField.setAcceptingInput(true);
        
        clearButton = new ClickableMouseOverArea(gc, new Image(1,1), 200, 600, 100, 34, "Clear", () -> {
            logintokenTextField.setText("");
            return true;
        });
        
        loginButton = new ClickableMouseOverArea(gc, new Image(1,1), 340, 600, 100, 34, "Login", () -> {
            identifyToServer();
            return true;
        });
        
        registerButton = new ClickableMouseOverArea(gc, new Image(1,1), 200, 684, 500, 34, "Sign in with Google / I forgot my password", () -> {
            askToRegister();
            return true;
        });
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        ConnectionRenderingInformation info = new ConnectionRenderingInformation(socket, environment.getServerlURL(), lastMessageFromServer, SOCKETSTATUS);
        info.setVersion(versionString);
        info.setChangelog(versionChangelog);
        info.setEnvironment(environment);
        
        LoginRenderingInformation loginRenderingInformation = new LoginRenderingInformation(logintokenTextField, registerButton, clearButton, loginButton);
        loginRenderingInformation.setRegisterToken(registerToken);
        loginRenderingInformation.setRegistering(registering);
        if(socket != null && (sentAutomaticLogin + 1000) < System.currentTimeMillis()){
            renderingManager.drawLogin(grphcs, gc, loginRenderingInformation, info);
        } else {
            grphcs.setColor(Color.white);
            grphcs.drawString("Loading...", 100, 100);
        }
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        inputManager.setInput(gc.getInput());
        inputManager.update();
        logintokenTextField.setInput(gc.getInput());
        
        if(inputManager.isCtrlVPressed()) {
            try {
                String data = (String) Toolkit.getDefaultToolkit()
                        .getSystemClipboard().getData(DataFlavor.stringFlavor);
                if(logintokenTextField.hasFocus()){
                    logintokenTextField.dopaste(data);
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(inputManager.isKeyBindPressed(KeyBindAction.C,true) && (!logintokenTextField.hasFocus())){
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
             if(logintokenTextField.hasFocus()){
                logintokenTextField.setFocus(false);
            } else {
                eventBus.post(new CloseProgramEvent(false, true));
            }
        }
        
    }
    
    private void refreshSocket() {
        socket.disconnect();
        socket.off();
        socket.close();
        registering = false;
        registerToken = "";
        try {
            initializeSocketToLoginMode();
        } catch (Exception ex) {
            Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void identifyToServer(){
        JSONObject identifyObject = new JSONObject();
        try {
            identifyObject.put("type", "game-client");
            identifyObject.put("version", GameLauncher.GAME_VERSION);
            identifyObject.put("token", logintokenTextField.getText());
        } catch (JSONException ex) {
            Logger.getLogger(ServerListState.class.getName()).log(Level.SEVERE, null, ex);
        }

        socket.emit(NetworkEvents.CLIENT_IDENTIFY, identifyObject, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
    }
    
    public void askToRegister(){
        JSONObject registerObject = new JSONObject();
        System.out.println("Register sent");

        socket.emit(NetworkEvents.CLIENT_ASK_REGISTER, registerObject, new Ack() {
            @Override
            public void call(Object... args) {
               
            }
        });
    }
    
    private void cleanUpSocket() {
        socket.off(NetworkEvents.SERVER_LOGIN_FAIL);
        socket.off(NetworkEvents.SERVER_LOGIN_SUCCESS);
        socket.off(NetworkEvents.SERVER_GIVE_LOGIN_TOKEN);
        socket.off(NetworkEvents.SERVER_GIVE_REGISTER_TOKEN);
    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        logintokenTextField.setAcceptingInput(false);
        registering = false;
        registerToken = "";
    }
    
    @Subscribe
    public void listenInitGameInfoEvent(GiveSocketInfoEvent event){
        try {
            if(event.getState() != getID()) {
                return;
            }
            initializeSocketToLoginMode();
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            Logger.getLogger(LoginState.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
