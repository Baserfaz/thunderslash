package com.thunderslash.gameobjects;


import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Coordinate;

public class Player extends Actor {
    
    public Player(String name, Coordinate worldPos, 
            SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
    }
}
