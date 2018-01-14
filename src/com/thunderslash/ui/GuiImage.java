package com.thunderslash.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.DepthLevel;

public class GuiImage extends GuiElement {

    private BufferedImage img;
    private DepthLevel deptLevel;
    
    public GuiImage(int x, int y, BufferedImage img, DepthLevel deptLevel, boolean isEnabled) {
        super(x, y, img.getWidth(), img.getHeight());
        this.img = img;
        this.deptLevel = deptLevel;
        this.isEnabled = isEnabled;
    }

    public void render(Graphics g) {
        if(this.isVisible) g.drawImage(this.img, (int)this.x, (int)this.y, null);
    }

    public void tick() {
        
        int spriteSize = Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
        float speed = 2f;
        
        if(this.deptLevel == DepthLevel.FOREGROUND) this.y += speed;
        else if(this.deptLevel == DepthLevel.BACKGROUND) this.y += speed / 2;
        
        if(this.y > Game.HEIGHT + spriteSize) this.y = -spriteSize;
    }
    
    public void onClick() {
        if(this.isEnabled) {}
    }
    
    public void onHover() {
        if(this.isEnabled) {}
    }

    // ----- GETTERS & SETTERS -----
    public BufferedImage getImg() { return img; }
    public void setImg(BufferedImage img) { this.img = img; }
    public DepthLevel getDeptLevel() { return deptLevel; }
    public void setDeptLevel(DepthLevel deptLevel) { this.deptLevel = deptLevel; }
}
