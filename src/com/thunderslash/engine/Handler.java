package com.thunderslash.engine;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.Animation;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.gameobjects.PhysicsObject;
import com.thunderslash.gameobjects.Player;
import com.thunderslash.utilities.Animator;

public class Handler {

    private List<GameObject> objects = new ArrayList<GameObject>();

    public void tickGameObjects() {
        for(int i = 0; i < objects.size(); i++) {
            GameObject current = objects.get(i);
            if(current != null) current.tick();
        }
    }
    
    public void tickAnimations() {
        Animator animator = Game.instance.getAnimator();
        if(animator == null || animator.getCurrentAnims().isEmpty()) return;
        for(Animation anim : animator.getCurrentAnims()) {
            anim.tick();
        }
        
        for(int i = animator.getCurrentAnims().size() - 1; i >= 0; i--) {
            
            Animation anim = animator.getCurrentAnims().get(i);
            
            if(anim.getHasFinished()) {
                animator.removeAnim(anim);
            }
            
        }
    }
    
    public void renderGameObjects(Graphics g) {
        
        // references
        List<Block> waterBlocks = new ArrayList<Block>();
        List<Actor> actors = new ArrayList<Actor>();
        List<PhysicsObject> items = new ArrayList<PhysicsObject>();
        Actor player = Game.instance.getActorManager().getPlayerInstance();
        if(player == null) return;
        
        // render all game objects 
        for(int i = 0; i < this.objects.size(); i++) {
            GameObject current = this.objects.get(i);
            
            if(current instanceof Player) continue;
            
            // get actors
            if(current instanceof Actor) {
                Actor actor = (Actor) current;
                actors.add(actor);
                continue;
            }
            
            // get water blocks
            if(current instanceof Block) {
                Block block = (Block) current;
                if(block.getBlocktype() == BlockType.WATER) {
                    waterBlocks.add(block);
                    continue;
                }
            }
            
            // get physicsObjects
            if(current instanceof PhysicsObject) {
                PhysicsObject obj = (PhysicsObject) current;
                items.add(obj);
                continue;
            }
            
            // nothing special -> just render it
            if(current.getIsVisible()) {
                current.render(g);
            }
        }
        
        // render items
        for(PhysicsObject obj : items) {
            obj.render(g);
        }
        
        // render other actors
        for(Actor actor : actors) {
            actor.render(g);
        }
        
        player.render(g);
        
        // render water
        for(Block block : waterBlocks) {
            block.render(g);
        }
    }

    public void AddObject(GameObject go) { this.objects.add(go); }	
    public void RemoveObject(GameObject go) { this.objects.remove(go); }

    public List<GameObject> getObjects() { return objects; }
    public void setObjects(List<GameObject> objects) { this.objects = objects; }

}
