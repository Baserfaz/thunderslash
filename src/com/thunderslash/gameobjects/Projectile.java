package com.thunderslash.gameobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.RenderUtils;

public class Projectile extends GameObject {

    private Direction dir;
    private int damage = 1;
    private int moveSpeed = 3;
    
    public Projectile(Point worldPos, SpriteType type, Direction dir) {
        super(worldPos, type);
        this.dir = dir;
    }

    public void tick() {
        if(this.isEnabled == false) return;
        
        this.move();
        this.checkCollisions();
        this.updateHitbox();
        
        // TODO: animate
        
    }

    public void render(Graphics g) {
        
        if(this.isVisible) {
            if(this.dir == Direction.EAST) g.drawImage(this.defaultStaticSprite, this.worldPosition.x, this.worldPosition.y, null);
            else RenderUtils.renderSpriteFlippedHorizontally(this.defaultStaticSprite, this.worldPosition, g);
        }
        
        if(Game.drawGameObjectRects) {
            g.setColor(Color.red);
            g.drawRect(this.hitbox.x, this.hitbox.y, this.hitboxSizes.width, this.hitboxSizes.height);
        }
        
    }
    
    private void move() {
        if(this.dir == Direction.EAST) this.worldPosition.x += moveSpeed;
        else this.worldPosition.x -= moveSpeed;
    }

    private void checkCollisions() {
        
        boolean hit = false;
        
        for(GameObject go : this.getNearbyGameObjects(100, true)) {
            Rectangle r = go.getHitbox();
            if(this.hitbox.intersects(r)) {
                if(go instanceof Enemy) {
                    Enemy enemy = (Enemy) go;
                    enemy.getHP().takeDamage(this.damage);
                    hit = true;
                } else if(go instanceof Block && ((Block)go).getBlocktype() == BlockType.SOLID) {
                    hit = true;
                } 
                
                if(hit) {
                    this.isVisible = false;
                    this.isEnabled = false;
                    Game.instance.getHandler().RemoveObject(this);
                }
            }
        }
    }
    
}
