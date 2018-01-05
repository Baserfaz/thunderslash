package com.thunderslash.particles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;

public class Particle {

    private Point position = new Point(0, 0);
    private Point velocity = new Point(0, 0);
    private Point acceleration = new Point(0, 0);
    
    private BufferedImage img;
    private double currentLifeTime = 0.0;
    private double maxLifeTime = 10.0;
    
    private boolean enabled = false;
    
    public Particle(int x, int y, BufferedImage img) {
        this.position.x = x;
        this.position.y = y;
        this.img = img;
    }

    public void enable() {
        this.enabled = true;
        this.currentLifeTime = 0.0;
    }
    
    public void tick() {
        
        if(this.enabled == false) return;
        
        if(this.currentLifeTime < maxLifeTime) {
            
            this.currentLifeTime += Game.instance.getTimeBetweenFrames();
            
            this.velocity.x += this.acceleration.x;
            this.velocity.y += this.acceleration.y + Game.GRAVITY;
            
            this.position.x += velocity.x;
            this.position.y += velocity.y;
            
        } else {
            this.enabled = false;
        }
    }
    
    public void render(Graphics g) {
        if(this.enabled) g.drawImage(this.img, this.position.x, this.position.y, null);       
    }
    
    public int getX() { return this.position.x; }
    public void setX(int x) { this.position.x = x; }
    public int getY() { return this.position.y; }
    public void setY(int y) { this.position.y = y; }
    public BufferedImage getImg() { return img; }
    public void setImg(BufferedImage img) { this.img = img; }
    public Point getAcceleration() { return acceleration; }
    public void setAcceleration(Point acceleration) { this.acceleration = acceleration; }
    public Point getVelocity() { return velocity; }
    public void setVelocity(Point velocity) { this.velocity = velocity; }
    public void setVelocity(int ax, int ay) {
        this.velocity.x = ax;
        this.velocity.y = ay;
    }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }
}
