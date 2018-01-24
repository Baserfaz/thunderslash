package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thunderslash.data.Animation;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.AnimationType;
import com.thunderslash.enumerations.SpriteType;

public class Torch extends VanityObject {

    private Animation torchFlicked;
    private BufferedImage frame = null;
    
    public Torch(Point worldPos) {
        super(worldPos, SpriteType.TORCH);
        this.torchFlicked = Game.instance.getSpriteStorage().getAnimation(AnimationType.TORCH);
    }
    
    public void tick() {
        if(this.isEnabled) {
            this.frame = this.torchFlicked.getFrame(this.currentAnimIndex);
            this.calculateAnimations(this.torchFlicked);
        }
    }
    
    public void render(Graphics g) {
        if(this.isVisible) {
            if(this.frame == null) this.frame = this.defaultStaticSprite;
            g.drawImage(this.frame, this.worldPosition.x, this.worldPosition.y, null);
        }
    }
    
}
