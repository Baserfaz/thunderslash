package com.thunderslash.data;

import java.util.HashMap;
import java.util.Map;

import com.thunderslash.enumerations.Direction;
import com.thunderslash.gameobjects.Block;

public class NeighborData {

    private Map<Direction, Block> neighbors;
    private Block me = null;
    
    public NeighborData(Block me, Map<Direction, Block> neighbors) {
        this.neighbors = neighbors;
        this.me = me;
    }
    
    public NeighborData(Block me) {
        this.me = me;
        this.neighbors = new HashMap<Direction, Block>();
    }

    public NeighborData() {
        this.me = null;
        this.neighbors = new HashMap<Direction, Block>();
    }
    
    public NeighborData(Block me, Block N, Block E, Block S, Block W) {
        this.me = me;
        this.neighbors = new HashMap<Direction, Block>();
        this.neighbors.put(Direction.NORTH, N);
        this.neighbors.put(Direction.WEST, W);
        this.neighbors.put(Direction.SOUTH, S);
        this.neighbors.put(Direction.EAST, E);
    }
    
    public Map<Direction, Block> getNeighbors() {
        return neighbors;
    }

    public void setNeighbor(Direction dir, Block b) {
        if(this.neighbors.containsKey(dir) == false) {
            this.neighbors.put(dir, b);
        } else {
            System.out.println("Already has a neighbor in direction " + dir);
        }
    }
    
    public void setNeighbors(Map<Direction, Block> neighbors) {
        this.neighbors = neighbors;
    }

    public Block getMe() {
        return me;
    }

    public void setMe(Block me) {
        this.me = me;
    }
    
    
}
