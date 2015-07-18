package fingerprint.states.menu.enums;

public enum GameDifficulty {
    EASY(200),REGULAR(150),HARD(100),VERY_HARD(75);
    
    private int maxLives;
    private GameDifficulty(int maxlives) {
        this.maxLives = maxlives;
    }
    public int getMaxLives() {
        return maxLives;
    }
    @Override
    public String toString() {
        switch(this){
        case EASY:
            return "Puppy";
        case REGULAR:
            return "Regular";
        case HARD:
            return "Hard";
        case VERY_HARD:
            return "Salmon snake";
        }
        return "DIFFICULTY_VALUE";
    }
}
