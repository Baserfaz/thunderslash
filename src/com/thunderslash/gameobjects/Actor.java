package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.Health;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ActorState;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.RenderUtils;
import com.thunderslash.utilities.Vector2;

public class Actor extends PhysicsObject {
    
    protected String name;
    protected Health HP;
    
    protected ActorState actorState = ActorState.IDLING;
    protected Direction facingDirection = Direction.WEST;
    private Rectangle attackBox;
    
    protected int attackDamage = 1;
    protected int castDamage = 3;
    
    protected boolean isStunned = false;
    private ActorState oldState;
    
    protected double stunTimer = 0.0;
    protected double defaultStunDuration = 500.0;
    
    protected double attackCooldown = 200.0;
    protected double defendCooldown = 200.0;
    protected double useCooldown    = 200.0;
    protected double castCooldown   = 200.0;
    
    protected boolean canAttack = true;
    protected boolean canDefend = true;
    protected boolean canUse    = true;
    protected boolean canCast   = true;
    
    protected boolean allowCleaveAttacks = false;
    
    public Actor(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(worldPos, spriteType);
        
        this.name = name;
        this.HP = new Health(hp, this);
    }
    
    public void tick() {
        
        this.handleStunState();
        this.handleCooldowns();
        this.handleActorStates();
        
        // change facing direction
        if(this.direction.x > 0f) this.facingDirection = Direction.EAST;
        else if(this.direction.x < 0f) this.facingDirection = Direction.WEST;
        
        super.tick();
    }
    
