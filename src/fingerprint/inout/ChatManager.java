/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.inout;

import com.google.common.eventbus.Subscribe;
import fingerprint.rendering.gui.event.DisplayConsoleMessageEvent;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Color;

/**
 * Created Nov 16, 2017
 * @author arska
 */
public class ChatManager {
    
    
    private List<Chatline> chatlines = new ArrayList<>();

    public List<Chatline> getChatlines() {
        if(chatlines.size() > 5){
            return chatlines.subList(0, 5);
        }
        return chatlines;
    }
    
    public List<Chatline> getRelevantChatlines() {
        List<Chatline> result = new ArrayList<>();
        for(Chatline a : getChatlines()){
            if(System.currentTimeMillis() - (a.getCreated() + 15000) < 0){
                result.add(a);
            }
        }
        return result;
    }
    
    public void addLine(Chatline line){
        chatlines.add(0,line);
    }
    
    public void clear(){
        chatlines.clear();
    }
    
    @Subscribe
    public void listenDisplayConsoleMessageEvent(DisplayConsoleMessageEvent event){
        addLine(new Chatline(event.getText(), System.currentTimeMillis(), event.getColor()));
    }
    
    
    

}
