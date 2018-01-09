package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import com.thunderslash.data.Animation;
import com.thunderslash.data.Power;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ActorState;
import com.thunderslash.enumerations.AnimationType;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.particles.Emitter;
import com.thunderslash.utilities.AnimationCreator;
import com.thunderslash.utilities.RenderUtils;

public class Player extends Actor {
    
    private Power power;
    private Emitter playerEmitter;
    private GameObject focusedObject;
    
    private ActorState oldState;
    
    private Animation idleAnim;
    private Animation walkAnim;
    private Animation fallAnim;
    private Animation attackAnim;
    private Animation defendAnim;
    private Animation castAnim;
    
    private double attackTimer = Double.POSITIVE_INFINITY;
    private double defendTimer = Double.POSITIVE_INFINITY;
    private double castTimer = Double.POSITIVE_INFINITY;

    private double attackFrameTime = 25.0;
    private double castFrameTime = 40.0;
    
    private double attackCooldown = 200.0;
    private double defendCooldown = 200.0;
    private double castCooldown = 200.0;
    
    private boolean canAttack = true;
    private boolean canDefend = true;
    private boolean canCast = true;
    private boolean allowCleaveAttacks = false;
    
    private boolean isInvulnerable = false;
    
    private double invulnerabilityTimer = 0.0;
    private double invulnerableTime = 100.0;
    
    private double actionPollingCooldown = 50;
    private double currentPollingTimer = 0.0;
    
    private int castDamage = 3;
    
    public Player(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
    
        this.power = new Power();
        this.playerEmitter = Game.instance.getEmitterManager().createEmitter();
        
        // set animations
        this.idleAnim   = AnimationCreator.createAnimation(AnimationType.PLAYER_IDLE);
        this.walkAnim   = AnimationCreator.createAnimation(AnimationType.PLAYER_WALK);
        this.fallAnim   = AnimationCreator.createAnimation(AnimationType.PLAYER_FALL);
        this.attackAnim = AnimationCreator.createAnimation(AnimationType.PLAYER_ATTACK);
        this.defendAnim = AnimationCreator.createAnimation(AnimationType.PLAYER_DEFEND);
        this.castAnim   = AnimationCreator.createAnimation(AnimationType.PLAYER_CAST);
        
        // set animation timers / cooldowns
        this.attackCooldown = this.attackFrameTime * this.attackAnim.getAnimationLength();
        this.defendCooldown = this.attackFrameTime * this.defendAnim.getAnimationLength();
        this.castCooldown   = this.castFrameTime   * this.castAnim.getAnimationLength();
        
        // set stuff
        this.maxVerticalSpeed    = 5.5f   * Game.SPRITESIZEMULT;
        this.maxHorizontalSpeed  = 1f     * Game.SPRITESIZEMULT;
        this.maxVerticalAccel    = 0.22f  * Game.SPRITESIZEMULT;
        this.maxHorizontalAccel  = 0.25f  * Game.SPRITESIZEMULT;
        this.horizontalAccelMult = 0.35f  * Game.SPRITESIZEMULT;
        this.jumpForce           = -0.24f * Game.SPRITESIZEMULT;
        this.friction            = 0.10f  * Game.SPRITESIZEMULT;
        this.collisionDistance   = 50f    * Game.SPRITESIZEMULT;
        
    }
    
