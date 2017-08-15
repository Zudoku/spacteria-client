package fingerprint.rendering.gui.event;

import java.io.Serializable;

import org.newdawn.slick.Color;

public class DisplayConsoleMessageEvent implements Serializable{
    
    private String text;
    private Color color;
    
    
    public DisplayConsoleMessageEvent(String text, Color color) {
        this.text = text;
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }
    
    public String getText() {
        return text;
    }

}
