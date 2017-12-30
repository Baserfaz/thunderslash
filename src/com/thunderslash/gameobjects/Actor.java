package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.Health;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Coordinate;
import com.thunderslash.utilities.Vector2;
import com.thunderslash.utilities.Mathf;
import com.thunderslash.utilities.RenderUtils;

public class Actor extends GameObject {
    
    // references to other files
    protected String name;
    protected Health HP;
    
    // actor settings
    protected float maxVerticalSpeed = 5.5f * Game.SPRITESIZEMULT;
    protected float maxHorizontalSpeed = 1f * Game.SPRITESIZEMULT;
    protected float maxVerticalAccel = 0.24f * Game.SPRITESIZEMULT;
    protected float maxHorizontalAccel = 0.25f * Game.SPRITESIZEMULT;
    protected float horizontalAccelMult = 0.35f * Game.SPRITESIZEMULT;
    protected float jumpForce = -0.24f * Game.SPRITESIZEMULT;
    protected float friction = 0.10f * Game.SPRITESIZEMULT;
    protected float collisionDistance = 50f * Game.SPRITESIZEMULT;
    
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
    
    // other refs
    protected Block lastBlock = null;
    protected Direction facingDirection = Direction.WEST;
    
    // collections
    private List<Point> collisionPoints = new ArrayList<Point>();
    
    public Actor(String name, Coordinate worldPos, SpriteType spriteType, int hp) {
        super(worldPos, spriteType);
        
        this.name = name;
        this.HP = new Health(hp);
        
    }
    
    public void tick() {
        
        if(Game.drawActorCollisionPoints) {
            this.collisionPoints.clear();
        }
        
        this.updateCollisions();
        this.move();
        
        // update hitbox position
        this.hitbox.x = this.worldPosition.x + this.hitboxSizes.x;
        this.hitbox.y = this.worldPosition.y + this.hitboxSizes.y;
        
        this.hitboxCenter = new Coordinate(this.hitbox.x + this.hitbox.width / 2, 
                this.hitbox.y + this.hitbox.height / 2);
    }
    
    public void render(Graphics g) {
        if(this.facingDirection == Direction.EAST) {
            g.drawImage(this.sprite, this.worldPosition.x, this.worldPosition.y, null);
        } else if(this.facingDirection == Direction.WEST) {
            RenderUtils.renderSpriteFlippedHorizontally(sprite, this.worldPosition, g);
        }
    }
    
    // on action key press
    public void action() {
        
        // get all items near the actor
        List<GameObject> gos = new ArrayList<GameObject>();
        for(Block block : this.getNearbyBlocks(this.collisionDistance)) {
            if(block.getItem() != null) {
                gos.add(block.getItem());
            }
        }
        
        // calculate closest obj
        float smallestDist = 99999f;
        GameObject closestObj = null;
        
        // the center of the hitbox
        // works as the anchor point
        // for calculating distances.
        Vector2 me = new Vector2(this.hitbox.x + this.hitboxSizes.width / 2,
                this.hitbox.y + this.hitboxSizes.height / 2);
        
        for(GameObject go : gos) {
            Vector2 v2 = new Vector2(go.hitbox.x, go.hitbox.y);
            float dist = me.distance(v2);
            if(dist < smallestDist) {
                smallestDist = dist;
                closestObj = go;
            }
        }
        
        // use the closest obj
        if(closestObj instanceof Chest) {
            Chest chest = (Chest) closestObj;
            if(chest.isOpen() == false) {
                chest.open();
            }
        }
    }
    
    private void move() {
        
        this.handleCollisions();
        
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
            this.velocity.y = 1f;
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
                    this.lastBlock.getBounds().y - (Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT) + 1);
            }
        }
    }
    
    private void updateCollisions() {
        
        int margin = 3 * Game.SPRITESIZEMULT;
        
        // y-axis
        int top    = this.hitbox.y;
        int center = this.hitbox.y + this.hitboxSizes.height / 2;
        int bottom = this.hitbox.y + this.hitboxSizes.height;
        
        // x-axis
        int left   = this.hitbox.x;
        int right  = this.hitbox.x + this.hitboxSizes.width;
        int middle = this.hitbox.x + this.hitboxSizes.width / 2;
        
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
            
            if(block.getBlocktype() == BlockType.SOLID || 
                    block.getBlocktype() == BlockType.PLATFORM ||
                    block instanceof Trap) {
                
                // when the actor is falling
                if(this.velocity.y > 0f) {
                    if(this.lastBlock != block) {
                        if(block.getBounds().contains(bl) || 
                                block.getBounds().contains(bc) || 
                                block.getBounds().contains(br)) {
                            
                            if(block instanceof Trap) {
                                Trap trap = (Trap) block;
                                this.getHP().takeDamage(trap.getDamage());
                                //this.isGrounded = false;
                            } else {
                                this.isGrounded = true;
                                this.lastBlock = block;
                            }
                            
                        }
                    }
                } else {
                    if(block == this.lastBlock) {
                        if(block.getBounds().contains(bl) == false && 
                                block.getBounds().contains(bc) == false && 
                                block.getBounds().contains(br) == false) {
                            
                            this.isGrounded = false;
                            this.lastBlock = null;
                        }
                    }
                }
                
                if(block.getBlocktype() != BlockType.PLATFORM) {
                    if(block.getBounds().contains(lc) ||
                            block.getBounds().contains(lt) ||
                            block.getBounds().contains(lb)) {
                        this.collisionLeft = true;
                    }
                    
                    if(block.getBounds().contains(rc) ||
                            block.getBounds().contains(rt) ||
                            block.getBounds().contains(rb)) {
                        this.collisionRight = true;
                    }
                    
                    if(block.getBounds().contains(tc) ||
                            block.getBounds().contains(tl) ||
                            block.getBounds().contains(tr)) {
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

    private List<Block> getNearbyBlocks(float colDist) {
        
        List<Block> allBlocks = new ArrayList<Block>();
        
        Vector2 me = new Vector2(this.getBounds().x, this.getBounds().y);
        
        for(Block block : Game.instance.getWorld().getCurrentRoomBlocks()) {
            
            Vector2 them = new Vector2(block.getBounds().x, block.getBounds().y);
            float distance = me.distance(them);
            
            if(distance <= colDist) {
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
}
