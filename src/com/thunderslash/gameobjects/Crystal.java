package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thunderslash.data.Animation;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.AnimationType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.AnimationCreator;
import com.thunderslash.utilities.SpriteCreator;

public class Crystal extends GameObject {

    private boolean isUsed = false;
    
    private BufferedImage usedSprite;
    private BufferedImage questionMark;
    
    private Animation bounceAnim;
    
    public Crystal(Point worldPos, SpriteType type) {
        super(worldPos, type);
        SpriteCreator sc = Game.instance.getSpriteCreator();
        this.questionMark = sc.CreateCustomSizeSprite(0, 6 * 32 + 6, 4, 9);
        this.bounceAnim = AnimationCreator.createAnimation(AnimationType.CRYSTAL_BOUNCE);
        this.usedSprite = sc.CreateSprite(SpriteType.CRYSTAL_USED);
    }

    public void use() {
        this.isUsed = true;
        this.hasFocus = false;
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
        
        if(this.hasFocus) {
            g.drawImage(this.questionMark,
                    this.hitboxCenter.x - this.questionMark.getWidth() / 2,
                    this.hitbox.y - this.questionMark.getHeight() - 30, null);
        }
        
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

}
