package com.thunderslash.gameobjects;

import java.awt.Color;
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
import com.thunderslash.enumerations.SoundEffect;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.AnimationCreator;
import com.thunderslash.utilities.RenderUtils;
import com.thunderslash.utilities.Vector2;

public class Player extends Actor {
    
    private Power power;
    private GameObject focusedObject;
    
    private ActorState oldState;
    
    private Animation idleAnim;
    private Animation walkAnim;
    private Animation fallAnim;
    private Animation attackAnim;
    private Animation castAnim;
    
    private double attackTimer = Double.POSITIVE_INFINITY;
    private double castTimer = Double.POSITIVE_INFINITY;

    private double attackFrameTime = 50.0;
    private double castFrameTime = 100.0;
    
    private double attackCooldown = 200.0;
    private double castCooldown = 200.0;
    
    private boolean canAttack = true;
    private boolean canCast = true;
    
    private double collisionPollingCooldown = 50;
    private double currentCollisionPollingTimer = 0.0;
    
    private boolean didAttack = false;
    
    public Player(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
    
        this.power = new Power();
        
        this.invulnerableTime = 500.0;
        
        // set animations
        this.idleAnim   = AnimationCreator.createAnimation(AnimationType.PLAYER_IDLE);
        this.walkAnim   = AnimationCreator.createAnimation(AnimationType.PLAYER_WALK);
        this.fallAnim   = AnimationCreator.createAnimation(AnimationType.PLAYER_FALL);
        this.attackAnim = AnimationCreator.createAnimation(AnimationType.PLAYER_ATTACK);
        this.castAnim   = AnimationCreator.createAnimation(AnimationType.PLAYER_CAST);
        
        // set animation timers / cooldowns
        this.attackCooldown = this.attackFrameTime * this.attackAnim.getAnimationLength();
        this.castCooldown   = this.castFrameTime   * this.castAnim.getAnimationLength();
        
        // set player specific variables
        this.maxVerticalSpeed    = 5.5f   * Game.SPRITESIZEMULT;
        this.maxHorizontalSpeed  = 1f     * Game.SPRITESIZEMULT;
        this.maxVerticalAccel    = 0.22f  * Game.SPRITESIZEMULT;
        this.maxHorizontalAccel  = 0.25f  * Game.SPRITESIZEMULT;
        this.horizontalAccelMult = 0.35f  * Game.SPRITESIZEMULT;
        this.jumpForce           = -0.24f * Game.SPRITESIZEMULT;
        this.friction            = 0.10f  * Game.SPRITESIZEMULT;
        this.collisionDistance   = 50f    * Game.SPRITESIZEMULT;
        
        this.frame = this.defaultStaticSprite;
        
    }
    
    public void tick() {
        if(this.isEnabled) {
            
            this.checkCollisionsWithGameObjects();
            this.updateActorState();
            this.handleCooldowns();
            this.updateAnimationFrame();
            
            if(this.actorState != this.oldState) this.onActorStateChange();
            
            // dont allow movement while attacking/casting
            if(this.actorState == ActorState.ATTACKING ||
                    this.actorState == ActorState.CASTING) {
                this.direction = new Vector2();
            }
            
            super.tick();
            
            this.oldState = this.actorState;
        }
    }
    
    public void render(Graphics g) {
        if(this.isVisible) {
            
            if(this.isInvulnerable) {
                this.frame = RenderUtils.tintWithColor(this.frame, Color.white);
            }
            
            if(this.facingDirection == Direction.EAST) {
                g.drawImage(this.frame, this.worldPosition.x, this.worldPosition.y, null);
            } else if(this.facingDirection == Direction.WEST) {
                RenderUtils.renderSpriteFlippedHorizontally(this.frame, this.worldPosition, g);
            }
        }
    }

    public void onDeath() {
        BufferedImage img = Game.instance.getSpriteCreator().CreateSprite(SpriteType.PLAYER_DEAD);
        img = RenderUtils.tint(img, true, 2);
        this.setSprite(img);
        this.direction = new Vector2();
        // Game.instance.setGamestate(GameState.GAME_OVER);
    }
    
    private void updateAnimationFrame() {
        
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
            
            if(this.currentAnimIndex == 4) this.doAttackAction();
            
        } else if(this.actorState == ActorState.CASTING) {
            frame = this.castAnim.getFrame(this.currentAnimIndex);
            currentAnim = this.castAnim;
        } else if(this.actorState == ActorState.JUMPING) {
            frame = this.fallAnim.getFrame(this.currentAnimIndex);
            currentAnim = this.fallAnim;
        }

        // updates animation index & fallback to static sprite
        if(currentAnim != null) this.calculateAnimations(currentAnim);
        if(frame == null) frame = this.defaultStaticSprite;
        
