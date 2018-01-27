package com.thunderslash.gameobjects;

import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.RenderUtils;

public class Slime extends Enemy {

    public Slime(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
        
        // set enemy movement settings  
        this.maxHorizontalAccel = 5f;
        this.maxHorizontalSpeed = 3f;
        this.horizontalAccelMult = 3f;
        
        this.maxVerticalAccel = 10f;
        this.maxVerticalSpeed = 10f;
        
        this.friction = 0.1f;
        this.jumpForce = -0.75f;
        
    }

    public void onDeath() {
        BufferedImage img = Game.instance.getSpriteCreator().CreateSprite(SpriteType.ENEMY_SLIME_DEAD);
        img = RenderUtils.tint(img, true, 2);
        this.setSprite(img);
    }

    public void doBehaviour() {
        Player player = Game.instance.getActorManager().getPlayerInstance();
        Point p = player.getHitboxCenter();
        
        if(p.distance(this.hitboxCenter) < Game.ENEMY_ACTIVATION_RANGE) {
            
            // x position
            if(p.x < this.hitboxCenter.x) this.direction.x = -1;
            else if(p.x > this.hitboxCenter.x) this.direction.x = 1;
            
            // y position
            this.direction.y = 1;
            
        } else {
            this.resetInputs();
        }
    }
}
