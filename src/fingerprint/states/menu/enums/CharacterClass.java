package fingerprint.states.menu.enums;

public enum CharacterClass {
    WARRIOR(200,200),MAGE(150,250),KNIGHT(300,100);
    
    private int baseHealth;
    private int baseDexterity;
    private CharacterClass(int baseHealth, int baseDexterity) {
        this.baseHealth = baseHealth;
        this.baseDexterity = baseDexterity;
    }
    
    @Override
    public String toString() {
        switch(this){
        case WARRIOR:
            return "Warrior";
        case MAGE:
            return "Mage";
        case KNIGHT:
            return "Knight";
        }
        return "CLASS_VALUE";
    }
}
