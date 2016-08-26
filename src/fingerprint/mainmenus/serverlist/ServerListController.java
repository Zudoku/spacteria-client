/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprint.mainmenus.serverlist;

/**
 *
 * @author arska
 */
public class ServerListController {
    private int selection = 0;
    private int roomAmount;
    
    public void up(){
        if(getSelection() > 0){
            setSelection(getSelection() - 1);
        }else{
            setSelection(getRoomAmount());
        }
    }
    public void down(){
        if(getSelection() < getRoomAmount()){
            setSelection(getSelection() + 1);
        }else{
            setSelection(0);
        }
    }

    /**
     * @return the selection
     */
    public int getSelection() {
        return selection;
    }

    /**
     * @param selection the selection to set
     */
    public void setSelection(int selection) {
        this.selection = selection;
    }

    /**
     * @return the roomAmount
     */
    public int getRoomAmount() {
        return roomAmount;
    }

    /**
     * @param roomAmount the roomAmount to set
     */
    public void setRoomAmount(int roomAmount) {
        this.roomAmount = roomAmount;
    }
    
    
}
