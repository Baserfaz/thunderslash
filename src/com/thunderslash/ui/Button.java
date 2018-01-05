package com.thunderslash.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ButtonAction;
import com.thunderslash.enumerations.GameState;
import com.thunderslash.utilities.RenderUtils;

public class Button extends GuiElement {

    private Color fontColor;
    private Color bgColor;
    private int fontSize;
    private BufferedImage img;
    private String txt;
    private ButtonAction action;
    
    public Button(int x, int y, int width, int height, 
            String txt, Color fontColor, Color bgColor, int fontSize, ButtonAction action) {
        
        super(x, y, width, height);
        this.fontColor = fontColor;
        this.bgColor = bgColor;
        this.fontSize = fontSize;
        this.txt = txt;
        this.action = action;
        
    }
    
    public Button(int x, int y, BufferedImage img) {
        super(x, y, img.getWidth(), img.getHeight());
        this.img = img;
    }
    
    public void render(Graphics g) {
        if(this.isVisible()) {
            
            if(this.img != null) {
                
                if(this.isHovering) RenderUtils.tint(this.img, true, 2);
                else g.drawImage(this.img, this.x, this.y, null);
                
            } else {
                
                Font font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, this.fontSize);
                g.setFont(font);
                
                Rectangle r = new Rectangle(this.x, this.y, this.width, this.height);
                
                if(this.isHovering) g.setColor(this.bgColor.darker());
                else g.setColor(this.bgColor);
                
                // render box
                g.fillRect(r.x, r.y, r.width, r.height);
                
                int txtWidth = g.getFontMetrics().stringWidth(this.txt);
                int txtHeight = g.getFontMetrics().getHeight();
                
                int centerX = (r.x + r.width / 2) - txtWidth / 2;
                int centerY = (r.y + txtHeight + r.height / 2) - txtHeight / 2;
                
                // render text inside it
                g.setColor(this.fontColor);
                g.drawString(this.txt, centerX, centerY);
            }
        }
    }

    public void tick() {
        if(this.isEnabled()) {}
    }

    public void onClick() {
        switch(this.action) {
            case EXIT:
                System.exit(0);
                break;
            case PLAY:
                Game.instance.startNewGame();
                break;
            default:
                System.out.println("Not supported action: " + this.action);
                break;
        }
    }
    
    // ------- GETTERS & SETTERS --------
    public void onHover() { this.isHovering = true; }
    public Color getFontColor() { return fontColor; }
    public void setFontColor(Color fontColor) { this.fontColor = fontColor; }
    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }
    public Color getBgColor() { return bgColor; }
    public void setBgColor(Color bgColor) { this.bgColor = bgColor; }
    public ButtonAction getAction() { return action; }
    public void setAction(ButtonAction action) { this.action = action; }
}
