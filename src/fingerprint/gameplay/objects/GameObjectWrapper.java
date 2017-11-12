package fingerprint.gameplay.objects;

import fingerprint.gameplay.objects.interact.Portal;
import fingerprint.gameplay.objects.interact.NPC;
import fingerprint.gameplay.objects.interact.LootBag;

public class GameObjectWrapper {
    private int type;
    private int x;
    private int y;
    private String hash;
    private Portal portal;
    private LootBag lootbag;
    private NPC npc;

    public GameObject getGObject() {
        switch (type) {
            case 1:
                return lootbag;
            case 2:
                return portal;
            case 3:
                return npc;
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
