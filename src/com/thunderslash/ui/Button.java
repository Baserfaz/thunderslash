package com.thunderslash.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;

public class Button extends GuiElement {

    private Color fontColor;
    private Color bgColor;
    private int fontSize;
    private BufferedImage img;
    private String txt;
    
    public Button(int x, int y, int width, int height, 
            String txt, Color fontColor, Color bgColor, int fontSize) {
        super(x, y, width, height);
        this.fontColor = fontColor;
        this.bgColor = bgColor;
        this.fontSize = fontSize;
        this.txt = txt;
    }
    
    public Button(int x, int y, BufferedImage img) {
        super(x, y, img.getWidth(), img.getHeight());
        this.img = img;
    }
    
    public void render(Graphics g) {
        if(this.isVisible()) {
            
            if(this.img != null) g.drawImage(this.img, this.x, this.y, null);
            else {
                
                Font font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, this.fontSize);
                g.setFont(font);
                
                Rectangle r = new Rectangle(this.x, this.y, this.width, this.height);
                
                // render box
                g.setColor(this.bgColor);
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
        if(this.isEnabled()) {
            
        }
    }

    public void onClick() {
        if(this.isEnabled()) {
            System.out.println("Clicked button: " + this.toString());
        }
    }
    
    public Color getFontColor() {
        return fontColor;
    }
    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }
    public int getFontSize() {
        return fontSize;
    }
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }
    
}
