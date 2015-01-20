package fingerprint.gameplay.map.gameworld;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GameWorldDeserializer implements JsonDeserializer<GameWorld>{
    private static final Logger logger = Logger.getLogger(GameWorldDeserializer.class.getName());
    @Override
    public GameWorld deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        GameWorld gameWorld = null;
        return gameWorld;
    }

}
