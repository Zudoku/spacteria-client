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

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import fingerprint.core.GameLauncher;
import fingerprint.gameplay.map.gameworld.GameWorld;
import fingerprint.gameplay.map.gameworld.GameWorldDeserializer;
import fingerprint.gameplay.map.gameworld.GameWorldMetaData;
import fingerprint.gameplay.map.generation.GameWorldBuilder;
import fingerprint.states.menu.enums.GameDifficulty;

@Singleton
public class GameFileHandler {
    private static final Logger logger = Logger.getLogger(GameFileHandler.class.getName());
    private GameWorldDeserializer worldDeSerializer;
    private EventBus eventBus;
    private boolean readableFiles = true;
    
    public static String WORLD_FILE_EXTENSION = ".world";
    
    @Inject
    public GameFileHandler(EventBus eventBus) {
        worldDeSerializer = new GameWorldDeserializer();
        this.eventBus = eventBus;
    }
    public boolean initiateWorld(String filename, GameDifficulty difficulty){
        GameWorldBuilder worldBuilder = new GameWorldBuilder();
        
        GameWorldMetaData metaData = new GameWorldMetaData();
        metaData.filename = filename;
        metaData.fileVersion = GameLauncher.GAME_VERSION;
        metaData.lastPlayed = System.currentTimeMillis();
        
        
        GameWorld createdWorld= worldBuilder.generateNewWorld(metaData);
        if(createdWorld == null){
            logger.log(Level.SEVERE,"Couldn't generate a proper world.");
        }
        
        if(!validateFileName(filename)){
            logger.log(Level.SEVERE,"Couldn't save a worldFile because of bad filename.");
            return false;
        }
        
        return saveWorldGameFile(createdWorld);
    }
    private void initWorldGameFileSave(GameWorld initWorld){
        //VERSION 
        if(initWorld.getMetaData().fileVersion != GameLauncher.GAME_VERSION){
            logger.log(Level.INFO,"World version differs {0} from current {1} ",new Object[]{initWorld.getMetaData().fileVersion , GameLauncher.GAME_VERSION});
            if(!initWorld.getMetaData().oldVersions.contains(initWorld.getMetaData().fileVersion)){
                initWorld.getMetaData().oldVersions.add(initWorld.getMetaData().fileVersion);
            }
            initWorld.getMetaData().fileVersion = GameLauncher.GAME_VERSION;
        }
        //LAST PLAYED
        long lastPlayed= System.currentTimeMillis();
        initWorld.getMetaData().lastPlayed = lastPlayed;
        

    }
    
    /**
     * Save the world state to JSON file to Saves folder.
     * @param worldToSave
     */
    public boolean saveWorldGameFile(GameWorld worldToSave){
     // Make Gson JSON serializer.
        Gson gson;
        GsonBuilder ga=new GsonBuilder();
        if(readableFiles){
            ga.setPrettyPrinting();
        }
        gson=ga.create();
        BufferedWriter writer = null;
        logger.log(Level.FINEST,"Starting to save {0}",worldToSave);
        if(!validateFileName(worldToSave.getMetaData().filename)){
            logger.log(Level.SEVERE,"Couldn't save a worldFile because of bad filename.");
            return false;

        }
        //INIT SAVE
        initWorldGameFileSave(worldToSave);
        //ACTUAL SAVING
        try {
            //Open file
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("Saves/"+worldToSave.getMetaData().filename + WORLD_FILE_EXTENSION), "utf-8"));
            //Write to file
            gson.toJson(worldToSave, writer);
            //Close file
            writer.close();
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE,"Unsupported encoding! {0}",ex);
            return false;
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE,"File not found! {0}",ex);
            return false;
        } catch (IOException ex) {
            logger.log(Level.SEVERE,"IO Exeption! {0}",ex);
            return false;
        }
        //POST SAVE
        //TODO:
        logger.log(Level.FINEST,"Saving finished!");
        return true;
    }
    public GameWorld loadWorldGameFile(String filename){
        String trueFileName = "Saves/" + filename + WORLD_FILE_EXTENSION;
        GameWorld loadedWorld = null;
        try {
            GsonBuilder gb=new GsonBuilder();
            gb.registerTypeAdapter(GameWorld.class,worldDeSerializer);
            Gson gson = gb.create();
            loadedWorld = gson.fromJson(new FileReader(trueFileName), GameWorld.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return loadedWorld;
    }
    public static boolean validateFileName(String filename){
        if(!filename.matches("^[a-zA-Z0-9_-]*$")){
            
            return false;
        }
        return true;
    }
    public static String removeFileExension(String original){
        return original.replaceFirst("[.][^.]+$", "");
    }
}
