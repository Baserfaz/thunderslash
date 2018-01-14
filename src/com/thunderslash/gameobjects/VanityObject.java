package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;

import com.thunderslash.enumerations.SpriteType;

public class VanityObject extends GameObject {

    public VanityObject(Point worldPos, SpriteType type) {
        super(worldPos, type);
    }

    public void tick() {}
    public void render(Graphics g) {
        if(this.isVisible) {
            g.drawImage(this.defaultStaticSprite, this.worldPosition.x, this.worldPosition.y, null);
        }
    }

}
