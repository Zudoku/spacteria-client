/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.states.events;

import io.socket.client.Socket;

/**
 * Created Mar 9, 2017
 * @author arska
 */
public class GiveSocketInfoEvent {
    private String myID;
    private Socket socket;
    private int state;

    public GiveSocketInfoEvent(String myID, Socket socket, int state) {
        this.myID = myID;
        this.socket = socket;
        this.state = state;
    }

    public String getMyID() {
        return myID;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getState() {
        return state;
    }
    
    

}
