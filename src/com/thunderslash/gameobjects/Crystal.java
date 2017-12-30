package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Coordinate;

public class Crystal extends GameObject {

    private boolean isUsed = false;
    private BufferedImage usedSprite;
    
    public Crystal(Coordinate worldPos, SpriteType type) {
        super(worldPos, type);
        
        this.usedSprite = Game.instance.getSpriteCreator().CreateSprite(SpriteType.CRYSTAL_USED);
    }

    public void absorb() {
        if(this.isUsed == false) {
            System.out.println("ABSORBED A CRYSTAL!");
            this.isUsed = true;
        }
    }
    
    public void tick() {}

    public void render(Graphics g) {
        if(this.isVisible) {
            if(this.isUsed) {
                g.drawImage(usedSprite, this.worldPosition.x, this.worldPosition.y, null);
            } else {
                g.drawImage(sprite, this.worldPosition.x, this.worldPosition.y, null);
            }
        }
    }

}
