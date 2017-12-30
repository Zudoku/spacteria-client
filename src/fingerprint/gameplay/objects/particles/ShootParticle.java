/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.particles;

import fingerprint.inout.FileUtil;
import fingerprint.rendering.manager.UIRenderingUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * Created Dec 26, 2017
 * @author arska
 */
public class ShootParticle extends ParticleObject{
    
    
    public ShootParticle(double initX, double initY, float angle) {
        super(initX, initY);
        try {
            emitter = ParticleIO.loadEmitter(FileUtil.PARTICLES_PATH + "/shoot.xml");
            emitter.angularOffset.setValue(angle);
        } catch (IOException ex) {
            Logger.getLogger(ShootParticle.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

    @Override
    public void draw(Graphics graphics) {
        if(particleSystem == null) {
            particleSystem = new ParticleSystem(UIRenderingUtil.getSpriteImage(29));
            particleSystem.setPosition((float)getX(), (float)getY());
            particleSystem.addEmitter(emitter);
            particleSystem.setRemoveCompletedEmitters(true);
        }
        super.draw(graphics); 
    }
    
    

}
