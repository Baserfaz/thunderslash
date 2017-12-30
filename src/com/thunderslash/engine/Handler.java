package com.thunderslash.engine;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.enumerations.BlockType;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.gameobjects.Player;

public class Handler {

    private List<GameObject> objects = new ArrayList<GameObject>();

    public void tick() {
        for(int i = 0; i < objects.size(); i++) {
            GameObject current = objects.get(i);
            if(current != null) current.tick();
        }
    }
    
    public void render(Graphics g) {

        // render queue
        // background
        // blocks
        // items
        // actors
        // player
        // water
        
        // references
        List<Block> waterBlocks = new ArrayList<Block>();
        List<Actor> actors = new ArrayList<Actor>();
        Actor player = Game.instance.getActorManager().getPlayerInstance();
        if(player == null) return;
        
        // render all game objects 
        for(int i = 0; i < objects.size(); i++) {
            GameObject current = objects.get(i);
            
            if(current.getIsVisible() && Game.drawGameObjectRects) {
                Rectangle bounds = current.getBounds();
                g.setColor(Game.gameObjectRectColor);
                g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
            
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
            
            // nothing special -> just render it
            if(current.getIsVisible()) {
                current.render(g);
            }
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
