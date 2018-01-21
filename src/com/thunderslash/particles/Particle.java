package com.thunderslash.particles;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.thunderslash.data.Animation;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.utilities.Vector2;

public class Particle {

    private Point position = new Point(0, 0);
    private Vector2 velocity = new Vector2(0f, 0f);
    private Vector2 acceleration = new Vector2(0f, 0f);
    
    private Rectangle collider = new Rectangle();
    
    private Animation animation;
    private BufferedImage sprite;
    
    private BufferedImage frame;
    
    private double frameTime = 50.0;
    private double currentFrameTime = 0.0;
    private int frameIndex = 0;
    
    private double currentLifeTime = 0.0;
    private double maxLifeTime = 10000.0;
    
    private boolean enabled = false;
    
    public Particle(int x, int y, BufferedImage img, Animation animation) {
        this.position.x = x;
        this.position.y = y;
        
        if(img != null) {
            this.sprite = img;
            this.frame = this.sprite;
        } else if(animation != null) {
            this.animation = animation;
            this.frame = this.animation.getFrame(0);
        } else System.out.println("Particle::Constructor: no image or animation!");
    }

    public void enable() {
        this.enabled = true;
        this.currentLifeTime = 0.0;
        this.velocity.x = 0f;
        this.velocity.y = 0f;
        this.acceleration.x = 0f;
        this.acceleration.y = 0f;
    }
    
    public void tick() {
        if(this.enabled) {
            
            if(this.sprite != null) {
                if(this.currentLifeTime < maxLifeTime) {
                    
                    this.currentLifeTime += Game.instance.getTimeBetweenFrames();
                    this.move();
                    this.checkCollisions();
                    
                } else {
                    this.enabled = false;
                }
                
            } else if(this.animation != null) {
                
                this.move();
                //this.checkCollisions();
                
                if(this.currentFrameTime > this.frameTime) {
                    this.currentFrameTime = 0.0;
                    this.frameIndex += 1;
                    
                    if(frameIndex == this.animation.getAnimationLength()) {
                        this.enabled = false;
                        this.frameIndex = 0;
                    } else {
                        this.frame = this.animation.getFrame(this.frameIndex);
                    }
                    
                } else {
                    this.currentFrameTime += Game.instance.getTimeBetweenFrames();
                }
                
            }
        }
    }
    
    public void render(Graphics g) {
        if(this.enabled) g.drawImage(this.frame, this.position.x, this.position.y, null);       
    }
    
    private void checkCollisions() {
        
        // update collider location
        this.collider = new Rectangle(this.position.x, this.position.y, this.sprite.getWidth(), this.sprite.getHeight());

        for(Block block : Game.instance.getWorld().getCurrentRoomBlocks()) {
            if(block.getIsEnabled() && block.getIsVisible()) {
                if(block.getBlocktype() == BlockType.SOLID) {
                    if(collider.intersects(block.getHitbox())) {
                        this.enabled = false;
                        break;
                    }
                }
            }
        }
    }
    
    private void move() {
        
        this.velocity.x += this.acceleration.x;
        this.velocity.y += this.acceleration.y + Game.GRAVITY;
        this.position.x += velocity.x;
        this.position.y += velocity.y;
        
        this.acceleration.y = 0f;
        
        if(this.acceleration.x > 0f) this.acceleration.x -= 0.1f;
        else this.acceleration.x += 0.1f;
        
    }
    
    // ---- GETTERS & SETTERS -----
    public int getX() { return this.position.x; }
    public void setX(int x) { this.position.x = x; }
    public int getY() { return this.position.y; }
    public void setY(int y) { this.position.y = y; }
    public void setAnimation(Animation anim) { this.animation = anim; }
    public Animation getAnimation() { return this.animation; }
    public BufferedImage getSprite() { return sprite; }
    public void setSprite(BufferedImage sprite) { this.sprite = sprite; }
    public Vector2 getAcceleration() { return acceleration; }
    public void setAcceleration(Vector2 acceleration) { this.acceleration = acceleration; }
    public Vector2 getVelocity() { return velocity; }
    public void setVelocity(Vector2 velocity) { this.velocity = velocity; }
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