    public void render(Graphics g) {
        if(this.facingDirection == Direction.EAST) {
            g.drawImage(this.sprite, this.worldPosition.x, this.worldPosition.y, null);
        } else if(this.facingDirection == Direction.WEST) {
            RenderUtils.renderSpriteFlippedHorizontally(sprite, this.worldPosition, g);
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
    
    private void handleActorStates() {
        
        // TODO: cant defend, attack, use, cast at same time.
        
        if(this.HP.isDead()) this.actorState = ActorState.DEAD;
        else if(this.canCast == false) this.actorState = ActorState.CASTING;
        else if(this.canAttack == false) this.actorState = ActorState.ATTACKING;
        else if(this.canDefend == false) this.actorState = ActorState.DEFENDING;
        else if(this.canUse == false) this.actorState = ActorState.USING;
        else if(this.velocity.x == 0f && this.velocity.y == 0f ||
                this.acceleration.x == 0f && this.acceleration.y == 0f) this.actorState = ActorState.IDLING;
        else if(this.velocity.y < 0f) this.actorState = ActorState.JUMPING;
        else if(this.velocity.y > 0f) this.actorState = ActorState.FALLING;
        else if(this.direction.x > 0f || this.direction.x < 0f) this.actorState = ActorState.WALKING;
        
        // on state change.
        if(this.actorState != this.oldState) { 
            
            // reset frame index when state changes.
            this.currentAnimIndex = 0;
            
            // change animation speed when attacking.
            if(this.actorState == ActorState.ATTACKING ||
                    this.actorState == ActorState.DEFENDING) this.frameTime = this.attackFrameTime;
            else this.frameTime = this.defaultFrameTime;
        }
        
        // cache last frame's state
        this.oldState = this.actorState;
        
    }
    
    private void handleCooldowns() {

        double dt = Game.instance.getTimeBetweenFrames();
        
        if(this.castTimer < this.castCooldown) {
            this.castTimer += dt;
            this.canCast = false;
        } else {
            this.canCast = true;
        }
        
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
        
        if(this.useTimer < this.useCooldown) {
            this.useTimer += dt;
            this.canUse = false;
        } else {
            this.canUse = true;
        }
        
    }
    
    public void cast() {
        
        // TODO: only support player casts for now
        
        if(this.canCast && this instanceof Player) {
            Player player = (Player) this;
            
            if(player.getPower().getCurrentPower() > 0) {
                
                player.getPower().addCurrentPower(-1);
                
                this.castTimer = 0.0;
                this.actorState = ActorState.CASTING;

                List<GameObject> hits = this.checkHit(5, 75, 25);
                        
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
    
    public void defend() {
        if(this.canDefend) {
            
            this.defendTimer = 0.0;
            this.actorState = ActorState.DEFENDING;
            
            // check if we hit something
            List<GameObject> hits = this.checkHit(3, 10, 25);
            
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
            
            // check if we hit something.
            List<GameObject> hits = this.checkHit(3, 15, 28);
            
            if(hits.isEmpty() == false) {
                for(GameObject hit : hits) {
                    
                    if(hit instanceof Actor) {
                        ((Actor)hit).getHP().takeDamage(this.attackDamage);
                    }
                    
                    this.knockback(hit);
                    
                    if(this.allowCleaveAttacks == false) break;
                }
            }
            
        }
    }
    
    public void action() {
        
        if(this.canUse) {
        
            // should always be player
            Player player = (Player) this;
            
            this.useTimer = 0.0;
            this.actorState = ActorState.USING;
            
            // TODO: use checkHit method instead.
            
            // calculate closest obj
            double smallestDist = Double.POSITIVE_INFINITY;
            GameObject closestObj = null;
            
            for(GameObject go : this.getNearbyGameObjects(this.collisionDistance, false)) {
                double dist = go.hitboxCenter.distance(this.hitboxCenter);
                if(dist < smallestDist) {
                    smallestDist = dist;
                    closestObj = go;
                }
            }
            
            // use the closest obj
            if(closestObj instanceof Chest) {
                
                Chest chest = (Chest) closestObj;
                if(chest.isOpen() == false) chest.open();
                
            } else if(closestObj instanceof Crystal) {
                
                Crystal crystal = (Crystal) closestObj;
                
                if(crystal.isUsed() == false) {
                    crystal.absorb();
                    player.getPower().addCurrentPower(crystal.getPowerValue());
                }
                
            }
        }   
    }
    
    private void knockback(GameObject target) {
        
        if(target instanceof PhysicsObject) {
            PhysicsObject obj = (PhysicsObject) target;
            
            float xforce = 1f * this.getHorizontalDirectionAsInteger();
            float yforce = -0.5f;
            
            obj.isGrounded = false;
            
            obj.velocity.x = xforce;
            obj.acceleration.x = xforce;
        
            obj.velocity.y = yforce;
            obj.acceleration.y = yforce;
            
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
        attackBox = new Rectangle(xpos, this.hitbox.y, attSizex, attSizey);
        
        // check collisions with objs
        for(GameObject go : this.getNearbyGameObjects(this.collisionDistance, false)) {
            if(go.getHitbox().intersects(attackBox)) {
                retObjs.add(go);
            }
        }
        
        return retObjs;
    }
    
    
    public int getHorizontalDirectionAsInteger() {
        return (this.facingDirection == Direction.EAST) ? 1 : -1;
    }
    
    public Health getHP() {
        return this.HP;
    }

    public String getName() {
        return this.name;
    }
    
    public Vector2 getAcceleration() {
        return this.acceleration;
    }
    
    public Vector2 getVelocity() {
        return this.velocity;
    }
    
    public boolean isGrounded() {
        return this.isGrounded;
    }
    
    public void setGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
    }

    public Vector2 getDirection() {
        return this.direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }
    
    public ActorState getActorState() {
        return this.actorState;
    }

    public float getCollisionDistance() {
        return collisionDistance;
    }

    public void setCollisionDistance(float collisionDistance) {
        this.collisionDistance = collisionDistance;
    }

    public Rectangle getAttackBox() {
        return attackBox;
    }

    public void setAttackBox(Rectangle attackBox) {
        this.attackBox = attackBox;
    }
}
