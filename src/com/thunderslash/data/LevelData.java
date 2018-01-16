package com.thunderslash.data;

import java.util.ArrayList;
import java.util.List;

import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.gameobjects.VanityObject;

public class LevelData {

    private int width;
    private int height;
    private int[] pixels;
    
    private List<Block> blocks;
    private List<Block> background;
    private List<VanityObject> vanityObjects;
    private List<GameObject> items;
    private List<Actor> actors;
    
    private List<GameObject> gameObjects;
    
    public LevelData(int width, int height, List<Block> blocks, 
            List<VanityObject> vanityObjects, List<GameObject> items, List<Actor> actors) {
        this.width = width;
        this.height = height;
        this.blocks = blocks;
        this.vanityObjects = vanityObjects;
        this.items = items;
        this.actors = actors;
        this.background = new ArrayList<Block>();
    }
    
    public LevelData(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.blocks = new ArrayList<Block>();
    }

    
    // ---- GETTERS & SETTERS ----
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int[] getPixels() { return pixels; }
    public void setPixels(int[] pixels) { this.pixels = pixels; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public List<Block> getBlocks() { return blocks; }
    public void setBlocks(List<Block> blocks) { this.blocks = blocks; }
    public List<VanityObject> getVanityObjects() { return vanityObjects; }
    public void setVanityObjects(List<VanityObject> vanityObjects) { this.vanityObjects = vanityObjects; }
    public List<GameObject> getItems() { return items; }
    public void setItems(List<GameObject> items) { this.items = items; }
    public List<Actor> getActors() { return actors; }
    public void setActors(List<Actor> actors) { this.actors = actors; }
    public List<Block> getBackground() { return background; }
    public void setBackground(List<Block> background) { this.background = background; }
    public List<GameObject> getGameObjects() { return this.gameObjects; }
    
}
