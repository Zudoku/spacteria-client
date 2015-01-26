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

import fingerprint.gameplay.map.GameArea;
import fingerprint.gameplay.map.generation.AreaGenerator;
import fingerprint.gameplay.map.generation.AreaShapeGenerator;
import fingerprint.rendering.RenderingManager;

public class MainMenuState extends BasicGameState{

    private static final Logger logger = Logger.getLogger(MainMenuState.class.getName());
    private GameArea currentArea;
    private RenderingManager renderingManager;
    @Override
    public void init(GameContainer arg0, StateBasedGame arg1)
            throws SlickException {
        //TESTING 
        /**
        AreaShapeGenerator g = new AreaShapeGenerator();
        int size = 256;
        BufferedImage image =g.imageDataFromFloatArray(g.intArrayToFloatArray(g.maskGeneration(size,size,AreaShapeGenerator.GENERIC_AREA_SMOOTHNESS,
                AreaShapeGenerator.GENERIC_MASK_CUTOFF),size,size),size,size);
        
        try {
            File outputfile = new File(System.getProperty("java.io.tmpdir")+"noise.png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Couldn't save RoadImage.");
            return;
        }
        AreaGenerator r = new AreaGenerator();
        r.generateAreas();
        **/
        AreaGenerator r = new AreaGenerator();
        currentArea = r.generateAreas().get(0);
        renderingManager = new RenderingManager();
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame arg1, Graphics graphics)
            throws SlickException {
        renderingManager.draw(graphics, currentArea.getTileLayers());
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
