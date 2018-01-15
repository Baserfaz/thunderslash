package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.Animation;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.particles.Emitter;

public abstract class GameObject {

    protected Point worldPosition;
    protected BufferedImage defaultStaticSprite;
    
    protected boolean hasFocus = false;
    
    protected Rectangle hitbox;
    protected Rectangle hitboxSizes;
    protected Point hitboxCenter;
    
    protected boolean isEnabled = false;
    protected boolean isVisible = false;
    
    // animation
    protected double frameTime = 0.0;
    protected double defaultFrameTime = 100.0;
    
    protected double currentFrameTime = 0.0;
    protected int currentAnimIndex = 0;
    
    protected Emitter particleEmitter;
    
    public GameObject(Point worldPos, SpriteType type) {

        // set world position
        this.worldPosition = worldPos;

        // default hitbox
        this.hitbox = new Rectangle(worldPos.x, worldPos.y,
                Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT,
                Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT);
        
        // create sprite
        this.defaultStaticSprite = Game.instance.getSpriteCreator().CreateSprite(type);
        
        // add to handler
        Game.instance.getHandler().AddObject(this);
        
        this.recalculateBoundingBox();
        
        this.hitboxCenter = new Point(this.hitbox.x + this.hitbox.width / 2, 
                this.hitbox.y + this.hitbox.height / 2);
        
        this.frameTime = this.defaultFrameTime;
        
        this.particleEmitter = Game.instance.getEmitterManager().createEmitter(this);
        
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    
    // ------------------ Helppers -------------------
    
    protected List<GameObject> getNearbyGameObjects(float distance, boolean allowBlocks) {
        List<GameObject> objs = new ArrayList<GameObject>();
        for(GameObject go : Game.instance.getHandler().getObjects()) {
            if(go instanceof Player) continue;
            if(allowBlocks == false && go instanceof Block) continue;
            if(go.hitboxCenter.distance(this.hitboxCenter) < distance) objs.add(go); 
        }
        return objs;
    }
    
    protected void updateHitbox() {
        this.hitbox.x = this.worldPosition.x + this.hitboxSizes.x;
        this.hitbox.y = this.worldPosition.y + this.hitboxSizes.y;
        this.hitboxCenter = new Point(this.hitbox.x + this.hitbox.width / 2, 
                this.hitbox.y + this.hitbox.height / 2);
    }
    
    protected void calculateAnimations(Animation anim) {
        double dt = Game.instance.getTimeBetweenFrames();
        if(this.currentFrameTime > this.frameTime) {
            this.currentFrameTime = 0.0;
            this.currentAnimIndex += 1;
            if(this.currentAnimIndex >= anim.getAnimationLength()) {
                this.currentAnimIndex = 0;
            }
        }
        this.currentFrameTime += dt;
    }
    
    public void recalculateBoundingBox() {
                
        int[] pixels = defaultStaticSprite.getRGB(0, 0, defaultStaticSprite.getWidth(),
                defaultStaticSprite.getHeight(), null, 0, defaultStaticSprite.getWidth());
        
        int x = this.worldPosition.x, y = this.worldPosition.y;
        int w = defaultStaticSprite.getWidth(), h = defaultStaticSprite.getHeight();
        
        int largestX = 0;
        int smallestX = w;
        int largestY = 0;
        int smallestY = h;
        
        for(int i = 0; i < pixels.length; i++) {
         
            int current = pixels[i];
            int alpha = (current & 0xff000000) >>> 24;
            
            if(alpha == 255) {
                
                // pixel position in the sprite
                int yy = i / w;
                int xx = i % w;
                
                if(yy < smallestY) {
                    smallestY = yy;
                } else if(yy > largestY) {
                    largestY = yy;
                }
                
                if(xx > largestX) {
                    largestX = xx;
                } else if(xx < smallestX) {
                    smallestX = xx;
                }
                
            }
        }
        
        y += smallestY;
        x += smallestX;
        w = largestX - smallestX;
        h = largestY - smallestY;
        
        // update hitbox 
        this.setBoundingBoxSize(x, y, w, h);
        
        // store hitbox sizes
        this.hitboxSizes = new Rectangle(smallestX, smallestY, w, h);
        
    }
    
    public void setBoundingBoxSize(int x, int y, int w, int h) {
        this.hitbox.x = x;
        this.hitbox.y = y;
        this.hitbox.width = w;
        this.hitbox.height = h;
    }
    
    public String getInfo() {
        return "GameObject: " + this.toString() + " worldPos: (" +
                this.getWorldPosition().x + ", " + this.getWorldPosition().y + ")";
    }

    public void addWorldPosition(float x, float y) { 
        this.worldPosition = new Point(this.worldPosition.x + Math.round(x),
                this.worldPosition.y + Math.round(y));
    }
    
    // ---- GETTERS & SETTERS ----
    public Rectangle getHitbox() { return this.hitbox; }
    public Point getHitboxCenter() { return this.hitboxCenter; }
    public void setSprite(BufferedImage i) { this.defaultStaticSprite = i; }
    public BufferedImage getSprite() { return this.defaultStaticSprite; }
    public void setWorldPosition(int x, int y) { this.worldPosition = new Point(x, y); }
    public void setWorldPosition(Point pos) { this.worldPosition = pos; }
    public Point getWorldPosition() { return this.worldPosition; }
    public boolean getIsVisible() { return this.isVisible; }
    public boolean getIsEnabled() { return this.isEnabled; }
    public void setIsVisible(boolean b) { this.isVisible = b; }
    public void setIsEnabled(boolean b) { this.isEnabled = b; }
    
    public void activate() { 
        this.isVisible = true;
        this.isEnabled = true;
    }
    
    public void deactivate() {
        this.isVisible = false;
        this.isEnabled = false;
    }

}
