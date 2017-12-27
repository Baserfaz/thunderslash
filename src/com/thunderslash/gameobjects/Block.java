package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Coordinate;

public class Block extends GameObject {

    private BlockType blockType;
    private Coordinate gridPosition;
    
    public Block(Coordinate worldPos, Coordinate gridPosition, BlockType blockType, SpriteType type) {
        super(worldPos, type);
        
        this.blockType = blockType;
        this.gridPosition = gridPosition;
    }

    public void tick() {}
    
    public void render(Graphics g) {
        g.drawImage(sprite, worldPosition.x, worldPosition.y, null);
    }
    
    public Rectangle getBounds() {
        return this.hitbox;
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
}

