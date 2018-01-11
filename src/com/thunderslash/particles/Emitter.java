package com.thunderslash.particles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.engine.Game;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.utilities.Mathf;
import com.thunderslash.utilities.SpriteCreator;
import com.thunderslash.utilities.Vector2;

public class Emitter {

    private int x = 0, y = 0;
    private List<Particle> particles;
    private BufferedImage particleSprite;
    private GameObject parentObject;
    
    private int defaultMaxParticleAmount = 50;
    
    public Emitter(GameObject parent) {
        this.particles = new ArrayList<Particle>();
        this.parentObject = parent;
        
        SpriteCreator sc = Game.instance.getSpriteCreator();
        this.particleSprite = sc.CreateCustomSizeSprite(19, 6 * 32, 5, 5, 1);
        
        Point pos = parent.getHitboxCenter();
        
        // populate particles list
        for(int i = 0; i < this.defaultMaxParticleAmount; i++) {
            this.particles.add(new Particle(pos.x, pos.y, this.particleSprite));
        }
    }
    
    public void tick() {
        // follow the parent object
        if(this.parentObject != null) {
            this.x = this.parentObject.getHitboxCenter().x;
            this.y = this.parentObject.getHitboxCenter().y;
        }
        for(Particle p : particles) { p.tick(); }
    }
    
    public void render(Graphics g) {
        for(Particle p : particles) { p.render(g); }
    }
    
    public void emit(int n) {
        
        double horizontalMult = 0.5;
        double verticalMult = 2.0;
                
        for(int i = 0; i < n; i++) {
            Particle current = this.getFreeParticle();
            if(current != null) {
                
                Vector2 accel = new Vector2(
                        (float) (Mathf.randomRange(-1.0, 1.0) * horizontalMult),
                        (float) (Mathf.randomRange(-2.0, -1.0) * verticalMult));
                
                current.enable();
                current.setPosition(this.x, this.y);
                current.setAcceleration(accel);
                current.setSprite(this.particleSprite);
            }
        }
    }
    
    private Particle getFreeParticle() {
        Particle p = null;
        for(Particle pp : this.particles) {
            if(pp.isEnabled() == false) {
                p = pp;
                break;
            }
        }
        return p;
    }
    // ----- SETTERS & GETTERS -----
    public int getMaxParticleAmount() { return this.defaultMaxParticleAmount; }
    public void setSprite(BufferedImage sprite) { this.particleSprite = sprite; }
    public BufferedImage getSprite() { return this.particleSprite; }
    public List<Particle> getParticles() { return this.particles; }
}
