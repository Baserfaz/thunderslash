package com.thunderslash.data;

import java.util.ArrayList;
import java.util.List;

import com.thunderslash.gameobjects.Block;

public class LevelData {

    private int width;
    private int height;
    private int[] pixels;
    private List<Block> blocks;
    
    public LevelData(int width, int height, List<Block> blocks) {
        this.width = width;
        this.height = height;
        this.blocks = blocks;
    }
    
    public LevelData(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.blocks = new ArrayList<Block>();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
    
}
