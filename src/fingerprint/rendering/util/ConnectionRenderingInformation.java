/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.rendering.util;

import fingerprint.networking.NetworkEnvironment;
import io.socket.client.Socket;

/**
 * Created Aug 15, 2017
 * @author arska
 */
public class ConnectionRenderingInformation {
    private final Socket socket;
    private final String host;
    private final String lastMessage;
    private final String status;
    
    private String version;
    private String changelog;
    private NetworkEnvironment environment;

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

    public void setEnvironment(NetworkEnvironment environment) {
        this.environment = environment;
    }

    public NetworkEnvironment getEnvironment() {
        return environment;
    }

    public String getStatus() {
        return status;
    }

    public String getChangelog() {
        return changelog;
    }

    public String getVersion() {
        return version;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
