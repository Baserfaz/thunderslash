package com.thunderslash.gameobjects;

import java.awt.Graphics;

import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Coordinate;

public class Block extends GameObject {

    private BlockType blockType;
    private Coordinate gridPosition;
    
    private GameObject item;
    
    public Block(Coordinate worldPos, Coordinate gridPosition, BlockType blockType, SpriteType type) {
        super(worldPos, type);
        
        this.blockType = blockType;
        this.gridPosition = gridPosition;
    }

    public void tick() {}
    
    public void render(Graphics g) {
        g.drawImage(sprite, worldPosition.x, worldPosition.y, null);
    }

    public BlockType getBlocktype() {
        return blockType;
    }

    public void setBlocktype(BlockType blocktype) {
        this.blockType = blocktype;
    }

    public Coordinate getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Coordinate gridPosition) {
        this.gridPosition = gridPosition;
    }

    public GameObject getItem() {
        return item;
    }

    public void setItem(GameObject item) {
        this.item = item;
    }
}

