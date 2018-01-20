package fingerprint.states.menu.enums;


public enum MainMenuSelection {
    PLAY(0),OPTIONS(1),LEADERBOARD(2),TROPHIES(3), LOGINSCREEN(4), EXIT(5);
    
    int index;
    private MainMenuSelection(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        switch(this){
        case PLAY:
            return "Play";
        case OPTIONS:
            return "Options";
        case LEADERBOARD:
            return "Leaderboards";
        case TROPHIES:
            return "Throphies";
        case LOGINSCREEN:
            return "Back to login";
        case EXIT:
            return "Exit";
        }
        return "MENU_ITEM";
    }
    
    public int getIndex() {
        return index;
    }
}
