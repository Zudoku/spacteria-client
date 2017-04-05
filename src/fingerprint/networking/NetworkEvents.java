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
    public static final String CLIENT_CHARACTERLIST_REQUEST = "characterlist";
    public static final String CLIENT_CREATE_CHARACTER = "createcharacter";
    public static final String CLIENT_LOAD_CHARACTER = "loadcharacter";
    public static final String CLIENT_DELETE_CHARACTER = "deletecharacter";
    
    public static final String SERVER_DISPLAYROOMLIST = "displayroomlist";
    public static final String SERVER_CHARACTERLIST = "displaycharacterlist";
    public static final String SERVER_CHARACTERLOAD_SUCCESS = "charloadsuccess";
    public static final String SERVER_JOINROOM = "joinroom";
    public static final String SERVER_PLAYERJOINEDMYGAME = "playerjoin";
    public static final String SERVER_PLAYERLEFTMYGAME = "playerleft";
    public static final String SERVER_CORRECTNPCPOSITION = "correctnpcposition";
    public static final String SERVER_PROJECTILE_SPAWNED = "newprojectilespawned";
    public static final String SERVER_PROJECTILE_DESPAWNED = "despawnprojectile";
    public static final String SERVER_REFRESH_ROOM_DESC = "refreshroomdesc";
    public static final String SERVER_LOGIN_SUCCESS = "loginsuccess";
    public static final String SERVER_LOGIN_FAIL = "loginfail";
    
    //public static final String SERVER_UPDATE_NPC_POSITION= "updatenpcposition";
}
