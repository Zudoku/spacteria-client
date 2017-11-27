/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.items;

/**
 * Created Nov 21, 2017
 * @author arska
 */
public class Currencies {
    private int characterid;
    private int coin;
    private int bugbounty;
    private int rollticket;

    public int getBugbounty() {
        return bugbounty;
    }

    public int getCoin() {
        return coin;
    }

    public int getRollticket() {
        return rollticket;
    }

    public void setBugbounty(int bugbounty) {
        this.bugbounty = bugbounty;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setRollticket(int rollticket) {
        this.rollticket = rollticket;
    }

    public int getCharacterid() {
        return characterid;
    }

    public void setCharacterid(int characterid) {
        this.characterid = characterid;
    }
    
    
    
}
