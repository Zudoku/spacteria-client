package fingerprint.gameplay.map.gameworld;

import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import fingerprint.gameplay.objects.player.Player;


public class CharacterSaveFileDeserializer implements JsonDeserializer<CharacterSaveFile>{
    private static final Logger logger = Logger.getLogger(CharacterSaveFileDeserializer.class.getName());
    
    
    @Override
    public CharacterSaveFile deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        CharacterSaveFile savedFile = new CharacterSaveFile();
        final JsonObject jo = je.getAsJsonObject();
        //META DATA
        CharacterMetaData metaData=jdc.deserialize(jo.get("metaData"),CharacterMetaData.class);
        savedFile.setMetaData(metaData);

        //Player
        
        Player player = jdc.deserialize(jo.get("player"), Player.class);
        savedFile.setPlayer(player);

        return savedFile;
    }

}
