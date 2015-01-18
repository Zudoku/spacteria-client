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
    
    private static String GAMESETTINGS_PATH = "Settings/gamesettings.data";
    
    public void saveGameSettings(GameSettings settings){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //gson.toJson(settings);
        
        BufferedWriter writer = null;
        logger.log(Level.FINEST,"Saving gamesettings to {0}",GAMESETTINGS_PATH);
        try {
            //Open file
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(GAMESETTINGS_PATH), "utf-8"));
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
        logger.log(Level.FINEST,"Loading Controls...");
        try{
            Gson gson = new GsonBuilder().create();
            settings = gson.fromJson(new FileReader(GAMESETTINGS_PATH), GameSettings.class);
            logger.log(Level.FINEST,"Found existing keybinds!");
        }catch (FileNotFoundException ex){
            settings = new GameSettings();
            settings.resetDefaultSettings();
            logger.log(Level.FINER,"Couldn't find existing keybinds. Default controls assigned! This is probably your first time playing this game!");
            saveGameSettings(settings);
            logger.log(Level.FINER,"Saved keybinds!");
        }
        return settings;
    }
}