        this.frame = frame;
    }
    
    private void onActorStateChange() {
        
        // reset frame index when state changes.
        this.currentAnimIndex = 0;
        
        // reset attack
        this.didAttack = false;
        
        // change animation speed according to the state
        if(this.actorState == ActorState.ATTACKING) this.frameTime = this.attackFrameTime;
        else if(this.actorState == ActorState.CASTING) this.frameTime = this.castFrameTime;
        else this.frameTime = this.defaultFrameTime;
        
    }
    
    private void handleCooldowns() {
        double dt = Game.instance.getTimeBetweenFrames();
        if(this.attackTimer < this.attackCooldown) {
            this.attackTimer += dt;
            this.canAttack = false;
        } else {
            this.canAttack = true;
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
        else if(this.velocity.y < 0f) this.actorState = ActorState.JUMPING;
        else if(this.velocity.y > 0f) this.actorState = ActorState.FALLING;
        else if(this.direction.x > 0f || this.direction.x < 0f) this.actorState = ActorState.WALKING;
        else if(this.velocity.x == 0f && this.velocity.y == 0f ||
                this.acceleration.x == 0f && this.acceleration.y == 0f) this.actorState = ActorState.IDLING;
    }
    
    private void checkCollisionsWithGameObjects() {
        if(this.currentCollisionPollingTimer > this.collisionPollingCooldown) {
            this.focusedObject = null;
            this.currentCollisionPollingTimer = 0.0;
            for(GameObject go : this.getNearbyGameObjects(this.collisionDistance, false)) {
                
                if(go instanceof Chest && ((Chest)go).isOpen() == false) {
                    go.hasFocus = this.hitbox.intersects(go.getHitbox());
                    if(go.hasFocus) this.focusedObject = go;
                } else if(go instanceof Crystal && ((Crystal)go).isUsed() == false) {
                    go.hasFocus = this.hitbox.intersects(go.getHitbox());
                    if(go.hasFocus) this.focusedObject = go;
                } else if(go instanceof Enemy) {
                    
                    if(this.isInvulnerable == false) {
                        
                        if(this.HP.isDead()) continue;
                        
                        Enemy enemy = (Enemy) go;    
                        if(enemy.getHP().isDead() == false && this.hitbox.intersects(go.getHitbox())) {
                            this.isInvulnerable = true;
                            this.HP.takeDamage(1);
                            
                            Game.instance.getSoundManager().playSound(SoundEffect.PLAYER_HURT);
                        }
                    }
                }
            }
        } else {
            this.currentCollisionPollingTimer += Game.instance.getTimeBetweenFrames();
        }
    }
 
    protected void onCollision(Direction dir) {}
    
    // ---- PLAYER ACTIONS ----
    public void jump() { this.setDirection(new Vector2(this.direction.x, 1f)); }
    public void drop() { this.setDirection(new Vector2(this.direction.x, -1f)); }
    public void left() { this.setDirection(new Vector2(-1f, this.direction.y)); }
    public void right() { this.setDirection(new Vector2(1f, this.direction.y)); }
    
    public void cast() {
        if(this.canCast) {
            if(this.getPower().getCurrentPower() > 0) {
                this.getPower().addCurrentPower(-1);
                this.castTimer = 0.0;
                this.actorState = ActorState.CASTING;
                this.doCastAction();
            }
        }
    }
    
    private void doCastAction() {
        // Game.instance.getHandler().AddObject(new Projectile(this.worldPosition, SpriteType.LIGHTNING_BOLT, this.facingDirection));
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
    
    public void attack() {
        if(this.canAttack) {
            this.attackTimer = 0.0;
            this.actorState = ActorState.ATTACKING;
            
            Game.instance.getSoundManager().playSound(SoundEffect.PLAYER_ATTACK);
            
        }
    }
    
    private void doAttackAction() {
        
        // attack only once per swing
        if(this.didAttack) return;
        this.didAttack = true;
        
        int attWidth = 25;
        int attHeight = 30;
        int attackDist = attWidth / 2;
        
        // check if we hit something.
        List<GameObject> hits = this.checkHit(-attackDist, attWidth + 10, attHeight);
        
        if(hits.isEmpty() == false) {
            for(GameObject hit : hits) {
                if(hit instanceof Enemy && ((Enemy) hit).getHP().isDead() == false) {
                    Enemy enemy = (Enemy) hit;
                    
                    // take damage
                    enemy.getHP().takeDamage(this.attackDamage);
                    
                    // add knockback
                    this.knockback(enemy, this.facingDirection);
                    
                    // add score
                    if(enemy.getHP().isDead()) Game.instance.getSession().addScore(enemy.getKillScore());
                    
                    Game.instance.getSoundManager().playSound(SoundEffect.ATTACK_HIT);
                    
                    break;
                }
            }
        }
    }
    
    // ---- GETTERS & SETTERS -----
    public Power getPower() { return power; }
    public void setPower(Power power) { this.power = power; }
}
