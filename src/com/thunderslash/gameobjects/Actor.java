package com.thunderslash.gameobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.Health;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ActorState;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.ParticleType;
import com.thunderslash.enumerations.SoundEffect;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.particles.Emitter;
import com.thunderslash.utilities.Mathf;
import com.thunderslash.utilities.RenderUtils;
import com.thunderslash.utilities.Vector2;

public abstract class Actor extends PhysicsObject {
    
    protected String name;
    protected Health HP;
    
    protected BufferedImage frame;
    
    protected ActorState actorState = ActorState.IDLING;
    protected Direction facingDirection = Direction.EAST;
    protected Rectangle attackBox;
    
    protected int attackDamage = 1;
    
    protected boolean isStunned = false;
    protected double stunTimer = 0.0;
    protected double defaultStunDuration = 500.0;
    
    protected boolean isInvulnerable = false;
    
    protected double invulnerabilityTimer = 0.0;
    protected double invulnerableTime = 300.0;
    
    public Actor(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(worldPos, spriteType);
        
        this.name = name;
        this.HP = new Health(hp, this);
    }
    
    public void tick() {
        if(this.isEnabled) {
            
            this.handleInvulnerableTimer();
            this.handleStunState();
            
            // change facing direction
            if(this.direction.x > 0f) this.facingDirection = Direction.EAST;
            else if(this.direction.x < 0f) this.facingDirection = Direction.WEST;
            
            super.tick();
        }
    }
    
    public void render(Graphics g) {
        if(this.isVisible) {
            
            BufferedImage img = this.defaultStaticSprite;
            
            if(this.isInvulnerable) {
                img = RenderUtils.tintWithColor(img, Color.red);
            }
            
            if(this.facingDirection == Direction.EAST) {
                g.drawImage(img, this.worldPosition.x, this.worldPosition.y, null);
            } else if(this.facingDirection == Direction.WEST) {
                RenderUtils.renderSpriteFlippedHorizontally(img, this.worldPosition, g);
            } 
        }
    }
    
    private void handleInvulnerableTimer() {
        if(this.isInvulnerable) {
            if(this.invulnerabilityTimer < this.invulnerableTime) {
                this.invulnerabilityTimer += Game.instance.getTimeBetweenFrames();
            } else {
                this.isInvulnerable = false;
                this.invulnerabilityTimer = 0.0;
            }
        }
    }
    
    public void onLanding() {
        
        // on landing create dust particles.
        Point offset = new Point(0, this.hitbox.height / 2);
        this.particleEmitter.emit((int) Mathf.randomRange(2, 5), offset, ParticleType.DUST_ANIM);
        
        // play sound effect
        if(this instanceof Player) {
            Game.instance.getSoundManager().playSound(SoundEffect.LAND);
        } else {
            Game.instance.getSoundManager().playSoundWithPan(SoundEffect.LAND, this);
        }
        
    }
    
    protected void handleStunState() {
        if(this.isStunned) {
            if(this.stunTimer > this.defaultStunDuration) {
                this.isStunned = false;
                this.stunTimer = 0.0;
            } else {
                this.stunTimer += Game.instance.getTimeBetweenFrames();
            }
        }
    }
    
    public List<GameObject> checkHit(int distanceX, int sizex, int sizey) {
        
        List<GameObject> retObjs = new ArrayList<GameObject>();
        
        int xpos = this.hitboxCenter.x;
        int dist = distanceX * Game.SPRITESIZEMULT;
        int attSizex = sizex * Game.SPRITESIZEMULT;
        int attSizey = sizey * Game.SPRITESIZEMULT;        
        
        if(this.facingDirection == Direction.EAST) xpos += dist;
        else if(this.facingDirection == Direction.WEST) xpos -= dist + attSizex;
        
        // set up the rectangle 
        this.attackBox = new Rectangle(xpos, this.hitboxCenter.y - attSizey / 2, attSizex, attSizey);
        
        // check collisions with objs
        for(GameObject go : this.getNearbyGameObjects(this.collisionDistance, false)) {
            if(go.getHitbox().intersects(this.attackBox)) {
                retObjs.add(go);
            }
        }
        
        return retObjs;
    }
    
    // --------- GETTERS & SETTERS --------
    public int getHorizontalDirectionAsInteger() { return (this.facingDirection == Direction.EAST) ? 1 : -1; }
    public Health getHP() { return this.HP; }
    public String getName() { return this.name; }
    public Vector2 getAcceleration() { return this.acceleration; }
    public Vector2 getVelocity() { return this.velocity; }
    public boolean isGrounded() { return this.isGrounded; }
    public void setGrounded(boolean isGrounded) { this.isGrounded = isGrounded; }
    public Vector2 getDirection() { return this.direction; }
    public void setDirection(Vector2 direction) { this.direction = direction; }
    public Direction getFacingDirection() { return facingDirection; }
    public void setFacingDirection(Direction facingDirection) { this.facingDirection = facingDirection; }
    public ActorState getActorState() { return this.actorState; }
    public float getCollisionDistance() { return collisionDistance; }
    public void setCollisionDistance(float collisionDistance) { this.collisionDistance = collisionDistance; }
    public Rectangle getAttackBox() { return attackBox; }
    public void setAttackBox(Rectangle attackBox) { this.attackBox = attackBox; }
    public Emitter getParticleEmitter() { return this.particleEmitter; }
    public void setInvulnerable(boolean b) { this.isInvulnerable = b; }
    public boolean getInvulnerable() { return this.isInvulnerable; }
}
