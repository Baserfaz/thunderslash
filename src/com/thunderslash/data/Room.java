package com.thunderslash.data;

import java.util.List;

import com.thunderslash.gameobjects.Block;

public class Room {

    private int index;
    private List<Block> blocks;
    
    public Room(int index, List<Block> tiles) {
        this.blocks = tiles;
        this.index = index;
    }
    
    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
}
