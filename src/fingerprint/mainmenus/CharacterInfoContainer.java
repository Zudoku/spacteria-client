package fingerprint.mainmenus;

import fingerprint.gameplay.objects.player.Player;

public class CharacterInfoContainer {
    private boolean moreLeft;
    private boolean moreRight;
    
    private boolean isCreateNewCharDummy = false;
    
    private String filename;
    
    private Player playerData;

    /**
     * @return the moreLeft
     */
    public boolean isMoreLeft() {
        return moreLeft;
    }

    /**
     * @param moreLeft the moreLeft to set
     */
    public void setMoreLeft(boolean moreLeft) {
        this.moreLeft = moreLeft;
    }

    /**
     * @return the moreRight
     */
    public boolean isMoreRight() {
        return moreRight;
    }

    /**
     * @param moreRight the moreRight to set
     */
    public void setMoreRight(boolean moreRight) {
        this.moreRight = moreRight;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the playerData
     */
    public Player getPlayerData() {
        return playerData;
    }

    /**
     * @param playerData the playerData to set
     */
    public void setPlayerData(Player playerData) {
        this.playerData = playerData;
    }

    /**
     * @return the isCreateNewCharDummy
     */
    public boolean isIsCreateNewCharDummy() {
        return isCreateNewCharDummy;
    }

    /**
     * @param isCreateNewCharDummy the isCreateNewCharDummy to set
     */
    public void setIsCreateNewCharDummy(boolean isCreateNewCharDummy) {
        this.isCreateNewCharDummy = isCreateNewCharDummy;
    }

}
