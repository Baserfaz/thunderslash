package com.thunderslash.gameobjects;

import java.awt.Point;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.SpriteType;

public class Enemy extends Actor {
    
    public Enemy(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
        
        // set enemy movement settings  
        this.maxHorizontalAccel = 0.1f * Game.SPRITESIZEMULT;
        this.maxHorizontalSpeed = 0.5f * Game.SPRITESIZEMULT;
        this.friction = 0f;
        
    }

    public void tick() {
        Player player = Game.instance.getActorManager().getPlayerInstance();
        Point p = player.getHitboxCenter();
        
        if(p.x < this.hitboxCenter.x) {
            this.direction.x = -1;
            this.direction.y = 1;
            
        } else if(p.x > this.hitboxCenter.x) {
            this.direction.x = 1;
            this.direction.y = 1;
        }
        
        super.tick();
    }
    
}
