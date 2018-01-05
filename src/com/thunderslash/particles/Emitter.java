package com.thunderslash.particles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.utilities.SpriteCreator;

public class Emitter {

    private int x, y;
    private List<Particle> particles;
    
    public Emitter(int x, int y) {
        this.x = x;
        this.y = y;
        this.particles = new ArrayList<Particle>();
    
        SpriteCreator sc = Game.instance.getSpriteCreator();
        BufferedImage img = sc.CreateCustomSizeSprite(20, 6 * 32, 5, 5);
        
        // preload n amount of particles
        for(int i = 0; i < 20; i++) {
            this.particles.add(new Particle(this.x, this.y, img));
        }
    }
    
    public void emit(int n, Direction dir) {
        
        // TODO: add more particles if no particles are present
        if(n > this.particles.size()) n = this.particles.size();
        
        // acceleration direction
        int ax = 0, ay = 0;
        
        switch(dir) {
            case EAST:
                ax = 1;
                break;
            case NORTH:
                ay = -1;
                break;
            case SOUTH:
                ay = 1;
                break;
            case WEST:
                ax = -1;
                break;
                
            case NORTH_EAST:
                ax = 1;
                ay = -1;
                break;
            case NORTH_WEST:
                ax = -1;
                ay = -1;
                break;
            case SOUTH_EAST:
                ax = 1;
                ay = 1;
                break;
            case SOUTH_WEST:
                ax = -1;
                ay = 1;
                break;
        }
        
        for(int i = 0; i < n; i++) {
            Particle current = this.particles.get(i);
            current.setPosition(this.x, this.y);
            current.setVelocity(ax, ay);
            current.enable();
        }
    }
}
