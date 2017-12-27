package com.thunderslash.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Item;

public class GuiRenderer {

    public void render(Graphics g) {
        
        Camera cam = Game.instance.getCamera();
        if(cam == null) return;
        
        this.renderVersion(g, cam);
        this.renderDebugInfo(g, cam);
    }   

    private void renderDebugInfo(Graphics g, Camera cam) {
        if(Game.drawDebugInfo) {
            
            Actor player = Game.instance.getActorManager().getPlayerInstance();
            if(player == null) return;
            
            // create info strings
            String info = "pos: " + player.getWorldPosition().toString() + "\n";
            info += "input: " + player.getDirection().toString()+ "\n";
            info += "acceleration: " + player.getAcceleration().toString() + "\n";
            info += "velocity: " + player.getVelocity().toString() + "\n";
            info += "isGrounded: " + player.isGrounded() + "\n";
            
            this.renderString(info, 20, 35, Game.debugInfoColor, 30f, g);
        }
    }
    
    private void renderVersion(Graphics g, Camera cam) {
        Rectangle r = cam.getCameraBounds();
        this.renderString("Version 0.1", r.width - 90, r.height - 10, Color.black, 20f, g);
    }
    
    public void renderHoverText(Graphics g) {
        
        Item hoverItem = Game.instance.getDynamicGuiManager().getMouseHoverItem();
        if(hoverItem == null) return;
        Point mousePos = Game.instance.getMousePos();
        
        int x = mousePos.x + 40;
        int y = mousePos.y + 40;
        
        this.renderString(hoverItem.getInfo(), x, y, Color.black, 30f, g);
    }
    
    public void renderString(String msg, int x, int y, Color color, float size, Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        Camera cam = Game.instance.getCamera();
        if(cam == null) return;
        
        Rectangle camr = cam.getCameraBounds();
        
        // calculate string position 
        // using camera position
        int xx = camr.x + x;
        int yy = camr.y + y;
        
        Font font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, size);
        g2d.setColor(color);
        g2d.setFont(font);
        
        for (String line : msg.split("\n")) {
            g2d.drawString(line, xx, yy);
            yy += g.getFontMetrics().getHeight() + Game.LINEHEIGHT;
        }
    }

}
