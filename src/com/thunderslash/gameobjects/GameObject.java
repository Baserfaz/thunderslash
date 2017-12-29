package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Coordinate;

public abstract class GameObject {

    protected Coordinate worldPosition;
    protected BufferedImage sprite;
    
    // hitboxes
    protected Rectangle hitbox;
    protected Rectangle hitboxSizes;
    protected Coordinate hitboxCenter;
    
    protected boolean isEnabled = true;
    protected boolean isVisible = true;
    
    public GameObject(Coordinate worldPos, SpriteType type) {

        // set world position
        this.worldPosition = worldPos;

        // default hitbox
        this.hitbox = new Rectangle(worldPos.x, worldPos.y,
                Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT,
                Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT);
        
        // create sprite
        this.sprite = Game.instance.getSpriteCreator().CreateSprite(type);
        
        // add to handler
        Game.instance.getHandler().AddObject(this);
        
        this.recalculateBoundingBox();
        
        this.hitboxCenter = new Coordinate(this.hitbox.x + this.hitbox.width / 2, 
                this.hitbox.y + this.hitbox.height / 2);
        
    }

    public String getInfo() {
        return "GameObject: " + this.toString() + " worldPos: (" +
                this.getWorldPosition().x + ", " + this.getWorldPosition().y + ")";
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public Rectangle getBounds() { return this.hitbox; }
    
    public void recalculateBoundingBox() {
                
        int[] pixels = sprite.getRGB(0, 0, sprite.getWidth(),
                sprite.getHeight(), null, 0, sprite.getWidth());
        
        int x = this.worldPosition.x, y = this.worldPosition.y;
        int w = sprite.getWidth(), h = sprite.getHeight();
        
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
    
    public void setSprite(BufferedImage i) { this.sprite = i; }
    
    public BufferedImage getSprite() { return this.sprite; }
    
    public void addWorldPosition(float x, float y) { 
        this.worldPosition = new Coordinate(this.worldPosition.x + Math.round(x),
                this.worldPosition.y + Math.round(y));
    }
    
    public void setWorldPosition(int x, int y) { this.worldPosition = new Coordinate(x, y); }
    public void setWorldPosition(Coordinate pos) { this.worldPosition = pos; }
    public Coordinate getWorldPosition() { return this.worldPosition; }
    
    public boolean getIsVisible() { return this.isVisible; }
    public boolean getIsEnabled() { return this.isEnabled; }
    
    public void setIsVisible(boolean b) { this.isVisible = b; }
    public void setIsEnabled(boolean b) { this.isEnabled = b; }


}
