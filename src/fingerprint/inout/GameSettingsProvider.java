package fingerprint.inout;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GameSettingsProvider {
    private static final Logger logger = Logger.getLogger(GameSettingsProvider.class.getName());
    

    
    public void saveGameSettings(GameSettings settings){
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        //gson.toJson(settings);
        
        BufferedWriter writer = null;
        logger.log(Level.FINEST,"Saving gamesettings to {0}",FileUtil.SETTINGS_PATH);
        try {
            //Open file
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(FileUtil.SETTINGS_PATH), "utf-8"));
            //Write to file
            gson.toJson(settings,writer);
            //Close file
            writer.close();
            
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE,"Unsupported encoding! {0}",ex);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE,"File not found! {0}",ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE,"IO Exeption! {0}",ex);
        }
    }
    
    
    public GameSettings loadGameSettings(){
        GameSettings settings = null;
        logger.log(Level.FINEST,"Loading gamesettings...");
        try{
            Gson gson = new GsonBuilder().create();
            settings = gson.fromJson(new FileReader(FileUtil.SETTINGS_PATH), GameSettings.class);
            logger.log(Level.FINEST,"Found existing gamesettings!");
        }catch (FileNotFoundException ex){
            settings = new GameSettings();
            settings.resetDefaultSettings();
            logger.log(Level.FINER,"Couldn't find existing gamesettings. Default settings assigned! This is probably your first time playing this game!");
            saveGameSettings(settings);
            logger.log(Level.FINER,"Saved gamesettings!");
        }
        return settings;
    }
    
    public void saveLoginToken(LoginToken loginToken){
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        
        BufferedWriter writer = null;
        try {
            //Open file
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(FileUtil.LOGINTOKEN_PATH), "utf-8"));
            //Write to file
            gson.toJson(loginToken,writer);
            //Close file
            writer.close();
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE,"Unsupported encoding! {0}",ex);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE,"File not found! {0}",ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE,"IO Exeption! {0}",ex);
        }
    }
    
    public LoginToken loadLoginToken(){
        try{
            Gson gson = new GsonBuilder().create();
            LoginToken token = gson.fromJson(new FileReader(FileUtil.LOGINTOKEN_PATH), LoginToken.class);
            logger.log(Level.FINEST,"Found existing loginToken!");
            return token;
        }catch (FileNotFoundException ex){
            return null;
        }
    }
}