    public void tick() {
        this.checkGameObjectCollisions();
        this.updateActorState();
        this.handleCooldowns();
        this.handleActorStates();
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
        } else if(this.actorState == ActorState.CASTING) {
            frame = this.castAnim.getFrame(this.currentAnimIndex);
            currentAnim = this.castAnim;
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

    private void handleActorStates() {
        
        // on state change.
        if(this.actorState != this.oldState) { 
            
            // reset frame index when state changes.
            this.currentAnimIndex = 0;
            
            // change animation speed when attacking.
            if(this.actorState == ActorState.ATTACKING ||
                    this.actorState == ActorState.DEFENDING) this.frameTime = this.attackFrameTime;
            else if(this.actorState == ActorState.CASTING) this.frameTime = this.castFrameTime;
            else this.frameTime = this.defaultFrameTime;
        }
        
        // cache last frame's state
        this.oldState = this.actorState;
        
    }
    
    private void handleCooldowns() {

        double dt = Game.instance.getTimeBetweenFrames();
        
        if(this.attackTimer < this.attackCooldown) {
            this.attackTimer += dt;
            this.canAttack = false;
        } else {
            this.canAttack = true;
        }
        
        if(this.defendTimer < this.defendCooldown) {
            this.defendTimer += dt;
            this.canDefend = false;
        } else {
            this.canDefend = true;
        }
        
        if(this.castTimer < this.castCooldown) {
            this.castTimer += dt;
            this.canCast = false;
        } else {
            this.canCast = true;
        }
        
    }
    
    private void updateActorState() {
        
        if(this.HP.isDead()) this.actorState = ActorState.DEAD;
        else if(this.canCast == false) this.actorState = ActorState.CASTING;
        else if(this.canAttack == false) this.actorState = ActorState.ATTACKING;
        else if(this.canDefend == false) this.actorState = ActorState.DEFENDING;
        else if(this.velocity.y < 0f) this.actorState = ActorState.JUMPING;
        else if(this.velocity.y > 0f) this.actorState = ActorState.FALLING;
        else if(this.direction.x > 0f || this.direction.x < 0f) this.actorState = ActorState.WALKING;
        else if(this.velocity.x == 0f && this.velocity.y == 0f ||
                this.acceleration.x == 0f && this.acceleration.y == 0f) this.actorState = ActorState.IDLING;
        
    }
    
    public void cast() {
        
        if(this.canCast && this instanceof Player) {
            Player player = (Player) this;
            
            if(player.getPower().getCurrentPower() > 0) {
                
                player.getPower().addCurrentPower(-1);
                
                this.castTimer = 0.0;
                this.actorState = ActorState.CASTING;
                
                List<GameObject> hits = this.checkHit(25, 25, 45);
                        
                int spriteSize = (Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT) / 2;
                
                // create animation
                Game.instance.getAnimator().play(AnimationType.LIGHTNING_STRIKE,
                        (this.attackBox.x + this.attackBox.width / 2) - spriteSize,
                        this.attackBox.y - spriteSize);
                
                if(hits.isEmpty() == false) {    
                    for(GameObject hit : hits) {
                        if(hit instanceof Enemy) {
                           ((Actor) hit).getHP().takeDamage(this.castDamage);
                        }
                    }
                }
            }
        }
    }
    
    public void action() {
        if(this.focusedObject != null) {
            if(this.focusedObject instanceof Chest) {
                ((Chest)this.focusedObject).open();
            } else if(this.focusedObject instanceof Crystal) {
                ((Crystal)this.focusedObject).use();
                this.power.setCurrentPower(this.power.getMaxPower());
            }
        }
    }
    
    public void defend() {
        if(this.canDefend) {
            
            this.defendTimer = 0.0;
            this.actorState = ActorState.DEFENDING;
            
            // check if we hit something
            List<GameObject> hits = this.checkHit(3, 10, 30);
            
            if(hits.isEmpty() == false) {
                for(GameObject hit : hits) {
                    if(hit instanceof Enemy) {
                        Enemy enemy = (Enemy) hit;
                        enemy.isStunned = true;
                    }
                    
                    if(this.allowCleaveAttacks == false) break;
                }
            }
        }
    }
    
    public void attack() {
        if(this.canAttack) {
            
            this.attackTimer = 0.0;
            this.actorState = ActorState.ATTACKING;
            
            int attWidth = 25;
            int attHeight = 30;
            int attackDist = attWidth / 2;
            
            // check if we hit something.
            List<GameObject> hits = this.checkHit(-attackDist, attWidth + 10, attHeight);
            
            if(hits.isEmpty() == false) {
                for(GameObject hit : hits) {
                    if(hit instanceof Enemy) {
                        ((Enemy)hit).getHP().takeDamage(this.attackDamage);
                        if(this.allowCleaveAttacks == false) break;
                    }
                }
            }
        }
    }
    
    private void checkGameObjectCollisions() {
        
        // every n millisecond check collisions with GOs.
        
        if(this.currentPollingTimer > this.actionPollingCooldown) {
            
            this.focusedObject = null;
            this.currentPollingTimer = 0.0;
            
            for(GameObject go : this.getNearbyGameObjects(this.collisionDistance, false)) {
                if(go instanceof Chest && ((Chest)go).isOpen() == false) {
                    go.hasFocus = this.hitbox.intersects(go.getHitbox());
                    if(go.hasFocus) this.focusedObject = go;
                } else if(go instanceof Crystal && ((Crystal)go).isUsed() == false) {
                    go.hasFocus = this.hitbox.intersects(go.getHitbox());
                    if(go.hasFocus) this.focusedObject = go;
                } else if(go instanceof Enemy) {
                    
                    // when the player takes damage,
                    // set the player to invulnerability state,
                    // where the player doesn't take recurring damage.
                    
                    if(this.isInvulnerable) {
                        
                        if(this.invulnerabilityTimer < this.invulnerableTime) {
                            this.invulnerabilityTimer += Game.instance.getTimeBetweenFrames();
                        } else {
                            this.isInvulnerable = false;
                            this.invulnerabilityTimer = 0.0;
                        }
                        
                    } else {
                        
                        Enemy enemy = (Enemy) go;    
                        if(enemy.getHP().isDead() == false && this.hitbox.intersects(go.getHitbox())) {
                            this.isInvulnerable = true;
                            this.HP.takeDamage(1);
                        }
                        
                    }
                }
            }
            
        } else {
            this.currentPollingTimer += Game.instance.getTimeBetweenFrames();
        }
    }
    
    public Power getPower() { return power; }
    public void setPower(Power power) { this.power = power; }

    public Emitter getPlayerEmitter() {
        return playerEmitter;
    }

    public void setPlayerEmitter(Emitter playerEmitter) {
        this.playerEmitter = playerEmitter;
    }
}
