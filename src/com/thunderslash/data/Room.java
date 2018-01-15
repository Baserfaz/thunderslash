package com.thunderslash.data;

import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.gameobjects.VanityObject;

public class Room {

    private int index;
    private int width, height;
    private LevelData data;
    
    public Room(int index, LevelData data) {
        this.index = index;
        this.width = 0;
        this.height = 0;
        this.data = data;
    }
    
    public void activateRoom() {
        for(Block b : this.data.getBlocks()) { if(b != null) b.activate(); }
        for(Block b : this.data.getBackground()) { if(b != null) b.activate(); }
        for(VanityObject v : this.data.getVanityObjects()) { if(v != null) v.activate(); }
        for(Actor a : this.data.getActors()) { if(a != null) a.activate(); }
        for(GameObject i : this.data.getItems()) { if(i != null) i.activate(); }
    }
    
    public void deactivateRoom() {
        for(Block b : this.data.getBlocks()) { if(b != null) b.deactivate(); }
        for(Block b : this.data.getBackground()) { if(b != null) b.deactivate(); }
        for(VanityObject v : this.data.getVanityObjects()) { if(v != null) v.deactivate(); }
        for(Actor a : this.data.getActors()) { if(a != null) a.deactivate(); }
        for(GameObject i : this.data.getItems()) { if(i != null) i.deactivate(); }
    }
    
    // ---------------- GETTERS & SETTERS -------------------
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public LevelData getData() { return data; }
    public void setData(LevelData data) { this.data = data; }

}
