package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SoundEffect;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Mathf;
import com.thunderslash.utilities.Vector2;

public abstract class PhysicsObject extends GameObject {

    // default values
    protected float maxVerticalSpeed = 5.5f * Game.SPRITESIZEMULT;
    protected float maxHorizontalSpeed = 1f * Game.SPRITESIZEMULT;
    protected float maxVerticalAccel = 0.22f * Game.SPRITESIZEMULT;
    protected float maxHorizontalAccel = 0.25f * Game.SPRITESIZEMULT;
    protected float horizontalAccelMult = 0.35f * Game.SPRITESIZEMULT;
    protected float jumpForce = -0.24f * Game.SPRITESIZEMULT;
    protected float friction = 0.1f * Game.SPRITESIZEMULT;
    protected float collisionDistance = 50f * Game.SPRITESIZEMULT;
    protected float deaccelerationRate = 0.03f;
    
    protected Vector2 velocity = new Vector2();
    protected Vector2 acceleration = new Vector2();
    protected Vector2 direction = new Vector2();
    
    private boolean lastGrounded = false;
    private Block lastBlock = null;
    
    // collisions
    protected boolean isGrounded = false;
    protected boolean collisionLeft = false;
    protected boolean collisionRight = false;
    protected boolean collisionTop = false;
    protected boolean collidedWithTrap = false;
    
    // collections
    private List<Point> collisionPoints;
    
    public PhysicsObject(Point worldPos, SpriteType type) {
        super(worldPos, type);
    }
    
    public void tick() {
        if(this.isEnabled) {
            this.updateCollisions();
            
            if(this.lastGrounded == false && this.isGrounded) this.onLanding();
            
            this.move();
            this.updateHitbox();
            
            this.lastGrounded = this.isGrounded;
        }
    }
    
    public void render(Graphics g) {}
    protected abstract void onLanding();
    
    protected void knockback(GameObject target, Direction dir) {
        
        if(target instanceof PhysicsObject) {
            PhysicsObject obj = (PhysicsObject) target;
            
            int d = 0;
            
            if(dir == Direction.WEST) d = -1; 
            else if(dir == Direction.EAST) d = 1;
            else System.out.println("Physicsobject::knockback: unsupported direction: " + dir);
            
            float xforce = 1f * d;
            float yforce = -0.5f;
            
            obj.isGrounded = false;
            
            obj.velocity.x = xforce;
            obj.acceleration.x = xforce;
        
            obj.velocity.y = yforce;
            obj.acceleration.y = yforce;
        }
    }
    
