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
import fingerprint.gameplay.map.FunctionalMap;
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
        
        try {
            createWorldTemplate(metaData);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void createWorldTemplate(GameWorldMetaData metadata) throws IOException{
        //Base dir
        String worldDirPath = FileUtil.SAVES_FOLDER+"/"+metadata.filename;
        File baseDirectory = new File(worldDirPath);
        baseDirectory.mkdir();
        //world file
        File worldFile = new File(worldDirPath + "/data"+ FileUtil.WORLD_FILE_EXTENSION);
        worldFile.createNewFile();
        //Tile folder
        String tiledataPath = worldDirPath + "/world";
        File tileDataDirectory = new File(tiledataPath);
        tileDataDirectory.mkdir();
        //functionalmap
        File functionalMap = new File(tiledataPath + "/" + FileUtil.FUNCTIONAL_MAP_FILE_NAME + FileUtil.FUNCTIONAL_MAP_FILE_EXTENSION);
        functionalMap.createNewFile();
        //rendermap
        File renderingMap = new File(tiledataPath + "/" + FileUtil.RENDER_MAP_FILE_NAME + FileUtil.RENDER_MAP_FILE_EXTENSION);
        renderingMap.createNewFile();
    }
    
    private void preSave(GameWorld initWorld){
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
        preSave(worldToSave);
        //Create directory
        String worldDirPath = FileUtil.SAVES_FOLDER+"/"+worldToSave.getMetaData().filename;
        File baseDirectory = new File(worldDirPath);
        baseDirectory.mkdir();
        //ACTUAL SAVING
        try {
            
            //Open file
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(worldDirPath + "/data"+ FileUtil.WORLD_FILE_EXTENSION), "utf-8"));
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
        String tiledataPath = worldDirPath + "/world";
        File tileDataDirectory = new File(tiledataPath);
        tileDataDirectory.mkdir();
        
        File functionalMap = new File(tiledataPath + "/" + FileUtil.FUNCTIONAL_MAP_FILE_NAME + FileUtil.FUNCTIONAL_MAP_FILE_EXTENSION);
        try {
            writeFunctionalMap(worldToSave.getMap(), functionalMap);
        } catch (IOException e) {
           logger.log(Level.SEVERE,"Can't save functional map");
            e.printStackTrace();
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
    

    
    public GameWorld loadWorldGameFile(String filename){
        String trueFileName = "Saves/" + filename + "/data" + FileUtil.WORLD_FILE_EXTENSION;
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
        File functionalMapPath = new File(FileUtil.SAVES_FOLDER + "/" + filename + "/world/" + FileUtil.FUNCTIONAL_MAP_FILE_NAME + FileUtil.FUNCTIONAL_MAP_FILE_EXTENSION);
        try {
            byte[] content = ByteStreams.toByteArray(new FileInputStream(functionalMapPath));
            FunctionalMap functionalMap = new FunctionalMap(content);
            loadedWorld.setMap(functionalMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
    public static String getChunkFilePath(String worldname,String chunk){
        return FileUtil.SAVES_FOLDER+"/" + worldname + "/world/" + chunk + FileUtil.TILEDMAP_FILE_EXTENSION;
        
    }
    
}
