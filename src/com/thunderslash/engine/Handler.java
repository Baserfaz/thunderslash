package com.thunderslash.engine;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.GameObject;

public class Handler {

    private List<GameObject> objects = new ArrayList<GameObject>();

    public void tick() {
        for(int i = 0; i < objects.size(); i++) {
            GameObject current = objects.get(i);
            if(current != null) current.tick();
        }
    }
    
    public void render(Graphics g) {

        // references
        // Camera cam = Game.instance.getCamera();
        Actor player = Game.instance.getActorManager().getPlayerInstance();
        if(player == null) return;
        
        // render all game objects 
        for(int i = 0; i < objects.size(); i++) {
            GameObject current = objects.get(i);
            
            if(current.getIsVisible()) {
                current.render(g);
                
                
                if(Game.drawGameObjectRects) {
                    Rectangle bounds = current.getBounds();
                    g.setColor(Game.gameObjectRectColor);
                    g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
                }
            }
        }
    }

    public void AddObject(GameObject go) { this.objects.add(go); }	
    public void RemoveObject(GameObject go) { this.objects.remove(go); }

    public List<GameObject> getObjects() { return objects; }
    public void setObjects(List<GameObject> objects) { this.objects = objects; }

}
