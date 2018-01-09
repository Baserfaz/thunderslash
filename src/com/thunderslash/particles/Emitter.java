package com.thunderslash.particles;

import java.awt.Graphics;
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
    private BufferedImage smokeImg;
    
    public Emitter() {
        this.x = 0;
        this.y = 0;
        this.particles = new ArrayList<Particle>();
    
        SpriteCreator sc = Game.instance.getSpriteCreator();
        this.smokeImg = sc.CreateCustomSizeSprite(19, 6 * 32, 5, 5);
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
        
        Vector2 accel = this.getAcceleration(dir);
        float forceMult = 1.5f;
        
        // left to right
        float startXAccel = -0.25f;
        float currentXAccel = startXAccel;
        double stepSize = Math.abs(startXAccel * 2) / n;
        
        float additionalYAccel = 0f;
        
        for(int i = 0; i < n; i++) {
            Particle current = new Particle(this.x, this.y, this.smokeImg);
            this.particles.add(current);
            
            // add Y acceleration when the current
            // particle is near zero.
            // -> creates a dome effect.
            // TODO: atm. this is linear and creates a pyramid shape.
            if(currentXAccel < 0) {
                additionalYAccel = startXAccel - currentXAccel;
            } else if(currentXAccel > 0) {
                additionalYAccel = currentXAccel - Math.abs(startXAccel);
            }
                
            additionalYAccel *= 4f;
            
            current.enable();
            current.setPosition(this.x, this.y);
            current.setAcceleration(new Vector2(accel.x + currentXAccel, (accel.y * forceMult) + additionalYAccel));
            
            currentXAccel += stepSize; 
            
            // close to zero 
            // -> center particle
            if(currentXAccel < 0.05f && currentXAccel > -0.05f) {
                currentXAccel = 0.0f;
            }
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
