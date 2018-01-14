package com.thunderslash.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.Health;
import com.thunderslash.data.Power;
import com.thunderslash.enumerations.ButtonAction;
import com.thunderslash.enumerations.DepthLevel;
import com.thunderslash.enumerations.ElementAlign;
import com.thunderslash.enumerations.GameState;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Player;
import com.thunderslash.ui.Button;
import com.thunderslash.ui.GuiElement;
import com.thunderslash.ui.GuiElementManager;
import com.thunderslash.ui.GuiImage;
import com.thunderslash.utilities.RenderUtils;
import com.thunderslash.utilities.SpriteCreator;

public class GuiRenderer {

    // cache GUI sprites
    private BufferedImage heart;
    private BufferedImage tintedHeart;
    private BufferedImage powerSword;
    private BufferedImage tintedPowerSword;
    private BufferedImage playButtonSprite;
    private BufferedImage exitButtonSprite;
    private BufferedImage bgSprite;
    
    private GuiElementManager guiManager;
    
    private DecimalFormat df;
    
    public GuiRenderer() {
    
        this.guiManager = Game.instance.getGuiElementManager();
        SpriteCreator sc = Game.instance.getSpriteCreator();
        
        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        
        int tintAmount = 3;
        
        // cache GUI sprites
        this.heart = sc.CreateCustomSizeSprite(0, 6 * 32, 7, 6, Game.SPRITESIZEMULT);
        this.tintedHeart = RenderUtils.tint(this.heart, true, tintAmount);
        this.powerSword = sc.CreateCustomSizeSprite(8, 6 * 32, 10, 6, Game.SPRITESIZEMULT);
        this.tintedPowerSword = RenderUtils.tint(this.powerSword, true, tintAmount);
        
        this.playButtonSprite = sc.CreateCustomSizeSprite(0, 9 * 32 + 22, 64, 10, Game.SPRITESIZEMULT);
        this.exitButtonSprite = sc.CreateCustomSizeSprite(0, 9 * 32 + 11, 64, 10, Game.SPRITESIZEMULT);
        this.bgSprite = RenderUtils.tint(sc.CreateSprite(SpriteType.BACKGROUND_TILE_04), false, 1);
        
        this.createGuiElements();
    }

    // ---- CREATION ----
    private void createGuiElements() {
        this.createMainmenuElements();
        this.createLoadingElements();
        this.createPauseMenuElements();
    }
    
    private void createPauseMenuElements() {
        
        int width = 350;
        int height = 75;
        int margin = 10;
        int starty = 400;
        int xpos = (Game.CAMERA_WIDTH / 2) - width / 2;
        
        Button resumeButton = new Button(xpos, starty, width, height, "Resume", Color.black, Color.white, 40, ButtonAction.RESUME);
        Button exitButton = new Button(xpos, starty + height + margin, width, height, "Exit", Color.black, Color.white, 40, ButtonAction.EXIT);
        
        this.guiManager.addElementToPauseMenu(resumeButton);
        this.guiManager.addElementToPauseMenu(exitButton);
        
    }
    
    private void createLoadingElements() {
     
        // create background
        List<GuiElement> bg = new ArrayList<GuiElement>();
        
        int spriteSize = Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
        
        for(int y = 0; y < (Game.CAMERA_HEIGHT / spriteSize) + 3; y++) {
            for(int x = 0; x < Game.CAMERA_WIDTH / spriteSize; x++) {
                bg.add(new GuiImage(x * spriteSize, y * spriteSize - spriteSize, bgSprite, DepthLevel.BACKGROUND, false));
            }
        }
        
        this.guiManager.addMultipleElementsToLoading(bg);
        
    }
    
    private void createMainmenuElements() {
        int margin = 10;
        int starty = 400;
        int xpos = Game.CAMERA_WIDTH / 2 - 175;
        
        Button playButton = new Button(xpos, starty, this.playButtonSprite, ButtonAction.PLAY);
        Button exitButton = new Button(xpos, starty + this.exitButtonSprite.getHeight() + margin, this.exitButtonSprite, ButtonAction.EXIT);
        
        // create background
        List<GuiElement> bg = new ArrayList<GuiElement>();
        
        int spriteSize = Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
        
        for(int y = 0; y < (Game.CAMERA_HEIGHT / spriteSize) + 3; y++) {
            for(int x = 0; x < Game.CAMERA_WIDTH / spriteSize; x++) {
                bg.add(new GuiImage(x * spriteSize, y * spriteSize - spriteSize, bgSprite, DepthLevel.BACKGROUND, false));
            }
        }
        
        // add elements to list
        this.guiManager.addElementToMainmenu(playButton);
        this.guiManager.addElementToMainmenu(exitButton);
        this.guiManager.addMultipleElementsToMainmenu(bg);
    }
    
