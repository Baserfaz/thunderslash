package com.thunderslash.gameobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.Animation;
import com.thunderslash.data.Health;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ActorState;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Vector2;
import com.thunderslash.utilities.Mathf;
import com.thunderslash.utilities.RenderUtils;

public class Actor extends GameObject {
    
    // references to other files
    protected String name;
    protected Health HP;
    
    protected ActorState actorState = ActorState.IDLING;
    
    // actor settings
    // default values
    protected float maxVerticalSpeed = 5.5f * Game.SPRITESIZEMULT;
    protected float maxHorizontalSpeed = 1f * Game.SPRITESIZEMULT;
    protected float maxVerticalAccel = 0.22f * Game.SPRITESIZEMULT;
    protected float maxHorizontalAccel = 0.25f * Game.SPRITESIZEMULT;
    protected float horizontalAccelMult = 0.35f * Game.SPRITESIZEMULT;
    protected float jumpForce = -0.24f * Game.SPRITESIZEMULT;
    protected float friction = 0.10f * Game.SPRITESIZEMULT;
    private float collisionDistance = 50f * Game.SPRITESIZEMULT;
    
    // inputs
    protected Vector2 direction = new Vector2();
    
    // physics 
    protected Vector2 velocity = new Vector2();
    protected Vector2 acceleration = new Vector2();
    
    // collisions
    protected boolean isGrounded = false;
    protected boolean collisionLeft = false;
    protected boolean collisionRight = false;
    protected boolean collisionTop = false;
    protected boolean collidedWithTrap = false;
    
    // other refs
    private Block lastBlock = null;
    protected Direction facingDirection = Direction.WEST;
    private Rectangle attackBox;
    
    // animation
    protected double maxAnimationTime = 50.0;
    protected double currentAnimTime = 0.0;
    protected int currentAnimIndex = 0;
    
    // collections
    private List<Point> collisionPoints = new ArrayList<Point>();
    
    public Actor(String name, Point worldPos, SpriteType spriteType, int hp) {
        super(worldPos, spriteType);
        
        this.name = name;
        this.HP = new Health(hp);
        
    }
    
