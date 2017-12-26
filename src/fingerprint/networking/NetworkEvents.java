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
    
    //CLIENT LOGIN
    public static final String CLIENT_IDENTIFY = "identify";
    public static final String CLIENT_ASK_REGISTER = "register";
    
    
    //CLIENT MENU
    
    public static final String CLIENT_ASKTOJOINGAME = "joingame";
    public static final String CLIENT_MAKENEWROOM = "makeroom";
    public static final String CLIENT_REFRESHROOMLIST = "roomlist";
    public static final String CLIENT_REFRESHLEADERBOARDS = "listleaderboards";
    public static final String CLIENT_CREATE_CHARACTER = "createcharacter";
    public static final String CLIENT_LOAD_CHARACTER = "loadcharacter";
    public static final String CLIENT_DELETE_CHARACTER = "deletecharacter";
    public static final String CLIENT_CHARACTERLIST_REQUEST = "characterlist";
    
    //CLIENT GAME-EVENT PLAYER INTERACTION
    
    public static final String CLIENT_LOOT_ITEM = "lootgameitem";
    public static final String CLIENT_EQUIP_ITEM = "equipitem";
    public static final String CLIENT_UNEQUIP_ITEM = "unequipitem";
    public static final String CLIENT_DROP_ITEM = "dropitem";
    public static final String CLIENT_MOVE_ITEM_IN_INVENTORY = "moveitemininventory";
    public static final String CLIENT_SELL_ITEM = "sellitem";
    public static final String CLIENT_TELEPORT_TO_CAMP = "teleportcamp";
    public static final String CLIENT_ENTER_PORTAL = "enterportal";
    
    //CLIENT GAME-EVENT CORE
    
    public static final String CLIENT_UPDATE_POSITION = "updateposition";
    public static final String CLIENT_SPAWN_PROJECTILE = "spawnprojectile";
    public static final String CLIENT_MAP_LOADED = "maploaded";
    
    //CLIENT UTIL
    
    public static final String CLIENT_EMIT_CHAT_MESSAGE = "emitchatmsg";
    
    
    //SERVER LOGIN
    
    public static final String SERVER_LOGIN_SUCCESS = "loginsuccess";
    public static final String SERVER_LOGIN_FAIL = "loginfail";
    public static final String SERVER_GIVE_REGISTER_TOKEN = "giveregistertoken";
    public static final String SERVER_GIVE_LOGIN_TOKEN = "givelogintoken";
    
    //SERVER MENU
    
    public static final String SERVER_DISPLAYROOMLIST = "displayroomlist";
    public static final String SERVER_DISPLAYLEADERBOARDS = "displayleaderboards";
    public static final String SERVER_CHARACTERLIST = "displaycharacterlist";
    public static final String SERVER_CHARACTERLOAD_SUCCESS = "charloadsuccess";
    public static final String SERVER_JOINROOM = "joinroom";
    public static final String SERVER_BAD_CHARACTERNAME = "badcharactername";
    public static final String SERVER_CHARACTER_CREATED = "charactercreated";
    public static final String SERVER_VERSION_DATA = "versiondata";
    
    //SERVER CORE
    public static final String SERVER_PLAYERJOINEDMYGAME = "playerjoin";
    public static final String SERVER_PLAYERLEFTMYGAME = "playerleft";
    public static final String SERVER_CORRECTNPCPOSITION = "correctnpcposition";
    public static final String SERVER_PROJECTILE_SPAWNED = "newprojectilespawned";
    public static final String SERVER_LOOTBAG_SPAWNED = "newlootbagspawned";
    public static final String SERVER_ENEMY_SPAWNED = "newenemyspawned";
    public static final String SERVER_GAMEOBJECT_DESPAWNED = "despawngameobject";
    public static final String SERVER_REFRESH_ROOM_DESC = "refreshroomdesc";
    public static final String SERVER_UPDATE_CHARACTER_STATUS = "updatecharacterstatus";
    public static final String SERVER_UPDATE_LOOTBAG_STATUS = "updatelootbagstatus";
    public static final String SERVER_LOAD_NEW_MAP = "loadnewmap";
    public static final String SERVER_CHARACTERS_ALL_DEAD = "charactersalldead";
    
    //SERVER UTIL
    
    public static final String SERVER_CHAT_MESSAGE = "chatmsg";
    
}
