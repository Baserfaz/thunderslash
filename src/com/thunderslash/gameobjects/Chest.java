package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.SpriteCreator;

public class Chest extends PhysicsObject {

    private boolean isOpen = false;
    
    private BufferedImage openSprite;
    private BufferedImage questionMark;
    
    public Chest(Point worldPos, SpriteType spriteType) {
        super(worldPos, spriteType);
        
        SpriteCreator sc = Game.instance.getSpriteCreator();
        
        this.questionMark = sc.CreateCustomSizeSprite(0, 6 * 32 + 6, 4, 9);
        this.openSprite = sc.CreateSprite(SpriteType.CHEST_OPEN);
    }

    public void render(Graphics g) {
        if(this.isOpen) {
            g.drawImage(this.openSprite, worldPosition.x, worldPosition.y, null);
        } else {
            g.drawImage(this.defaultStaticSprite, worldPosition.x, worldPosition.y, null);
        }
        
        if(this.hasFocus) {
            g.drawImage(this.questionMark,
                    this.hitboxCenter.x - this.questionMark.getWidth() / 2,
                    this.hitbox.y - this.questionMark.getHeight() - 30, null);
        }
        
    }

    public void open() {
        this.isOpen = true;
        this.hasFocus = false;
    }
    
    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

}
