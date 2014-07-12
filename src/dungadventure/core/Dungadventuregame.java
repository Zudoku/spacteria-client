package dungadventure.core;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Dungadventuregame extends StateBasedGame {

    private static final int screenHeight = 800;
    private static final int screenWidth = 1200;
    
    private static final String PROGRAM_NAME = "Hannah's and Arttu's Nautical Dungeon Adventure";
    
    public Dungadventuregame(String title) {
        super(title);
        
    }
    public static void main(String[] arguments) {
        try {
            AppGameContainer app = new AppGameContainer(new Dungadventuregame(PROGRAM_NAME));
            app.setDisplayMode(screenWidth, screenHeight, false);
            app.setAlwaysRender(true);
            app.setTargetFrameRate(60);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
            
    }


}