/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.rendering.util;

import io.socket.client.Socket;

/**
 * Created Aug 15, 2017
 * @author arska
 */
public class ConnectionRenderingInformation {
    private Socket socket;
    private String host;
    private String lastMessage;
    private String status;

    public ConnectionRenderingInformation(Socket socket, String host, String lastMessage, String status) {
        this.socket = socket;
        this.host = host;
        this.lastMessage = lastMessage;
        this.status = status;
    }

    public String getHost() {
        return host;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getStatus() {
        return status;
    }
    
    
}
