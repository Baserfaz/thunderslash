package com.thunderslash.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.gameobjects.Block;

public class Room {

    private int index;
    private int width, height;
    private List<Block> blocks;
    
    public Room(int index, int width, int height, List<Block> tiles) {
        this.blocks = tiles;
        this.index = index;
        this.width = width;
        this.height = height;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
}
