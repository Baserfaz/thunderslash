package com.thunderslash.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Player;

public class GuiRenderer {

    // cache GUI sprites
    private BufferedImage heart;

    public GuiRenderer() {
        
        // cache GUI sprites
        this.heart = Game.instance.getSpriteCreator().CreateCustomSizeSprite(0, 6 * 32, 7, 6);
        
    }
    
    public void render(Graphics g) {
        
        Camera cam = Game.instance.getCamera();
        if(cam == null) return;
        
        this.renderHP(g, cam);
        
        this.renderVersion(g, cam);
        this.renderDebugInfo(g);
    }   
    
    private void renderHP(Graphics g, Camera cam) {
        
        Player player = Game.instance.getActorManager().getPlayerInstance();
        if(player != null) {
            
            Rectangle r = cam.getCameraBounds();
            int startx = r.x + 20;
            int starty = r.y + 20;
            
            int x = startx;
            int y = starty;
            
            int margin = 10;
            
            for(int i = 0; i < player.getHP().getCurrentHP(); i++) {
                g.drawImage(this.heart, x, y, null);
                x += margin + this.heart.getWidth();
            }
        }
        
    }

    private void renderDebugInfo(Graphics g) {
        if(Game.drawDebugInfo) {
            
            Actor player = Game.instance.getActorManager().getPlayerInstance();
            if(player == null) return;
            
            // create info strings
            String info = "pos: " + player.getWorldPosition().toString() + "\n";
            info += "input: " + player.getDirection().toString()+ "\n";
            info += "acceleration: " + player.getAcceleration().toString() + "\n";
            info += "velocity: " + player.getVelocity().toString() + "\n";
            info += "isGrounded: " + player.isGrounded() + "\n";
            info += "state: " +  player.getActorState().toString() + "\n";
            
            this.renderString(info, 20, 100, Game.debugInfoColor, 30f, g);
        }
    }
    
    private void renderVersion(Graphics g, Camera cam) {
        Rectangle r = cam.getCameraBounds();
        this.renderString("v0.1", r.width - 50, 20, Color.white, 20f, g);
    }
    
    public void renderString(String msg, int x, int y, Color color, float size, Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        Camera cam = Game.instance.getCamera();
        Rectangle r = cam.getCameraBounds();
        
        Font font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, size);
        g2d.setColor(color);
        g2d.setFont(font);
        
        int xx = r.x + x;
        int yy = r.y + y;
        
        for (String line : msg.split("\n")) {
            g2d.drawString(line, xx, yy);
            yy += g.getFontMetrics().getHeight() + Game.LINEHEIGHT;
        }
    }

}
