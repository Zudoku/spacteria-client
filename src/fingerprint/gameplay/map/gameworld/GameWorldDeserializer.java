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


public class GameWorldDeserializer implements JsonDeserializer<GameWorld>{
    private static final Logger logger = Logger.getLogger(GameWorldDeserializer.class.getName());
    
    
    @Override
    public GameWorld deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        GameWorld gameWorld = new GameWorld();
        logger.log(Level.FINEST,"Beginning to deserialize gameworld!");
        final JsonObject jo = je.getAsJsonObject();
        //META DATA
        logger.log(Level.FINEST,"Deserializing META-DATA...");
        GameWorldMetaData metaData=jdc.deserialize(jo.get("metaData"),GameWorldMetaData.class);
        gameWorld.setMetaData(metaData);
        
        logger.log(Level.FINEST,"Deserializing objects...");
        //Objects
        
        logger.log(Level.FINEST,"Deserializing player...");
        //Player
        
        Player player = jdc.deserialize(jo.get("player"), Player.class);
        gameWorld.setPlayer(player);
        
        
        
        
        logger.log(Level.FINEST,"GameWorld deserialized!");
        return gameWorld;
    }

}
