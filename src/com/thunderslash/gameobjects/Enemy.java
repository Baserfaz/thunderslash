package com.thunderslash.gameobjects;

import java.awt.Rectangle;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Coordinate;

public class Enemy extends Actor {
    
    public Enemy(String name, Coordinate worldPos, SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
        
        // set enemy movement settings  
        this.maxHorizontalAccel = 0.1f * Game.SPRITESIZEMULT;
        this.maxHorizontalSpeed = 0.5f * Game.SPRITESIZEMULT;
        this.friction = 0f;
        
    }

    public void tick() {
        Player player = Game.instance.getActorManager().getPlayerInstance();
        Coordinate p = player.getHitboxCenter();
        
        if(p.x < this.hitboxCenter.x) {
            this.direction.x = -1;
            
        } else if(p.x > this.hitboxCenter.x) {
            this.direction.x = 1;
        }
        
        super.tick();
    }
    
}
