package fingerprint.gameplay.map.gameworld.init;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import fingerprint.gameplay.map.gameworld.GameWorld;
import fingerprint.gameplay.map.gameworld.GameWorldDeserializer;

public class GameFileLoader {
    private static final Logger logger = Logger.getLogger(GameFileLoader.class.getName());
    
    private static String savesFolder = "Saves";
    
    public GameFileLoader() {
        
    }
    
    public List<String> listSavedGames(){
        List<String> savedGames = new ArrayList<>();
        
        File folder = new File(savesFolder);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                savedGames.add(listOfFiles[i].getName());
            }
        }
        logger.log(Level.INFO,"{0} saved games available!",savedGames.size());
        return savedGames;
    }
    public GameWorld loadWorldFile(String filename){
        GameWorld loadedWorld = null;
        String filepathToSaveFile = savesFolder + filename + ".world";
        GsonBuilder gb=new GsonBuilder();
        gb.registerTypeAdapter(GameWorld.class, new GameWorldDeserializer());
        Gson gson = gb.create();
        try {
            loadedWorld = gson.fromJson(new FileReader(filepathToSaveFile), GameWorld.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return loadedWorld;
    }
}
