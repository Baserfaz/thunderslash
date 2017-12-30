package com.thunderslash.gameobjects;

import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Coordinate;

public class Trap extends Block {

    private int damage;
    
    public Trap(Coordinate worldPos, Coordinate gridPosition, 
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
