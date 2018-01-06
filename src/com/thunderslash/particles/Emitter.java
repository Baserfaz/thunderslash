package com.thunderslash.particles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.utilities.SpriteCreator;
import com.thunderslash.utilities.Vector2;

public class Emitter {

    private int x, y;
    private List<Particle> particles;
    
    private BufferedImage img;
    
    public Emitter() {
        this.x = 0;
        this.y = 0;
        this.particles = new ArrayList<Particle>();
    
        SpriteCreator sc = Game.instance.getSpriteCreator();
        this.img = sc.CreateCustomSizeSprite(20, 6 * 32, 5, 5);
        
        // preload n amount of particles
        for(int i = 0; i < 20; i++) {
            this.particles.add(new Particle(this.x, this.y, this.img));
        }
    }
    
    public void tick() {
        for(Particle p : particles) {
            p.tick();
        }
    }
    
    public void render(Graphics g) {
        for(Particle p : particles) {
            p.render(g);
        }
    }
    
    public void emit(int n, Direction dir, int x, int y) {
        
        this.x = x;
        this.y = y;
        
        if(n > this.particles.size()) {
            int more = n - this.particles.size();
            for(int i = 0; i < more; i++) {
                this.particles.add(new Particle(this.x, this.y, this.img));
            }
        }
        
        Vector2 accel = this.getAcceleration(dir);
        
        for(int i = 0; i < n; i++) {
            Particle current = this.particles.get(i);
            current.setPosition(this.x, this.y);
            current.setAcceleration(accel);
            current.enable();
        }
    }
    
    private Vector2 getAcceleration(Direction dir) {
        
        float ax = 0f, ay = 0f;
        
        switch(dir) {
        case EAST:
            ax = 1f;
            break;
        case NORTH:
            ay = -1f;
            break;
        case SOUTH:
            ay = 1f;
            break;
        case WEST:
            ax = -1f;
            break;
            
        case NORTH_EAST:
            ax = 1f;
            ay = -1f;
            break;
        case NORTH_WEST:
            ax = -1f;
            ay = -1f;
            break;
        case SOUTH_EAST:
            ax = 1f;
            ay = 1f;
            break;
        case SOUTH_WEST:
            ax = -1f;
            ay = 1f;
            break;
        }
        return new Vector2(ax, ay);
    }
    
    public List<Particle> getParticles() {
        return this.particles;
    }
    
}
