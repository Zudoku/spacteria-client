package fingerprint.inout;

import java.io.*;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import fingerprint.core.GameLauncher;
import fingerprint.gameplay.map.gameworld.CharacterSaveFile;
import fingerprint.gameplay.map.gameworld.CharacterSaveFileDeserializer;
import fingerprint.gameplay.map.gameworld.CharacterMetaData;
import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.networking.events.LoadNewMapEvent;
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

    /**
     * Save the world state to JSON file to Saves folder
     */
    public boolean saveTilemapFile(LoadNewMapEvent event){
        File savedFile = new File(FileUtil.TILEDMAPS_PATH + "/" + event.getName() + FileUtil.TILEDMAP_FILE_EXTENSION);

        try {
            String filecontent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
            filecontent += ("<map version=\"1.0\" orientation=\"orthogonal\" width=\"" + event.getWidth() + "\" height=\"" + event.getHeight() + "\" tilewidth=\"64\" tileheight=\"64\">\n");
            filecontent += " <tileset firstgid=\"1\" name=\"BasicTileset\" tilewidth=\"64\" tileheight=\"64\">\n";
            filecontent += "  <image source=\"tilemap.png\" trans=\"ff00ff\" width=\"1280\" height=\"2560\"/>\n";
            filecontent += " </tileset>\n";
            filecontent += (" <layer name=\"" + event.getType() + "\" width=\"" + event.getWidth() + "\" height=\"" + event.getHeight() + "\">\n");
            filecontent += "  <data encoding=\"base64\" compression=\"zlib\">\n";
            filecontent += ("   " + event.getMapdata() + "\n");
            filecontent += "  </data>\n";
            filecontent += " </layer>\n";
            filecontent += "</map>\n";


            Files.write(filecontent, savedFile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
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
