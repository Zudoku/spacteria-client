/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.particles;

import fingerprint.gameplay.objects.GameObject;
import fingerprint.rendering.manager.UIRenderingUtil;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;

/**
 * Created Dec 26, 2017
 * @author arska
 */
public class ParticleObject extends GameObject {
    
    protected ParticleSystem particleSystem;
    protected ConfigurableEmitter emitter;
    private String guid;
    

    public ParticleObject(double initX, double initY) {
        super(initX, initY);
        emitter = new ConfigurableEmitter("test");
    }

    @Override
    public void draw(Graphics graphics) {
        particleSystem.render((float)getDrawingCoordinates()[0], (float)getDrawingCoordinates()[1]);
    }

    public ParticleSystem getParticleSystem() {
        return particleSystem;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
    
}