    public void tick() {
        if(Game.drawActorCollisionPoints) this.collisionPoints.clear();
        
        this.updateCollisions();
        this.move();
        
        // update hitbox position
        this.hitbox.x = this.worldPosition.x + this.hitboxSizes.x;
        this.hitbox.y = this.worldPosition.y + this.hitboxSizes.y;
        
        this.hitboxCenter = new Point(this.hitbox.x + this.hitbox.width / 2, 
                this.hitbox.y + this.hitbox.height / 2);
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
        
        System.out.println("ATTACK");
        this.actorState = ActorState.ATTACKING;
        
        int xpos = this.hitboxCenter.x;
        int dist = 6 * Game.SPRITESIZEMULT;
        int attSizex = 20 * Game.SPRITESIZEMULT;
        int attSizey = 25 * Game.SPRITESIZEMULT;        
        
        if(this.facingDirection == Direction.EAST) xpos += dist;
        else if(this.facingDirection == Direction.WEST) xpos -= dist + attSizex;
        
        // set up the rectangle 
        attackBox = new Rectangle(xpos, this.hitbox.y, attSizex, attSizey);
        
        // check collisions with objs
        for(GameObject go : this.getNearbyGameObjects(this.collisionDistance)) {
            if(go.getHitbox().intersects(attackBox)) {
                if(go instanceof Actor) {
                    Actor actor = (Actor) go;
                    actor.getHP().takeDamage(1);
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
    
    private void move() {
        
        this.handleCollisions();
        
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
        
        // horizontal movement
        this.acceleration.x = this.direction.x * this.horizontalAccelMult;

        if(this.isGrounded) {
            
            // jumping
            if(this.direction.y > 0f) {
                this.acceleration.y = this.jumpForce;
                this.isGrounded = false;
                this.direction.y = 0f;
            }
            
            // drop down
            if(this.direction.y < 0f) {
                if(this.lastBlock.getBlocktype() == BlockType.PLATFORM) {
                    this.isGrounded = false;
                    this.direction.y = 0f;
                }
            }
            
        } else if(this.isGrounded == false) {
            this.acceleration.y += Game.GRAVITY;
        }
        
        // clamp acceleration
        this.acceleration.y = Mathf.clamp(-this.maxVerticalAccel,
                this.maxVerticalAccel, this.acceleration.y);
        
        this.acceleration.x = Mathf.clamp(-this.maxHorizontalAccel,
                this.maxHorizontalAccel, this.acceleration.x);
        
        // set velocity vector
        velocity.x += this.acceleration.x;
        velocity.y += this.acceleration.y;
        
        // add friction
        if(this.velocity.x > 0f) this.velocity.x -= this.friction;
        else if(this.velocity.x < 0f) this.velocity.x += this.friction;
        
        // clamp values to min/max range
        velocity.x = Mathf.clamp(-this.maxHorizontalSpeed, this.maxHorizontalSpeed, velocity.x);
        velocity.y = Mathf.clamp(-this.maxVerticalSpeed, this.maxVerticalSpeed, velocity.y);
        
        // move the character
        this.addWorldPosition(velocity.x, velocity.y);
        
    }
    
    private void handleCollisions() {
        
        if(collisionLeft) {
            this.velocity.x = 1f;
            this.collisionLeft = false;
        }
        
        if(collisionRight) {
            this.velocity.x = -1f;
            this.collisionRight = false;
        }
        
        if(collisionTop) {
            this.velocity.y = 2f;
            this.acceleration.y = 0f;
            this.collisionTop = false;
        }
        
        if(isGrounded) {
            this.velocity.y = 0f;
            this.acceleration.y = 0f;
        
            // our position needs to be set so
            // that we are just a tiny bit inside
            // of the "lastBlock" because otherwise
            // the actor would not be on ground anymore.
            if(this.lastBlock != null) {
                this.setWorldPosition(
                    this.worldPosition.x,
                    this.lastBlock.getHitbox().y - (Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT) + 1);
            }
        }
    }
    
    private void updateCollisions() {
        
        int margin = 3 * Game.SPRITESIZEMULT;
        
        // y-axis
        int top    = this.hitbox.y;
        int center = this.hitboxCenter.y;
        int bottom = this.hitbox.y + this.hitboxSizes.height;
        
        // x-axis
        int left   = this.hitbox.x;
        int middle = this.hitboxCenter.x;
        int right  = this.hitbox.x + this.hitboxSizes.width;
        
        // TODO: refactor and check collisions with rectangles.
        
        // left collisions
        Point lc = new Point(left, center);
        Point lt = new Point(left, top + margin);
        Point lb = new Point(left, bottom - margin);
        
        // right collisions
        Point rc = new Point(right, center);
        Point rt = new Point(right, top + margin);
        Point rb = new Point(right, bottom - margin);
        
        // top collisions
        Point tc = new Point(middle, top);
        Point tl = new Point(left + margin, top);
        Point tr = new Point(right - margin, top);
        
        // bottom collisions
        Point bl = new Point(left + margin, bottom);
        Point bc = new Point(middle, bottom);
        Point br = new Point(right - margin, bottom);
        
        // center
        Point c = new Point(middle, center);
        
        for(Block block : this.getNearbyBlocks(this.collisionDistance)) {

            // if the block is disabled -> no collisions
            if(block.isEnabled == false) continue;
            
            // only check collisions against these
            if(block.getBlocktype() == BlockType.SOLID || 
                    block.getBlocktype() == BlockType.PLATFORM ||
                    block.getBlocktype() == BlockType.HURT) {
                
                Rectangle hitbox = block.getHitbox();
                
                // when the actor is falling
                if(this.velocity.y > 0f) {
                    if(this.lastBlock != block) {
                        if(hitbox.contains(bl) || hitbox.contains(bc) || hitbox.contains(br)) {
                            if(block instanceof Trap) {
                                
                                Trap trap = (Trap) block;
                                this.getHP().takeDamage(trap.getDamage());

                                this.velocity.y = -2f;
                                this.lastBlock = block;
                                
                            } else {
                                this.isGrounded = true;
                                this.lastBlock = block;
                            }
                        }
                    }
                } else {
                    if(block == this.lastBlock) {
                        if(hitbox.contains(bl) == false && 
                                hitbox.contains(bc) == false &&
                                hitbox.contains(br) == false) {
                            this.isGrounded = false;
                            this.lastBlock = null;
                        }
                    }
                }
                
                if(block.getBlocktype() != BlockType.PLATFORM) {
                    if(hitbox.contains(lc) || hitbox.contains(lt) || hitbox.contains(lb)) {
                        this.collisionLeft = true;
                    }
                    
                    if(hitbox.contains(rc) || hitbox.contains(rt) || hitbox.contains(rb)) {
                        this.collisionRight = true;
                    }
                    
                    if(hitbox.contains(tc) || hitbox.contains(tl) || hitbox.contains(tr)) {
                        this.collisionTop = true;
                    }
                }
            }
        }
        
        if(Game.drawActorCollisionPoints) {
            collisionPoints.add(lc);
            collisionPoints.add(lt);
            collisionPoints.add(lb);            
            
            collisionPoints.add(rc);
            collisionPoints.add(rt);   
            collisionPoints.add(rb);   
            
            collisionPoints.add(tc);
            collisionPoints.add(tl);
            collisionPoints.add(tr);
            
            collisionPoints.add(bl);
            collisionPoints.add(bc);
            collisionPoints.add(br);

            collisionPoints.add(c);
        }
    }

    private List<GameObject> getNearbyGameObjects(float distance) {
        List<GameObject> objs = new ArrayList<GameObject>();
        for(GameObject go : Game.instance.getHandler().getObjects()) {
            if(go instanceof Player || go instanceof Block) continue;
            if(go.hitboxCenter.distance(this.hitboxCenter) < distance) objs.add(go); 
        }
        return objs;
    }
    
    private List<Block> getNearbyBlocks(float colDist) {
        List<Block> allBlocks = new ArrayList<Block>();
        for(Block block : Game.instance.getWorld().getCurrentRoomBlocks()) {
            if(block.hitboxCenter.distance(this.hitboxCenter) < colDist) {
                allBlocks.add(block);
            }
        }
        return allBlocks;
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

    public List<Point> getCollisionPoints() {
        return collisionPoints;
    }

    public void setCollisionPoints(List<Point> collisionPoints) {
        this.collisionPoints = collisionPoints;
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

    public Block getLastBlock() {
        return lastBlock;
    }

    public void setLastBlock(Block lastBlock) {
        this.lastBlock = lastBlock;
    }

    public Rectangle getAttackBox() {
        return attackBox;
    }

    public void setAttackBox(Rectangle attackBox) {
        this.attackBox = attackBox;
    }
}
