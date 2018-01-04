package com.thunderslash.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.thunderslash.data.Health;
import com.thunderslash.data.Power;
import com.thunderslash.enumerations.ButtonAction;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Player;
import com.thunderslash.ui.Button;
import com.thunderslash.ui.GuiElementManager;
import com.thunderslash.utilities.RenderUtils;
import com.thunderslash.utilities.SpriteCreator;

public class GuiRenderer {

    // cache GUI sprites
    private BufferedImage heart;
    private BufferedImage tintedHeart;
    
    private BufferedImage powerSword;
    private BufferedImage tintedPowerSword;
    
    private GuiElementManager guiManager;
    
    public GuiRenderer() {
    
        this.guiManager = Game.instance.getGuiElementManager();
        SpriteCreator sc = Game.instance.getSpriteCreator();
        
        int tintAmount = 3;
        
        // cache GUI sprites
        this.heart = sc.CreateCustomSizeSprite(0, 6 * 32, 7, 6);
        this.tintedHeart = RenderUtils.tint(this.heart, true, tintAmount); 
        
        this.powerSword = sc.CreateCustomSizeSprite(8, 6 * 32, 10, 6);
        this.tintedPowerSword = RenderUtils.tint(this.powerSword, true, tintAmount);
        
        this.createGuiElements();
    }
    
    public void renderMenu(Graphics g) {
        this.renderMainmenuTitle(g);
        this.guiManager.render(g);
    }
    
    public void renderIngameGui(Graphics g) {
        
        Camera cam = Game.instance.getCamera();
        if(cam == null) return;
        
        Player player = Game.instance.getActorManager().getPlayerInstance();
        if(player == null) return;
        
        // vars
        Health hp = player.getHP();
        Power power = player.getPower();  
        int x = 20;
        
        // render hp and power
        this.renderImg(g, cam, x, 20, 10, 
                hp.getMaxHP(), hp.getCurrentHP(), this.heart, this.tintedHeart);
        this.renderImg(g, cam, x, 60, 10,
                power.getMaxPower(), power.getCurrentPower(), this.powerSword, this.tintedPowerSword);
        
        // render debugging information
        this.renderVersion(g, cam);
        this.renderDebugInfo(g);
    }

    private void createGuiElements() {
        
        int width = 350;
        int height = 75;
        int margin = 10;
        
        int starty = 400;
        int xpos = Game.CAMERA_WIDTH / 2 - width / 2;
        
        // create menu gui-elements
        Button playButton = new Button(xpos, starty, width, height, "Play",
                Color.black, Color.white, 40, ButtonAction.PLAY);
        
        Button exitButton = new Button(xpos, starty + height + margin, width, height,
                "Exit", Color.black, Color.white, 40, ButtonAction.EXIT);
        
        // add elements to list
        this.guiManager.addElement(playButton);
        this.guiManager.addElement(exitButton);
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
            
            this.renderString(info, 20, 150, Game.debugInfoColor, 30f, g);
        }
    }
    
    private void renderVersion(Graphics g, Camera cam) {
        Rectangle r = cam.getCameraBounds();
        this.renderString("v0.1", r.width - 50, 20, Color.white, 20f, g);
    }
    
    private void renderMainmenuTitle(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        int titleFontSize = 50;
        
        Font font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, titleFontSize);
        g2d.setFont(font);
        
        int width = g.getFontMetrics().stringWidth(Game.TITLE);
        
        this.renderString(Game.TITLE, 
                Game.CAMERA_WIDTH / 2 - width / 2,
                200,
                Color.white, titleFontSize, g);
        
    }
    
    // -----------------------------------
    
    private void renderImg(Graphics g, Camera cam,
            int x, int y, int margin, int maxAmount, int currentAmount,
            BufferedImage img, BufferedImage img2) {
            
        Rectangle r = cam.getCameraBounds();
        int startx = r.x + x;
        int starty = r.y + y;
        
        int xx = startx;
        int yy = starty;
        
        for(int i = 0; i < maxAmount; i++) {
            if(i < currentAmount)g.drawImage(img, xx, yy, null);
            else g.drawImage(img2, xx, yy, null);
            xx += margin + img.getWidth();
        }
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
