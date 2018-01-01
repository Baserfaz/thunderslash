package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Mathf;
import com.thunderslash.utilities.Vector2;

public class PhysicsObject extends GameObject {

    // default values
    protected float maxVerticalSpeed = 5.5f * Game.SPRITESIZEMULT;
    protected float maxHorizontalSpeed = 1f * Game.SPRITESIZEMULT;
    protected float maxVerticalAccel = 0.22f * Game.SPRITESIZEMULT;
    protected float maxHorizontalAccel = 0.25f * Game.SPRITESIZEMULT;
    protected float horizontalAccelMult = 0.35f * Game.SPRITESIZEMULT;
    protected float jumpForce = -0.24f * Game.SPRITESIZEMULT;
    protected float friction = 0.10f * Game.SPRITESIZEMULT;
    protected float collisionDistance = 50f * Game.SPRITESIZEMULT;
    
    protected Vector2 velocity = new Vector2();
    protected Vector2 acceleration = new Vector2();
    protected Vector2 direction = new Vector2();
    
    private Block lastBlock = null;
    
    // collisions
    protected boolean isGrounded = false;
    protected boolean collisionLeft = false;
    protected boolean collisionRight = false;
    protected boolean collisionTop = false;
    protected boolean collidedWithTrap = false;
    
    // collections
    private List<Point> collisionPoints = new ArrayList<Point>();
    
    public PhysicsObject(Point worldPos, SpriteType type) {
        super(worldPos, type);
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
    
    public void render(Graphics g) {}
    
    private void move() {
        
        this.handleCollisions();
        
        // horizontal movement
        if(this instanceof Actor) {
            this.acceleration.x = this.direction.x * this.horizontalAccelMult;
        }
        
        if(this.isGrounded) {
            
            if(this instanceof Actor) {
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
                                
                                if(this instanceof Actor) {
                                    ((Actor)this).getHP().takeDamage(trap.getDamage());
                                    this.velocity.y = -2f;
                                    this.lastBlock = block;
                                }
                                
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
    
    protected List<GameObject> getNearbyGameObjects(float distance) {
        List<GameObject> objs = new ArrayList<GameObject>();
        for(GameObject go : Game.instance.getHandler().getObjects()) {
            if(go instanceof Player || go instanceof Block) continue;
            if(go.hitboxCenter.distance(this.hitboxCenter) < distance) objs.add(go); 
        }
        return objs;
    }
    
    protected List<Block> getNearbyBlocks(float colDist) {
        List<Block> allBlocks = new ArrayList<Block>();
        for(Block block : Game.instance.getWorld().getCurrentRoomBlocks()) {
            if(block.hitboxCenter.distance(this.hitboxCenter) < colDist) {
                allBlocks.add(block);
            }
        }
        return allBlocks;
    }
    
    public List<Point> getCollisionPoints() {
        return collisionPoints;
    }

    public void setCollisionPoints(List<Point> collisionPoints) {
        this.collisionPoints = collisionPoints;
    }
    
    public Block getLastBlock() {
        return lastBlock;
    }

    public void setLastBlock(Block lastBlock) {
        this.lastBlock = lastBlock;
    }
    
}
