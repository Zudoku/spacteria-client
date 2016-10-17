/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.networking.events;

import fingerprint.mainmenus.serverlist.RoomDescription;

/**
 *
 * @author arska
 */
public class RefreshRoomDescEvent {
    private RoomDescription desc;
    boolean forceUpdate;

    public RefreshRoomDescEvent(RoomDescription desc, boolean forceUpdate) {
        this.desc = desc;
        this.forceUpdate = forceUpdate;
    }

    public RoomDescription getDesc() {
        return desc;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setDesc(RoomDescription desc) {
        this.desc = desc;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
    
    
    
}
