package com.thunderslash.gameobjects;

import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.RenderUtils;

public class Roller extends Enemy {
    
    public Roller(String name, Point worldPos, int hp) {
        super(name, worldPos, SpriteType.ENEMY_ROLLER, true, hp);
        
        this.maxHorizontalAccel = 1f;
        this.maxHorizontalSpeed = 6f;
        this.horizontalAccelMult = 0.2f;
        this.friction = 0.1f;
    }
    
    public void onDeath() {
        BufferedImage img = Game.instance.getSpriteCreator().CreateSprite(SpriteType.ENEMY_ROLLER_DEAD);
        img = RenderUtils.tint(img, true, 2);
        this.setSprite(img);
    }
    
    public void doBehaviour() {
        if(this.isInvulnerable) this.acceleration.x = 0f;
        
        if(this.facingDirection == Direction.EAST) this.direction.x = 1f;
        else this.direction.x = -1f;
    }
    
    protected void onCollision(Direction dir) {
        if(dir == Direction.EAST) {
            this.facingDirection = Direction.WEST;
        } else if(dir == Direction.WEST) {
            this.facingDirection = Direction.EAST;
        }
    }

}
