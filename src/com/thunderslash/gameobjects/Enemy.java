package com.thunderslash.gameobjects;

import java.awt.Point;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.SpriteType;

public class Enemy extends Actor {
    
    public Enemy(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
        
        // set enemy movement settings  
        this.maxHorizontalAccel = 0.2f * Game.SPRITESIZEMULT;
        this.maxHorizontalSpeed = 0.5f * Game.SPRITESIZEMULT;
        this.friction = 0.1f;
        
    }

    public void tick() {
        
        //this.handleStunState();
        //if(this.isStunned) return;
         
        if(this.HP.isDead()) {
            this.direction.x = 0f;
            this.direction.y = 0f;
        } else {
            this.doBehaviour();
        }
        
        // always tick physics
        super.tick();
    }
    
    private void doBehaviour() {
        
        Player player = Game.instance.getActorManager().getPlayerInstance();
        Point p = player.getHitboxCenter();
        
        // TODO: if can see player
        
        // x position
        if(p.x < this.hitboxCenter.x) this.direction.x = -1;
        else if(p.x > this.hitboxCenter.x) this.direction.x = 1;
        
        // y position
        if(p.y < this.hitboxCenter.y) this.direction.y = 1;
        else if(p.y > this.hitboxCenter.y) this.direction.y = -1;
        
    }
}
