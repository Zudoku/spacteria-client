package fingerprint.states.menu.enums;


public enum MainMenuSelection {
    PLAY(0),OPTIONS(1),TUTORIAL(2),TROPHIES(3),CHARACTER_CREATION(4),EXIT(5);
    
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
        case TUTORIAL:
            return "Tutorial";
        case TROPHIES:
            return "Throphies";
        case CHARACTER_CREATION:
            return "Character Editor";
        case EXIT:
            return "Exit";
        }
        return "MENU_ITEM";
    }
    
    public int getIndex() {
        return index;
    }
}
