package fingerprint.rendering;



import org.newdawn.slick.Color;

import com.google.common.eventbus.Subscribe;

public class UIManager {
    
    private String nextConsoleText = "Testing a longer console message";
    private Color textColor = Color.black;
    
    
    public UIManager() {
        // TODO Auto-generated constructor stub
    }
    
    public String getNextConsoleText() {
        return nextConsoleText;
    }
    
    @Subscribe
    public void listenDisplayConsoleMessageEvent(DisplayConsoleMessageEvent event){
        nextConsoleText = event.getText();
        textColor = event.getColor();
    }

}
