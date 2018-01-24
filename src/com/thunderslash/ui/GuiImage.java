package com.thunderslash.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.DepthLevel;
import com.thunderslash.enumerations.GuiAnimationType;
import com.thunderslash.enumerations.InteractAction;

public class GuiImage extends InteractableGuiElement {

    private BufferedImage img;
    private DepthLevel deptLevel;
    private GuiAnimationType guiAnimType;
    
    private float speed = 1f;
    
    public GuiImage(int x, int y, BufferedImage img, boolean isEnabled, 
            DepthLevel deptLevel, GuiAnimationType animType,
            InteractAction onClickAction, InteractAction onHoverAction) {
        super(x, y, img.getWidth(), img.getHeight(), onClickAction, onHoverAction);
        this.img = img;
        this.deptLevel = deptLevel;
        this.guiAnimType = animType;
        this.isEnabled = isEnabled;
        this.isMuted = true;
    }
    
    // a shorthand constructor.
    public GuiImage(int x, int y, BufferedImage img, boolean isEnabled,
            InteractAction onClickAction, InteractAction onHoverAction) {
        super(x, y, img.getWidth(), img.getHeight(), onClickAction, onHoverAction);
        this.img = img;
        this.deptLevel = DepthLevel.FOREGROUND;
        this.guiAnimType = GuiAnimationType.NONE;
        this.isEnabled = isEnabled;
        this.isMuted = true;
    }

    public void render(Graphics g) {
        if(this.isVisible) g.drawImage(this.img, (int)this.x, (int)this.y, null);
    }
    
    public void tick() {
        
        if(this.guiAnimType == GuiAnimationType.SCROLL_DOWN) this.scrollDown();
        else if(this.guiAnimType == GuiAnimationType.SCROLL_UP) this.scrollUp();
        
    }
    
    private void scrollUp() {
        this.y -= speed;
        if(this.y + this.img.getHeight() < 0) this.y += this.img.getHeight() * 2;
    }
    
    private void scrollDown() {
        this.y += speed;
        if(this.y > Game.HEIGHT) this.y += -this.img.getHeight() * 2;
    }

    // ----- GETTERS & SETTERS -----
    public BufferedImage getImg() { return img; }
    public void setImg(BufferedImage img) { this.img = img; }
    public DepthLevel getDeptLevel() { return deptLevel; }
    public void setDeptLevel(DepthLevel deptLevel) { this.deptLevel = deptLevel; }
}