    // ----- RENDERING -----
    public void renderLoading(Graphics g) {
        this.guiManager.render(g, GameState.LOADING);
        this.renderString("Loading", Game.CAMERA_WIDTH - 20, Game.CAMERA_HEIGHT, Color.white, 50f, ElementAlign.LEFT, g);
    }
    
    public void renderMenu(Graphics g) {
        int centerx = Game.CAMERA_WIDTH / 2;
        
        this.guiManager.render(g, GameState.MAINMENU);
        this.renderString(Game.TITLE, centerx, 200, Color.white, 60, ElementAlign.CENTER, g);
        this.renderString(Game.VERSION, centerx, 700, Color.white, 30, ElementAlign.CENTER, g);
    }
    
    public void renderPauseMenu(Graphics g) {
        this.renderString("Paused", Game.CAMERA_WIDTH / 2, 100, Color.white, 60f, ElementAlign.CENTER, g);
        this.guiManager.render(g, GameState.PAUSEMENU);
    }
    
    public void renderIngame(Graphics g) {
        
        Player player = Game.instance.getActorManager().getPlayerInstance();
        if(player == null) return;
        
        // vars
        Health hp = player.getHP();
        Power power = player.getPower();
        int x = 20;
        
        // render hp and power
        this.renderHUDSprite(g, x, 20, 10, 
                hp.getMaxHP(), hp.getCurrentHP(), this.heart, this.tintedHeart);
        this.renderHUDSprite(g, x, 60, 10,
                power.getMaxPower(), power.getCurrentPower(), this.powerSword, this.tintedPowerSword);
        
        // render score
        this.renderString(Game.instance.getSession().getScore() + "",
                Game.CAMERA_WIDTH - 20, 70, Color.white, 50f, ElementAlign.LEFT, g);
        
        this.guiManager.render(g, GameState.INGAME);
        
        // render debugging information
        this.renderDebugInfo(g);
    }
    
    private void renderDebugInfo(Graphics g) {
        if(Game.drawDebugInfo) {
            
            Actor player = Game.instance.getActorManager().getPlayerInstance();
            if(player == null) return;
            
            // for some freaking reason we need to add 
            // our offset position to the camera's position.
            Camera cam = Game.instance.getCamera();
            Rectangle r = cam.getCameraBounds();
            
            int x = r.x + 20;
            int y = r.y + 150;
            
            // create info strings
            String info = "---- SYSTEM ----\n";
            info += "frame time: " + this.df.format(Game.instance.getTimeBetweenFrames()) + "\n"; 
            info += "fps: " + Game.FPS + "\n";
            info += "version: " + Game.VERSION + "\n";
            info += "---- PLAYER ----\n";
            info += "pos: " + player.getWorldPosition().toString() + "\n";
            info += "input: " + player.getDirection().toString()+ "\n";
            info += "acceleration: " + player.getAcceleration().toString() + "\n";
            info += "velocity: " + player.getVelocity().toString() + "\n";
            info += "isGrounded: " + player.isGrounded() + "\n";
            info += "state: " +  player.getActorState().toString() + "\n";
            
            this.renderString(info, x, y, Game.debugInfoColor, 24f, ElementAlign.RIGHT, g);
        }
    }
    
    // --------------- GENERAL ----------------
    private void renderHUDSprite(Graphics g,
            int x, int y, int margin, int maxAmount, int currentAmount,
            BufferedImage img, BufferedImage img2) {
            
        Rectangle r = Game.instance.getCamera().getCameraBounds();
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

    public void renderString(String msg, int x, int y, Color color, float size, ElementAlign align, Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        Font font = Game.instance.getCustomFont().deriveFont(Font.PLAIN, size);
        g2d.setColor(color);
        g2d.setFont(font);
        
        int offsetx = 0;
        int offsety = -g.getFontMetrics().getHeight();
        
        switch(align) {
        case CENTER:
            offsetx = -g.getFontMetrics().stringWidth(msg) / 2;
            break;
        case LEFT:
            offsetx = -g.getFontMetrics().stringWidth(msg);
            break;
        case RIGHT:
            // do nothing
            break;
        default:
            System.out.println("GuiRenderer::renderString: ElementAlign not supported!");
            break;
        }
        
        int xx =  x + offsetx;
        int yy = y + offsety;
        
        for (String line : msg.split("\n")) {
            g2d.drawString(line, xx, yy);
            yy += g.getFontMetrics().getHeight() + Game.TEXT_LINEHEIGHT;
        }
    }
}
