package com.thunderslash.particles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.Animation;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ParticleType;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.utilities.AnimationCreator;
import com.thunderslash.utilities.Mathf;
import com.thunderslash.utilities.RenderUtils;
import com.thunderslash.utilities.SpriteCreator;
import com.thunderslash.utilities.Vector2;

public class Emitter {

    private int x = 0, y = 0;
    private List<Particle> particles;
    private GameObject parentObject;
    private int defaultMaxParticleAmount = 50;
    
    // cached sprites
    private BufferedImage defaultParticleSprite;
    private BufferedImage goldParticleSprite;
    private BufferedImage dustParticleSprite;
    
    // cached animations
    private Animation dustCloudAnimation;
    
    public Emitter(GameObject parent) {
        this.particles = new ArrayList<Particle>();
        this.parentObject = parent;
        
        // create cached sprites
        SpriteCreator sc = Game.instance.getSpriteCreator();
        this.defaultParticleSprite = sc.CreateCustomSizeSprite(19, 6 * 32, 5, 5, 1);
        this.goldParticleSprite = RenderUtils.tintWithColor(this.defaultParticleSprite, Color.YELLOW);
        this.dustParticleSprite = RenderUtils.tintWithColor(this.defaultParticleSprite, Color.black);
        
        // create cached animations
        this.dustCloudAnimation = AnimationCreator.createDustCloudAnimation();
        
        Point pos = parent.getHitboxCenter();
        
        // populate particles list
        for(int i = 0; i < this.defaultMaxParticleAmount; i++) {
            this.particles.add(new Particle(pos.x, pos.y, this.defaultParticleSprite, null));
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
    
    public void emit(int n, Point offset, ParticleType particleType) {
        
        double horizontalMult = 0.5;
        double verticalMult = 2.0;
        
        if(offset == null) offset = new Point(0, 0);
        
        BufferedImage img = null;
        Animation anim = null;
        
        if(particleType == ParticleType.DUST_ANIM) {
            anim = this.dustCloudAnimation;
        } else {
            img = this.getSpriteFromParticleType(particleType);
        }
            
        for(int i = 0; i < n; i++) {
            Particle current = this.getFreeParticle();
            if(current != null) {
                
                Vector2 accel = new Vector2(
                        (float) (Mathf.randomRange(-1.0, 1.0) * horizontalMult),
                        (float) (Mathf.randomRange(-2.0, -1.0) * verticalMult));
                
                current.enable();
                current.setPosition(this.x + offset.x, this.y + offset.y);
                current.setAcceleration(accel);
                
                // set sprite and animation
                current.setSprite(img);
                current.setAnimation(anim);
            }
        }
    }
    
    private BufferedImage getSpriteFromParticleType(ParticleType type) {
        BufferedImage img = null;
        
        switch(type) {
        case DUST:
            img = this.dustParticleSprite;
            break;
        case GOLD:
            img = this.goldParticleSprite;
            break;
        case DEFAULT:
            img = this.defaultParticleSprite;
            break;
        default:
            System.out.println("Emitter::getSpriteFromParticleType: unsupported particle type: " + type);
            img = this.defaultParticleSprite;
            break;
        }
        
        return img;
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
    public void setSprite(BufferedImage sprite) { this.defaultParticleSprite = sprite; }
    public BufferedImage getSprite() { return this.defaultParticleSprite; }
    public List<Particle> getParticles() { return this.particles; }
}
