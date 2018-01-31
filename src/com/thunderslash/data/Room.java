package com.thunderslash.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.Direction;
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
    
    public NeighborData getNeighbors(Block block, boolean getAllTypes) {
        
        NeighborData data = new NeighborData(block);
        Point p1 = block.getGridPosition();
        
        List<BlockType> allowedTypes = new ArrayList<BlockType>(
                Arrays.asList(
                        BlockType.PLAY_AREA, BlockType.PLAYER_SPAWN,
                        BlockType.PLATFORM, BlockType.EXIT, BlockType.HURT,
                        BlockType.WATER));
        
        for(Block b : this.data.getBlocks()) {
            
            if(getAllTypes == true || allowedTypes.contains(b.getBlocktype())) {
                
                Point p2 = b.getGridPosition();
                
                if(p1.x == p2.x && (p1.y - 1) == p2.y) {
                    data.setNeighbor(Direction.NORTH, b);
                } else if(p1.x == p2.x && (p1.y + 1) == p2.y) {
                    data.setNeighbor(Direction.SOUTH, b);
                } else if((p1.x - 1) == p2.x && p1.y == p2.y) {
                    data.setNeighbor(Direction.WEST, b);
                } else if((p1.x + 1) == p2.x && p1.y == p2.y) {
                    data.setNeighbor(Direction.EAST, b);
                } else if((p1.x - 1) == p2.x && (p1.y - 1) == p2.y) {
                    data.setNeighbor(Direction.NORTH_WEST, b);
                } else if((p1.x + 1) == p2.x && (p1.y - 1) == p2.y) {
                    data.setNeighbor(Direction.NORTH_EAST, b);
                } else if((p1.x - 1) == p2.x && (p1.y + 1) == p2.y) {
                    data.setNeighbor(Direction.SOUTH_WEST, b);
                } else if((p1.x + 1) == p2.x && (p1.y + 1) == p2.y) {
                    data.setNeighbor(Direction.SOUTH_EAST, b);
                }
            }
        }
        return data;
    }
    
    public void activateRoom() {
        for(Block b : this.data.getBlocks()) { if(b != null) b.activate(); }
        for(Block b : this.data.getBackground()) { if(b != null) b.activate(); }
        for(VanityObject v : this.data.getVanityObjects()) { if(v != null) v.activate(); }
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
