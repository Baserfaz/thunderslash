package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thunderslash.data.Animation;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ActorState;
import com.thunderslash.enumerations.AnimationType;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.AnimationCreator;
import com.thunderslash.utilities.RenderUtils;

public class Player extends Actor {
    
    private Animation idleAnim;
//    private Animation walkAnim;
//    private Animation attackAnim;
//    private Animation fallAnim;
//    private Animation castAnim;

    public Player(String name, Point worldPos, 
            SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
    
        // set animations
        this.idleAnim = AnimationCreator.createAnimation(AnimationType.PLAYER_IDLE);
        
        // set stuff
        this.maxVerticalSpeed = 5.5f * Game.SPRITESIZEMULT;
        this.maxHorizontalSpeed = 1f * Game.SPRITESIZEMULT;
        this.maxVerticalAccel = 0.22f * Game.SPRITESIZEMULT;
        this.maxHorizontalAccel = 0.25f * Game.SPRITESIZEMULT;
        this.horizontalAccelMult = 0.35f * Game.SPRITESIZEMULT;
        this.jumpForce = -0.24f * Game.SPRITESIZEMULT;
        this.friction = 0.10f * Game.SPRITESIZEMULT;
        this.setCollisionDistance(50f * Game.SPRITESIZEMULT);
        
    }
    
    public void tick() {
        super.tick();
    }
    
    public void render(Graphics g) {
        
        BufferedImage frame = null;
        Animation currentAnim = null;
        
        if(this.actorState == ActorState.IDLING) {
            frame = this.idleAnim.getFrame(this.currentAnimIndex);
            currentAnim = this.idleAnim;
        } else if(this.actorState == ActorState.DEAD) {
            //frame = 
        }

        // updates animation index
        if(currentAnim != null) this.calculateAnimations(currentAnim);
        
        // fallbacks to default static sprite
        if(frame == null) frame = this.sprite;
        
        // render current sprite
        if(this.facingDirection == Direction.EAST) {
            g.drawImage(frame, this.worldPosition.x, this.worldPosition.y, null);
        } else if(this.facingDirection == Direction.WEST) {
            RenderUtils.renderSpriteFlippedHorizontally(frame, this.worldPosition, g);
        }
    }
}
