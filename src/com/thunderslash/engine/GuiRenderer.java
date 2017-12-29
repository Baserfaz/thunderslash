package com.thunderslash.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.thunderslash.gameobjects.Actor;

public class GuiRenderer {

    public void render(Graphics g) {
        
        Camera cam = Game.instance.getCamera();
        if(cam == null) return;
        
        this.renderVersion(g, cam);
        this.renderDebugInfo(g);
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
            
            this.renderString(info, 20, 40, Game.debugInfoColor, 30f, g);
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
