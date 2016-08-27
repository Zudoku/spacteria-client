package fingerprint.states.menu.enums;

public enum CharacterClass {
    WARRIOR(5,2,12,5,5),MAGE(3,4,7,5,2),KNIGHT(6,1,10,5,7);
    
    private int baseHealth; //HP
    private int baseDexterity; //AS
    private int baseStrength; //DMG
    private int baseVitality; //HP REGEN
    private int baseDefence; //DEF
    private CharacterClass(int baseHealth, int baseDexterity, int baseStrength, int baseVitality, int baseDefence) {
        this.baseHealth = baseHealth;
        this.baseDexterity = baseDexterity;
        this.baseStrength = baseStrength;
        this.baseVitality = baseVitality;
        this.baseDefence = baseDefence;
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

    public int getBaseDexterity() {
        return baseDexterity;
    }

    public int getBaseHealth() {
        return baseHealth;
    }

    public int getBaseDefence() {
        return baseDefence;
    }

    public int getBaseStrength() {
        return baseStrength;
    }

    public int getBaseVitality() {
        return baseVitality;
    }
    
}
