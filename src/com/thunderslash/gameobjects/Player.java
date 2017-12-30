package com.thunderslash.gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Coordinate;
import com.thunderslash.utilities.RenderUtils;

public class Player extends Actor {

    private BufferedImage light;
    
    public Player(String name, Coordinate worldPos, 
            SpriteType spriteType, int hp) {
        super(name, worldPos, spriteType, hp);
    
        // create light sprite
        this.light = RenderUtils.scaleSprite(Game.instance.getSpriteCreator().
                CreateSprite(SpriteType.LIGHT_CIRCLE), 1);
        
    }
    
    public void tick() {
        super.tick();
        
        System.out.println(this.HP.getCurrentHP());
    }
    
    public void render(Graphics g) {
        
        // render light
        g.drawImage(light,
                this.hitboxCenter.x - this.light.getWidth() / 2,
                this.hitboxCenter.y - this.light.getHeight() / 2, null);
        
        // render current sprite
        if(this.facingDirection == Direction.EAST) {
            g.drawImage(this.sprite, this.worldPosition.x, this.worldPosition.y, null);
        } else if(this.facingDirection == Direction.WEST) {
            RenderUtils.renderSpriteFlippedHorizontally(sprite, this.worldPosition, g);
        }
        
        if(Game.drawCurrentBlock) {
            if(this.lastBlock != null) {
                g.setColor(Game.currentBlockColor);
                Rectangle r = this.lastBlock.getBounds();
                g.drawRect(r.x, r.y, r.width, r.height);
            }
        }
        
    }
}
