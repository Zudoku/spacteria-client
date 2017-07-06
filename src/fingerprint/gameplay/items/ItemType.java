package fingerprint.gameplay.items;

public enum ItemType {
    HELMET("Helmet"),
    PANTS("Pants"),
    SHOULDER("Shoulders"),
    WEAPON("Weapon"),
    CHEST("Chest armor"),
    BOOTS("Boots"),
    RING("Ring"),
    RELIC("Relic"),
    TOKEN("Token"),
    TRASH("Junk");

    private String text;

    ItemType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
