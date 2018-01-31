package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.ParticleType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.particles.Emitter;
import com.thunderslash.utilities.SpriteCreator;

public class Chest extends GameObject {

    private int openingScore;
    
    private boolean isOpen = false;
    
    private BufferedImage openSprite;
    private BufferedImage questionMark;
    
    private Emitter particleEmitter;
    
    public Chest(Point worldPos, SpriteType spriteType) {
        super(worldPos, spriteType);
        
        this.openingScore = 50;
        
        // cache sprites
        SpriteCreator sc = Game.instance.getSpriteCreator();
        this.questionMark = sc.CreateCustomSizeSprite(0, 6 * 32 + 16, 10, 16, 2);
        this.openSprite = sc.CreateSprite(SpriteType.CHEST_OPEN);
        
        this.particleEmitter = Game.instance.getEmitterManager().createEmitter(this);
    }

    public void tick() {}
    
    public void render(Graphics g) {
        if(this.isVisible) {
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
        
    }

    public void onLanding() {}
    
    public void open() {
        if(this.isEnabled) {
            this.isOpen = true;
            this.hasFocus = false;
            this.particleEmitter.emit(30, null, ParticleType.GOLD);
            Game.instance.getSession().addScore(this.openingScore);
        }
    }
    
    // ---- GETTERS & SETTERS ----
    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean isOpen) { this.isOpen = isOpen; }
    public int getOpeningScore() { return openingScore; }
    public void setOpeningScore(int openingScore) { this.openingScore = openingScore; }
}
