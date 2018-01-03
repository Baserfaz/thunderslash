package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thunderslash.data.Animation;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.AnimationType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.AnimationCreator;

public class Crystal extends GameObject {

    private boolean isUsed = false;
    private BufferedImage usedSprite;
    
    private int powerValue = 3;
    
    private Animation bounceAnim;
    
    public Crystal(Point worldPos, SpriteType type) {
        super(worldPos, type);
        
        // modify animation speed
        this.frameTime = 150.0;
        
        this.bounceAnim = AnimationCreator.createAnimation(AnimationType.CRYSTAL_BOUNCE);
        this.usedSprite = Game.instance.getSpriteCreator().CreateSprite(SpriteType.CRYSTAL_USED);
    }

    public void absorb() {
        if(this.isUsed == false) {
            this.isUsed = true;
        }
    }
    
    public void tick() {}

    public void render(Graphics g) {
        
        BufferedImage frame = null;

        if(this.isUsed) frame = this.usedSprite;
        else frame = this.bounceAnim.getFrame(this.currentAnimIndex);
        
        // updates animation index
        this.calculateAnimations(this.bounceAnim);
        
        if(this.isVisible) {
            g.drawImage(frame, this.worldPosition.x, this.worldPosition.y, null);
        }
    }

    public int getPowerValue() {
        return powerValue;
    }

    public void setPowerValue(int powerValue) {
        this.powerValue = powerValue;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

}
