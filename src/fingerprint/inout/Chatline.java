/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.inout;

import org.newdawn.slick.Color;

/**
 * Created Nov 16, 2017
 * @author arska
 */
public class Chatline {
    private String line;
    private long created;
    private Color color;

    public Chatline(String line, long created, Color color) {
        this.line = line;
        this.created = created;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public long getCreated() {
        return created;
    }

    public String getLine() {
        return line;
    }
    
    
}