    private void move() {
        
        this.handleCollisions();
        
        // horizontal movement
        if(this instanceof Actor) {
            
            // if the actor is stunned
            // dont read the inputs
            if(((Actor) this).isStunned) {
                this.direction.x = 0f;
                this.direction.y = 0f;
            }
            
            this.acceleration.x = this.direction.x * this.horizontalAccelMult;
        }
        
        if(this.isGrounded) {
            
            if(this instanceof Actor) {
                
                // jumping
                if(this.direction.y > 0f) {
                    this.acceleration.y = this.jumpForce;
                    this.isGrounded = false;
                    this.direction.y = 0f;
                    
                    if(this instanceof Player) {
                        Game.instance.getSoundManager().playSound(SoundEffect.PLAYER_JUMP);
                    } else if(this instanceof Enemy) {
                        Game.instance.getSoundManager().playSoundWithPan(SoundEffect.SLIME_JUMP, this);
                    }
                    
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
        
        // de-accelerate
        if(this.acceleration.x > 0f) this.acceleration.x -= this.deaccelerationRate;
        else if(this.acceleration.x < 0f) this.acceleration.x += this.deaccelerationRate;
        
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

        List<Point> collisionPoints = this.createCollisionPoints();
        
        for(GameObject go : this.getNearbyGameObjects(this.collisionDistance, true)) {
            
            if(go instanceof Block) {
                this.handleBlockCollisions(go, collisionPoints);
            } else if(go instanceof PhysicsObject) {
                this.handlePhysicsObjectCollisions(go);
            }
        }
        
        if(Game.drawActorCollisionPoints) {
            this.collisionPoints = new ArrayList<Point>(collisionPoints);
        }
    }
    
    private List<Point> createCollisionPoints() {
        
        int margin = 3 * Game.SPRITESIZEMULT;
        int bottomMargin = 1 * Game.SPRITESIZEMULT;
        
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
        Point bl = new Point(left + bottomMargin, bottom);
        Point bc = new Point(middle, bottom);
        Point br = new Point(right - bottomMargin, bottom);
        
        // center
        Point c = new Point(middle, center);
        
        return Arrays.asList(lc, lt, lb, rc, rt, rb, tc, tl, tr, bl, bc, br, c);
    }
    
    private void handlePhysicsObjectCollisions(GameObject go) {}
    
    private void handleBlockCollisions(GameObject go, List<Point> points) {
        
        Block block = (Block) go;
        
        // open collision points
        Point lc = points.get(0);
        Point lt = points.get(1);
        Point lb = points.get(2);
        
        Point rc = points.get(3);
        Point rt = points.get(4);
        Point rb = points.get(5);
        
        Point tc = points.get(6);
        Point tl = points.get(7);
        Point tr = points.get(8);
        
        Point bl = points.get(9);
        Point bc = points.get(10);
        Point br = points.get(11);
        
        // Point c = points.get(12);
        
        // only check collisions against these
        if(block.getBlocktype() == BlockType.SOLID || 
                block.getBlocktype() == BlockType.PLATFORM ||
                        block.getBlocktype() == BlockType.HURT) {
            
            Rectangle blockHitbox = go.getHitbox();
            
            // when the actor is falling
            if(this.velocity.y > 0f) {
                
                // checking whether the actor hit ground.
                
                if(this.lastBlock != block) {
                    if(blockHitbox.contains(bl) || blockHitbox.contains(bc) || blockHitbox.contains(br)) {
                        
                        // landed on hurt block
                        // actor takes damage.
                        if(block.getBlocktype() == BlockType.HURT) {
                            Trap trap = (Trap) block;
                            if(this instanceof Actor) {
                                ((Actor)this).getHP().takeDamage(trap.getDamage());
                                
                                if(this instanceof Player) {
                                    System.out.println("HIT A TRAP! OUCH");
                                }
                                
                            }
                        }
                        
                        this.isGrounded = true;
                        this.lastBlock = block;
                    }
                }
                
            } else {
                
                // this block is executed when the actor is grounded,
                // to check if the player is still on ground.
                
                if(block == this.lastBlock) {
                    
                    // only check collisions with the 
                    // current block the actor is standing on.
                    
                    if(blockHitbox.contains(bl) == false && 
                            blockHitbox.contains(bc) == false &&
                            blockHitbox.contains(br) == false) {
                        this.isGrounded = false;
                        this.lastBlock = null;
                    }
                    
                } else {
                    
                    // check other nearby blocks
                    // if we are standing on them.
                    
                    if(blockHitbox.contains(bl) || 
                            blockHitbox.contains(br) ||
                            blockHitbox.contains(bc)) {
                        this.isGrounded = true;
                        this.lastBlock = block;
                    }
                    
                }
            }
            
            if(block.getBlocktype() != BlockType.PLATFORM) {
                if(blockHitbox.contains(lc) || blockHitbox.contains(lt) || blockHitbox.contains(lb)) {
                    this.collisionLeft = true;
                }
                
                if(blockHitbox.contains(rc) || blockHitbox.contains(rt) || blockHitbox.contains(rb)) {
                    this.collisionRight = true;
                }
                
                if(blockHitbox.contains(tc) || blockHitbox.contains(tl) || blockHitbox.contains(tr)) {
                    this.collisionTop = true;
                }
            }
        }
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

    // ----- GETTERS & SETTERS -------
    public List<Point> getCollisionPoints() { return collisionPoints; }
    public void setCollisionPoints(List<Point> collisionPoints) { this.collisionPoints = collisionPoints; }
    public Block getLastBlock() { return lastBlock; }
    public void setLastBlock(Block lastBlock) { this.lastBlock = lastBlock; }
}
