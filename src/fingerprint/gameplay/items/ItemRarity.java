package fingerprint.gameplay.items;

import org.newdawn.slick.Color;

public enum ItemRarity {
    BASIC("Basic", Color.white),
    GOOD("Good", Color.green),
    GREAT("Great", Color.yellow),
    EPIC("Epic", Color.magenta);
    private String name;
    private Color color;

    ItemRarity(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
