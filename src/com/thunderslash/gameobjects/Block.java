package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;

import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.SpriteType;

public class Block extends GameObject {

    private BlockType blockType;
    private Point gridPosition;
    
    public Block(Point worldPos, Point gridPosition, BlockType blockType, SpriteType type) {
        super(worldPos, type);
        
        this.blockType = blockType;
        this.gridPosition = gridPosition;
    }

    public void tick() {}
    
    public void render(Graphics g) {
        if(this.isVisible) g.drawImage(defaultStaticSprite, worldPosition.x, worldPosition.y, null);
    }
    
    // ------------- GETTERS & SETTERS ----------------
    
    public BlockType getBlocktype() { return blockType; }
    public void setBlocktype(BlockType blocktype) { this.blockType = blocktype; }
    public Point getGridPosition() { return gridPosition; }
    public void setGridPosition(Point gridPosition) { this.gridPosition = gridPosition; }
}

