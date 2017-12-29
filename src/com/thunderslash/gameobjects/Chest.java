package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Coordinate;

public class Chest extends GameObject {

    private boolean isOpen = false;
    private BufferedImage openSprite;
    
    public Chest(Coordinate worldPos, SpriteType closedSpriteType, SpriteType openSpriteType) {
        super(worldPos, closedSpriteType);
        
        this.openSprite = Game.instance.getSpriteCreator().CreateSprite(openSpriteType);
    }

    public void tick() {}

    public void render(Graphics g) {
        if(this.isOpen) {
            g.drawImage(this.openSprite, worldPosition.x, worldPosition.y, null);
        } else {
            g.drawImage(this.sprite, worldPosition.x, worldPosition.y, null);
        }
    }

    public void open() {
        this.isOpen = true;
        
        // TODO: loot
    }
    
    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

}
