package fingerprint.gameplay.objects.events;

import fingerprint.gameplay.objects.lootbag.LootBag;

public class NewLootBagSpawnedEvent {
    private String guid;
    private LootBag lootbag;

    public String getGuid() {
        return guid;
    }

    public LootBag getLootbag() {
        return lootbag;
    }
    
    
}
