package fingerprint.gameplay.objects;

import fingerprint.gameplay.objects.lootbag.LootBag;

public class GGameObjectWrapper {
    private int type;
    private int x;
    private int y;
    private String hash;
    private Portal portal;
    private LootBag lootbag;

    public GameObject getGObject() {
        switch (type) {
            case 1:
                return lootbag;
            case 2:
                return portal;
        }

        return null;
    }

    public int getType() {
        return type;
    }

    public String getHash() {
        return hash;
    }
}
