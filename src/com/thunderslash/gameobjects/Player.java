package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thunderslash.data.Animation;
import com.thunderslash.data.Power;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ActorState;
import com.thunderslash.enumerations.AnimationType;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.AnimationCreator;
import com.thunderslash.utilities.RenderUtils;

public class Player extends Actor {
    
    private Power power;
    
    private Animation idleAnim;
    private Animation walkAnim;
    private Animation fallAnim;
    private Animation attackAnim;
    private Animation defendAnim;
    // private Animation castAnim;
    // private Animation jumpAnim;
    // private Animation useAnim;

    public Player(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
    
        this.power = new Power();
        
        // set animations
        this.idleAnim   = AnimationCreator.createAnimation(AnimationType.PLAYER_IDLE);
        this.walkAnim   = AnimationCreator.createAnimation(AnimationType.PLAYER_WALK);
        this.fallAnim   = AnimationCreator.createAnimation(AnimationType.PLAYER_FALL);
        this.attackAnim = AnimationCreator.createAnimation(AnimationType.PLAYER_ATTACK);
        this.defendAnim = AnimationCreator.createAnimation(AnimationType.PLAYER_DEFEND);
        
        // set animation timers / cooldowns
        this.attackCooldown = this.attackFrameTime * this.attackAnim.getAnimationLength();
        this.defendCooldown = this.attackFrameTime * this.defendAnim.getAnimationLength();
        
        // set stuff
        this.maxVerticalSpeed = 5.5f * Game.SPRITESIZEMULT;
        this.maxHorizontalSpeed = 1f * Game.SPRITESIZEMULT;
        this.maxVerticalAccel = 0.22f * Game.SPRITESIZEMULT;
        this.maxHorizontalAccel = 0.25f * Game.SPRITESIZEMULT;
        this.horizontalAccelMult = 0.35f * Game.SPRITESIZEMULT;
        this.jumpForce = -0.24f * Game.SPRITESIZEMULT;
        this.friction = 0.10f * Game.SPRITESIZEMULT;
        this.collisionDistance = 50f * Game.SPRITESIZEMULT;
        
    }
    
    public void tick() {
        
        this.handleStunState();
        if(this.isStunned) return;
        
        super.tick();
    }
    
    public void render(Graphics g) {
        
        BufferedImage frame = null;
        Animation currentAnim = null;
        
        if(this.actorState == ActorState.IDLING) {
            frame = this.idleAnim.getFrame(this.currentAnimIndex);
            currentAnim = this.idleAnim;
        } else if(this.actorState == ActorState.WALKING) {
            frame = this.walkAnim.getFrame(this.currentAnimIndex);
            currentAnim = this.walkAnim;
        } else if(this.actorState == ActorState.FALLING) {
            frame = this.fallAnim.getFrame(this.currentAnimIndex);
            currentAnim = this.fallAnim;
        } else if(this.actorState == ActorState.ATTACKING) {
            frame = this.attackAnim.getFrame(this.currentAnimIndex);
            currentAnim = this.attackAnim;
        } else if(this.actorState == ActorState.DEFENDING) {
            frame = this.defendAnim.getFrame(this.currentAnimIndex);
            currentAnim = this.defendAnim;
        }

        // updates animation index
        if(currentAnim != null) this.calculateAnimations(currentAnim);
        
        // fallback to default static sprite
        if(frame == null) frame = this.sprite;
        
        // render current sprite
        if(this.facingDirection == Direction.EAST) {
            g.drawImage(frame, this.worldPosition.x, this.worldPosition.y, null);
        } else if(this.facingDirection == Direction.WEST) {
            RenderUtils.renderSpriteFlippedHorizontally(frame, this.worldPosition, g);
        }
    }

    public Power getPower() {
        return power;
    }

    public void setPower(Power power) {
        this.power = power;
    }
}
