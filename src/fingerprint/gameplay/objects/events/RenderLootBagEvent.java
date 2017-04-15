package fingerprint.gameplay.objects.events;

import fingerprint.gameplay.objects.lootbag.LootBag;

public class RenderLootBagEvent {
    private LootBag lootBag;


    public RenderLootBagEvent(LootBag bag) {
        this.lootBag = bag;
    }

    public LootBag getLootBag() {
        return lootBag;
    }
}
