package com.thunderslash.gameobjects;

import java.awt.Point;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Mathf;

public abstract class Enemy extends Actor {
    
    protected double tickTimer = 0.0;
    protected double tickCooldown = 200.0;
    protected double activationRange = 670.0;
    
    protected int killScore = 100;
    
    public Enemy(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
        
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
    
    public abstract void onDeath();
    public abstract void doBehaviour();
    
    protected void resetInputs() {
        this.direction.x = 0f;
        this.direction.y = 0f;
    }

    // ---- GETTERS & SETTERS ----
    public int getKillScore() { return killScore; }
    public void setKillScore(int killScore) { this.killScore = killScore; }
}
