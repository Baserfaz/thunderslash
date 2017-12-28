package com.thunderslash.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.gameobjects.Block;

public class Room {

    private int index;
    private int width, height;
    private List<Block> blocks;
    private List<BufferedImage> backGroundTiles;
    
    public Room(int index, int width, int height, List<Block> tiles) {
        this.blocks = tiles;
        this.index = index;
        this.width = width;
        this.height = height;
        this.backGroundTiles = new ArrayList<BufferedImage>();
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

    public List<BufferedImage> getBackGroundTiles() {
        return backGroundTiles;
    }

    public void setBackGroundTiles(List<BufferedImage> backGroundTiles) {
        this.backGroundTiles = backGroundTiles;
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
