package com.thunderslash.gameobjects;

import java.awt.Point;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Mathf;

public class Enemy extends Actor {
    
    private double tickTimer = 0.0;
    private double tickCooldown = 200.0;
    private double activationRange = 670.0;
    
    private int killScore = 100;
    
    public Enemy(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
        
        // set enemy movement settings  
        this.maxHorizontalAccel = 5f;
        this.maxHorizontalSpeed = 3f;
        this.horizontalAccelMult = 3f;
        
        this.maxVerticalAccel = 10f;
        this.maxVerticalSpeed = 10f;
        
        this.friction = 0.1f;
        this.jumpForce = -0.75f;
        
        // randomize tick cooldown for all enemies.
        this.tickCooldown += Mathf.randomRange(0.0, 300.0);
        
    }

    public void tick() {
         if(this.isEnabled) {
             if(this.HP.isDead()) {
                 this.resetInputs();
             } else {
                
                 if(this.tickTimer > this.tickCooldown) {
                     this.tickTimer = 0.0;
                     this.doBehaviour();
                 } else {
                     this.resetInputs();
                     this.tickTimer += Game.instance.getTimeBetweenFrames();
                 }
             }
            
             // always tick physics
             super.tick();
         }
    }
    
    private void resetInputs() {
        this.direction.x = 0f;
        this.direction.y = 0f;
    }
    
    private void doBehaviour() {
        
        Player player = Game.instance.getActorManager().getPlayerInstance();
        Point p = player.getHitboxCenter();
        
        if(p.distance(this.hitboxCenter) < this.activationRange) {
            
            // enemy is in range to be activated
            
            // TODO: different behaviours.
            
            // x position
            if(p.x < this.hitboxCenter.x) this.direction.x = -1;
            else if(p.x > this.hitboxCenter.x) this.direction.x = 1;
            
            // y position
            this.direction.y = 1;
            
        } else {
            this.resetInputs();
        }
    }

    // ---- GETTERS & SETTERS ----
    public int getKillScore() { return killScore; }
    public void setKillScore(int killScore) { this.killScore = killScore; }
}
