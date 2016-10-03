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
public class NetworkEvents {
    
    public static final String CLIENT_ASKTOJOINGAME = "joingame";
    public static final String CLIENT_MAKENEWROOM = "makeroom";
    public static final String CLIENT_REFRESHROOMLIST = "roomlist";
    public static final String CLIENT_IDENTIFY = "identify";
    public static final String CLIENT_UPDATE_POSITION = "updateposition";
    public static final String CLIENT_SPAWN_PROJECTILE = "spawnprojectile";
    
    public static final String SERVER_DISPLAYROOMLIST = "displayroomlist";
    public static final String SERVER_JOINROOM = "joinroom";
    public static final String SERVER_PLAYERJOINEDMYGAME = "playerjoin";
    public static final String SERVER_PLAYERLEFTMYGAME = "playerleft";
    public static final String SERVER_CORRECTPLAYERPOSITION = "correctplayerposition";
    public static final String SERVER_PROJECTILE_SPAWNED = "newprojectilespawned";
    public static final String SERVER_PROJECTILE_DESPAWNED = "despawnprojectile";
}
