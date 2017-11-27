/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.interact;

import java.util.List;

/**
 * Created Nov 16, 2017
 * @author arska
 */
public class Lines {
    private List<String> random;
    private List<String> interact;

    public List<String> getInteract() {
        return interact;
    }

    public void setInteract(List<String> interact) {
        this.interact = interact;
    }

    public void setRandom(List<String> random) {
        this.random = random;
    }

    public List<String> getRandom() {
        return random;
    }
}
