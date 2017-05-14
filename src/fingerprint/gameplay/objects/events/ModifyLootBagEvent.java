/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.events;

import fingerprint.gameplay.objects.lootbag.GameItemWrapper;
import fingerprint.gameplay.objects.lootbag.LootBag;
import java.util.List;

/**
 * Created May 13, 2017
 * @author arska
 */
public class ModifyLootBagEvent {
    private String guid;
    private List<GameItemWrapper> lootbag;

    public String getGuid() {
        return guid;
    }

    public List<GameItemWrapper> getLootbag() {
        return lootbag;
    }

    
}
