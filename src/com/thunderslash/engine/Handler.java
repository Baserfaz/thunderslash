package com.thunderslash.engine;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import com.thunderslash.data.Animation;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.gameobjects.Crystal;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.gameobjects.PhysicsObject;
import com.thunderslash.gameobjects.Player;
import com.thunderslash.gameobjects.Projectile;
import com.thunderslash.gameobjects.VanityObject;
import com.thunderslash.particles.Emitter;
import com.thunderslash.particles.Particle;
import com.thunderslash.utilities.Animator;

public class Handler {

    private List<GameObject> objects = new ArrayList<GameObject>();

    public void tickGameObjects() {
        for(int i = 0; i < objects.size(); i++) {
            GameObject current = objects.get(i);
            if(current != null) current.tick();
        }
    }
    
    public void tickEmitters() {
        for(Emitter e : Game.instance.getEmitterManager().getEmitters()) {
            e.tick();
        }
    }
    
    public void renderParticles(Graphics g) {
        
        List<Emitter> emitters = Game.instance.getEmitterManager().getEmitters();
        try {
            for(Emitter e : emitters) {
                for (Particle p : e.getParticles()) {
                    p.render(g);
                }
            }
        } catch(ConcurrentModificationException e) { }
    }
    
    public void tickAnimations() {
        Animator animator = Game.instance.getAnimator();
        if(animator == null || animator.getCurrentAnims().isEmpty()) return;
        for(Animation anim : animator.getCurrentAnims()) {
            anim.tick();
        }
        
        for(int i = animator.getCurrentAnims().size() - 1; i >= 0; i--) {
            Animation anim = animator.getCurrentAnims().get(i);
            if(anim.getHasFinished()) animator.removeAnim(anim);
        }
    }
    
    public void renderGameObjects(Graphics g) {
        
        // references
        List<Block> waterBlocks = new ArrayList<Block>();
        List<Block> backgroundBlocks = new ArrayList<Block>();
        List<Block> solidBlocks = new ArrayList<Block>();
        List<Actor> actors = new ArrayList<Actor>();
        List<PhysicsObject> items = new ArrayList<PhysicsObject>();
        List<Crystal> crystals = new ArrayList<Crystal>();
        List<VanityObject> vanityObjects = new ArrayList<VanityObject>();
        List<Projectile> projectiles = new ArrayList<Projectile>();
        
        Actor player = Game.instance.getActorManager().getPlayerInstance();
        Camera cam = Game.instance.getCamera();
        if(player == null || cam == null) return;
        
        Block levelStart = null;
        Block levelEnd = null;
        
        // --------------------- calculate objs that camera sees ------------------------
        
        Rectangle camView = (Rectangle) cam.getCameraBounds().clone();
        
        int size = Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
        
        camView.x -= size;
        camView.width += 2 * size;
        
        camView.y -= size;
        camView.height += 2 * size;
        
        List<GameObject> objInView = new ArrayList<GameObject>();
        for(int i = 0; i < this.objects.size(); i++) {
            GameObject go = this.objects.get(i);
            if(go == null) continue;
            if(camView.contains(go.getHitboxCenter())) {
                objInView.add(go);
            }
        }
        
        // ---------------------- RENDER ---------------------------------
        
        // render all game objects 
        for(int i = 0; i < objInView.size(); i++) {
            GameObject current = objInView.get(i);
            
            if(current instanceof Player) continue;
            
            // get actors
            if(current instanceof Actor) {
                actors.add((Actor) current);
                continue;
            }
            
            // get physicsObjects
            if(current instanceof PhysicsObject) {
                items.add((PhysicsObject) current);
                continue;
            }
            
            // get crystals
            if(current instanceof Crystal) {
                crystals.add((Crystal)current);
                continue;
            }
            
            // get vanity objects
            if(current instanceof VanityObject) {
                vanityObjects.add((VanityObject)current);
                continue;
            }
            
            // get projectiles
            if(current instanceof Projectile) {
                projectiles.add((Projectile)current);
                continue;
            }
            
            // get blocks
            if(current instanceof Block) {
                Block block = (Block) current;
                BlockType type = block.getBlocktype();
                if(type == BlockType.WATER) waterBlocks.add(block);
                else if(type == BlockType.BACKGROUND) backgroundBlocks.add((Block) current);
                else if(type == BlockType.EXIT) levelEnd = block;
                else if(type == BlockType.PLAYER_SPAWN) levelStart = block;
                else solidBlocks.add(block);
            }
        }
        
        // render queue: back to front
        for(Block b : backgroundBlocks) { b.render(g); }
        if(levelStart != null) levelStart.render(g);
        if(levelEnd != null) levelEnd.render(g);
        for(Crystal c : crystals) { c.render(g); }
        for(PhysicsObject obj : items) { obj.render(g); }
        for(VanityObject v : vanityObjects) { v.render(g); }
        for(Projectile p : projectiles) { p.render(g); }
        for(Actor actor : actors) { actor.render(g); }
        player.render(g);
        for(Block block : waterBlocks) { block.render(g); }
        for(Block block : solidBlocks) { block.render(g); }
    }

    // ---- GETTERS & SETTERS ----
    public void AddObject(GameObject go) { this.objects.add(go); }	
    public void RemoveObject(GameObject go) { this.objects.remove(go); }
    public List<GameObject> getObjects() { return objects; }
    public void setObjects(List<GameObject> objects) { this.objects = objects; }
}
