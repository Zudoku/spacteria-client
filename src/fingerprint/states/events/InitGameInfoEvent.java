/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.states.events;

import fingerprint.gameplay.objects.player.GCharacter;
import fingerprint.mainmenus.serverlist.RoomDescription;
import io.socket.client.Socket;

/**
 *
 * @author arska
 */
public class InitGameInfoEvent {
    private RoomDescription description;
    private GCharacter myCharacter;
    private String myID;
    private Socket socket;

    public InitGameInfoEvent(RoomDescription description, GCharacter myCharacter, String myID, Socket socket) {
        this.description = description;
        this.myCharacter = myCharacter;
        this.myID = myID;
        this.socket = socket;
    }

    public void setDescription(RoomDescription description) {
        this.description = description;
    }

    public RoomDescription getDescription() {
        return description;
    }

    public GCharacter getMyCharacter() {
        return myCharacter;
    }

    public void setMyCharacter(GCharacter myCharacter) {
        this.myCharacter = myCharacter;
    }

    public String getMyID() {
        return myID;
    }

    public Socket getSocket() {
        return socket;
    }
    
    
    
    
    
    
}
