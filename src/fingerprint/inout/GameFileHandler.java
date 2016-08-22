package fingerprint.inout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import fingerprint.core.GameLauncher;
import fingerprint.gameplay.items.Inventory;
import fingerprint.gameplay.items.InventoryDeserializer;
import fingerprint.gameplay.map.FunctionalMap;
import fingerprint.gameplay.map.gameworld.CharacterSaveFile;
import fingerprint.gameplay.map.gameworld.CharacterSaveFileDeserializer;
import fingerprint.gameplay.map.gameworld.CharacterMetaData;
import fingerprint.gameplay.objects.player.Player;
import fingerprint.states.menu.enums.CharacterClass;

@Singleton
public class GameFileHandler {
    private static final Logger logger = Logger.getLogger(GameFileHandler.class.getName());
    private CharacterSaveFileDeserializer worldDeSerializer;
    private EventBus eventBus;
    private boolean readableFiles = true;

    @Inject
    public GameFileHandler(EventBus eventBus) {
        worldDeSerializer = new CharacterSaveFileDeserializer();
        this.eventBus = eventBus;
    }
    public boolean initCharacter(String filename, CharacterClass charClass){
        
        CharacterMetaData metaData = new CharacterMetaData();
        metaData.filename = filename;
        metaData.fileVersion = GameLauncher.GAME_VERSION;
        metaData.lastPlayed = System.currentTimeMillis();
        
        CharacterSaveFile createdWorld = new CharacterSaveFile();
        createdWorld.setMetaData(metaData);
        createdWorld.setPlayer(new Player());
        
        if(!validateFileName(filename)){
            logger.log(Level.SEVERE,"Couldn't save a worldFile because of bad filename.");
            return false;
        }
        
        return saveCharacterFile(createdWorld);
    }
    
    private void preSave(CharacterSaveFile characterFile){
        //VERSION 
        if(characterFile.getMetaData().fileVersion != GameLauncher.GAME_VERSION){
            logger.log(Level.INFO,"World version differs {0} from current {1} ",new Object[]{characterFile.getMetaData().fileVersion , GameLauncher.GAME_VERSION});
            if(!characterFile.getMetaData().oldVersions.contains(characterFile.getMetaData().fileVersion)){
                characterFile.getMetaData().oldVersions.add(characterFile.getMetaData().fileVersion);
            }
            characterFile.getMetaData().fileVersion = GameLauncher.GAME_VERSION;
        }
        //LAST PLAYED
        long lastPlayed= System.currentTimeMillis();
        characterFile.getMetaData().lastPlayed = lastPlayed;
        

    }
    
    /**
     * Save the world state to JSON file to Saves folder.
     * @param characterFile
     */
    public boolean saveCharacterFile(CharacterSaveFile characterFile){
     // Make Gson JSON serializer.
        Gson gson;
        GsonBuilder ga=new GsonBuilder();
        if(readableFiles){
            ga.setPrettyPrinting();
        }
        gson=ga.create();
        BufferedWriter writer = null;
        logger.log(Level.FINEST,"Starting to save {0}",characterFile);
        if(!validateFileName(characterFile.getMetaData().filename)){
            logger.log(Level.SEVERE,"Couldn't save a character because of bad filename.");
            return false;
        }
        //INIT SAVE
        preSave(characterFile);
        //Create directory
        String worldDirPath = FileUtil.CHARACTERS_PATH+"/"+characterFile.getMetaData().filename;
        File baseDirectory = new File(worldDirPath);
        baseDirectory.mkdir();
        //ACTUAL SAVING
        try {
            
            //Open file
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(FileUtil.CHARACTERS_PATH + "/"+ characterFile.getMetaData().filename + FileUtil.CHARACTER_FILE_EXTENSION), "utf-8"));
            //Write to file
            gson.toJson(characterFile, writer);
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
    private void writeFunctionalMap(FunctionalMap src, File target) throws IOException{
        byte content[] = new byte[FunctionalMap.SIZE*FunctionalMap.SIZE];
        for(int x=0;x<FunctionalMap.SIZE;x++){
            for(int y=0;y<FunctionalMap.SIZE;y++){
                content[x*FunctionalMap.SIZE + y] = src.getData()[x][y];
            }
        }
        Files.write(content, target);
    }
    

    
    public CharacterSaveFile loadCharacterSaveFile(String filename){
        String trueFileName = FileUtil.CHARACTERS_PATH + "/"+ filename + FileUtil.CHARACTER_FILE_EXTENSION;
        
        CharacterSaveFile loadedCharacter = null;
        try {
            GsonBuilder gb=new GsonBuilder();
            gb.registerTypeAdapter(CharacterSaveFile.class,worldDeSerializer);
            gb.registerTypeAdapter(Inventory.class,new InventoryDeserializer());
            Gson gson = gb.create();
            
            loadedCharacter = gson.fromJson(new FileReader(trueFileName), CharacterSaveFile.class);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return loadedCharacter;
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
    public static String getChunkFilePath(String worldname,String chunk){
        return FileUtil.SAVES_FOLDER+"/" + worldname + "/world/" + chunk + FileUtil.TILEDMAP_FILE_EXTENSION;
        
    }
    
}
