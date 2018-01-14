package com.thunderslash.gameobjects;

import java.awt.Point;

import com.thunderslash.enumerations.SpriteType;

public class Torch extends VanityObject {

    public Torch(Point worldPos) {
        super(worldPos, SpriteType.TORCH);
    }
}
