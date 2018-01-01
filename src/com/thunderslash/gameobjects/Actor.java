package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.thunderslash.data.Animation;
import com.thunderslash.data.Health;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ActorState;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Mathf;
import com.thunderslash.utilities.RenderUtils;
import com.thunderslash.utilities.Vector2;

public class Actor extends PhysicsObject {
    
    protected String name;
    protected Health HP;
    
    protected int damage = 1;
    
    protected ActorState actorState = ActorState.IDLING;
    protected Direction facingDirection = Direction.WEST;
    private Rectangle attackBox;
    
    // animation
    protected double maxAnimationTime = 50.0;
    protected double currentAnimTime = 0.0;
    protected int currentAnimIndex = 0;
    
    public Actor(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(worldPos, spriteType);
        
        this.name = name;
        this.HP = new Health(hp);
    }
    
    public void tick() {
        
        // handle actor states
        if(this.HP.isDead()) this.actorState = ActorState.DEAD;
        else if(this.velocity.x == 0f && this.velocity.y == 0f ||
                this.acceleration.x == 0f && this.acceleration.y == 0f) this.actorState = ActorState.IDLING;
        else if(this.velocity.y < 0f) this.actorState = ActorState.JUMPING;
        else if(this.velocity.y > 0f) this.actorState = ActorState.FALLING;
        else if(this.direction.x > 0f || this.direction.x < 0f) this.actorState = ActorState.WALKING;
        
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
    
    protected void calculateAnimations(Animation anim) {
        double dt = Game.instance.getTimeBetweenFrames();
        if(this.currentAnimTime > this.maxAnimationTime) {
            this.currentAnimTime = 0.0;
            this.currentAnimIndex += 1;
            if(this.currentAnimIndex >= anim.getAnimationLength()) {
                this.currentAnimIndex = 0;
            }
        }
        this.currentAnimTime += dt;
    }
    
    public void attack() {
        
        this.actorState = ActorState.ATTACKING;
        
        int xpos = this.hitboxCenter.x;
        int dist = 6 * Game.SPRITESIZEMULT;
        int attSizex = 20 * Game.SPRITESIZEMULT;
        int attSizey = 25 * Game.SPRITESIZEMULT;        
        
        int dir = 0;
        
        if(this.facingDirection == Direction.EAST) {
            xpos += dist;
            dir = 1;
        }
        else if(this.facingDirection == Direction.WEST) {
            xpos -= dist + attSizex;
            dir = -1;
        }
        
        // set up the rectangle 
        attackBox = new Rectangle(xpos, this.hitbox.y, attSizex, attSizey);
        
        // check collisions with objs
        for(GameObject go : this.getNearbyGameObjects(this.collisionDistance)) {
            if(go.getHitbox().intersects(attackBox)) {
                
                if(go instanceof PhysicsObject) {
                    PhysicsObject obj = (PhysicsObject) go;
                    
                    double force = Mathf.randomRange(0.5, 1.0);
                    
                    obj.acceleration.x = (float) (force * dir);
                    System.out.println(obj.getInfo());
                }
                
                if(go instanceof Actor) {
                    Actor actor = (Actor) go;
                    actor.getHP().takeDamage(this.damage);
                }
            }
        }
    }
    
    // on action key press
    public void action() {
        
        this.actorState = ActorState.ACTION;
        
        // calculate closest obj
        double smallestDist = Double.POSITIVE_INFINITY;
        GameObject closestObj = null;
        
        for(GameObject go : this.getNearbyGameObjects(this.collisionDistance)) {
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
            ((Crystal) closestObj).absorb();
        }
        
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
