package fingerprint.states;


import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fingerprint.gameplay.map.generation.AreaShapeGenerator;

public class MainMenuState extends BasicGameState{

    private static final Logger logger = Logger.getLogger(MainMenuState.class.getName());
    
    @Override
    public void init(GameContainer arg0, StateBasedGame arg1)
            throws SlickException {
        AreaShapeGenerator g = new AreaShapeGenerator();
        int size = 256;
        BufferedImage image =g.imageDataFromFloatArray(g.maskGeneration(size),size);
        
        try {
            File outputfile = new File(System.getProperty("java.io.tmpdir")+"noise.png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Couldn't save RoadImage.");
            return;
        }
        
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame arg1, Graphics graphics)
            throws SlickException {
        
    }

    @Override
    public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
            throws SlickException {
        
    }

    @Override
    public int getID() {
        return State_IDs.MAIN_MENU_ID;
    }

}
