package com.thunderslash.gameobjects;

import java.awt.Point;

import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.SpriteType;

public class Trap extends Block {

    private int damage;
    
    public Trap(Point worldPos, Point gridPosition, 
            BlockType blockType, SpriteType type, int damage) {
        super(worldPos, gridPosition, blockType, type);
        
        this.damage = damage;
        
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

}
