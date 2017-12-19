/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.networking;

/**
 *
 * @author arska
 */
public enum NetworkEnvironment {
    PRODUCTION("http://www.spacteria.com:3590"), 
    DEBUG1("http://192.168.1.141:3590"), 
    DEBUG2("http://127.0.0.1:3590");
    
    private String serverlURL;

    private NetworkEnvironment(String serverlURL) {
        this.serverlURL = serverlURL;
    }

    public String getServerlURL() {
        return serverlURL;
    }
    
}
