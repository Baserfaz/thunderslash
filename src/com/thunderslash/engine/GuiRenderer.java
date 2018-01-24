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
import com.thunderslash.enumerations.InteractAction;
import com.thunderslash.enumerations.DepthLevel;
import com.thunderslash.enumerations.ElementAlign;
import com.thunderslash.enumerations.GameState;
import com.thunderslash.enumerations.GuiAnimationType;
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
    private BufferedImage logo;
    
    private GuiElementManager guiElementManager;
    
    private DecimalFormat df;
    private SpriteCreator sc;
    
    public GuiRenderer() {
    
        this.guiElementManager = Game.instance.getGuiElementManager();
        this.sc = Game.instance.getSpriteCreator();
        
        this.df = new DecimalFormat();
        this.df.setMaximumFractionDigits(2);
        
        int tintAmount = 3;
        
        // cache GUI sprites
        this.heart = sc.CreateCustomSizeSprite(0, 6 * 32, 7, 6, Game.SPRITESIZEMULT);
        this.tintedHeart = RenderUtils.tint(this.heart, true, tintAmount);
        this.powerSword = sc.CreateCustomSizeSprite(8, 6 * 32, 10, 6, Game.SPRITESIZEMULT);
        this.tintedPowerSword = RenderUtils.tint(this.powerSword, true, tintAmount);
        
        this.playButtonSprite = sc.CreateCustomSizeSprite(0, 9 * 32 + 22, 64, 10, Game.SPRITESIZEMULT);
        this.exitButtonSprite = sc.CreateCustomSizeSprite(0, 9 * 32 + 11, 64, 10, Game.SPRITESIZEMULT);
        this.bgSprite = RenderUtils.tint(sc.CreateSprite(SpriteType.BACKGROUND_TILE_04), false, 1);
        
        // load logo file
        this.logo = sc.createImageFromFile(Game.LOGOFILENAME, 2);
        
        this.createGuiElements();
    }

    // ---- CREATION ----
    private void createGuiElements() {
        this.createMainmenuElements();
        this.createLoadingElements();
        this.createPauseMenuElements();
        this.createGameOverElements();
    }
    
    private void createPauseMenuElements() {
        
        int width = 350;
        int height = 75;
        int margin = 10;
        int starty = 400;
        int xpos = (Game.CAMERA_WIDTH / 2) - width / 2;
        
        Button resumeButton = new Button(xpos, starty, width, height,
                "Resume", Color.black, Color.white, 40, InteractAction.RESUME, InteractAction.NONE);
        Button exitButton = new Button(xpos, starty + height + margin, width, height,
                "Exit", Color.black, Color.white, 40, InteractAction.EXIT_TO_OS, InteractAction.NONE);
        
        this.guiElementManager.addElementToPauseMenu(resumeButton);
        this.guiElementManager.addElementToPauseMenu(exitButton);
        
    }
    
    private void createGameOverElements() {
        
        int width = 350;
        int height = 75;
        int starty = 400;
        int xpos = (Game.CAMERA_WIDTH / 2) - width / 2;
        
        Button exitButton = new Button(xpos, starty, width, height,
                "Exit", Color.black, Color.white, 40, InteractAction.EXIT_TO_MENU, InteractAction.NONE);
        
        this.guiElementManager.addElementToGameOver(exitButton);
    }
    
    private void createLoadingElements() {
        this.guiElementManager.addMultipleElementsToLoading(this.createScrollingBackground(GuiAnimationType.SCROLL_DOWN));
    }
    
    private void createMainmenuElements() {
        
        int margin = 10;
        int starty = 400;
        int xpos = Game.CAMERA_WIDTH / 2 - 175;
        
        Button playButton = new Button(xpos, starty, this.playButtonSprite, InteractAction.PLAY, InteractAction.NONE);
        Button exitButton = new Button(xpos, starty + this.exitButtonSprite.getHeight() + margin,
                this.exitButtonSprite, InteractAction.EXIT_TO_OS, InteractAction.NONE);
        
        GuiImage logo = new GuiImage(Game.CAMERA_WIDTH / 2 - this.logo.getWidth() / 2, 50,
                this.logo, true, DepthLevel.FOREGROUND, GuiAnimationType.NONE,
                InteractAction.NONE, InteractAction.NONE);
        
        // add elements to list
        this.guiElementManager.addElementToMainmenu(playButton);
        this.guiElementManager.addElementToMainmenu(exitButton);
        this.guiElementManager.addElementToMainmenu(logo);        
        this.guiElementManager.addMultipleElementsToMainmenu(this.createScrollingBackground(GuiAnimationType.SCROLL_DOWN));
    }
    
    private List<GuiElement> createScrollingBackground(GuiAnimationType animType) {
        List<GuiElement> elems = new ArrayList<GuiElement>();
        
        // calculate needed tile imgs to fill the screen.
        int xamount = Game.CAMERA_WIDTH / bgSprite.getWidth();
        int yamount = Game.CAMERA_HEIGHT / bgSprite.getHeight() + 3;
                
        BufferedImage tiledImg = sc.createTiledSprite(this.bgSprite, xamount, yamount);
        
        // minimum of two images are needed to create a scroll effect.
        GuiImage gimg = new GuiImage(0, 0, tiledImg, false,
                DepthLevel.BACKGROUND, animType, InteractAction.NONE, InteractAction.NONE);
        GuiImage gimg2 = new GuiImage(0, -tiledImg.getHeight(), tiledImg, false,
                DepthLevel.BACKGROUND, animType, InteractAction.NONE, InteractAction.NONE);    
        
        elems.add(gimg);
        elems.add(gimg2);
        
        return elems;
    }
    
    // ----- RENDERING -----
    public void renderLoading(Graphics g) {
        this.guiElementManager.render(g, GameState.LOADING);
        this.renderString("Loading", Game.CAMERA_WIDTH - 20, Game.CAMERA_HEIGHT, Color.white, 50f, ElementAlign.LEFT, g);
    }
    
    public void renderMenu(Graphics g) {
        int centerx = Game.CAMERA_WIDTH / 2;
        
        this.guiElementManager.render(g, GameState.MAINMENU);
        this.renderString(Game.VERSION, centerx, 700, Color.white, 30, ElementAlign.CENTER, g);
    }
    
    public void renderGameOver(Graphics g) {
        int centerx = Game.CAMERA_WIDTH / 2;
        this.renderString("Game over", centerx, 200, Color.white, 60f, ElementAlign.CENTER, g);
        this.guiElementManager.render(g, GameState.GAME_OVER);
    }
    
    public void renderPauseMenu(Graphics g) {
        this.renderString("Paused", Game.CAMERA_WIDTH / 2, 100, Color.white, 60f, ElementAlign.CENTER, g);
        this.guiElementManager.render(g, GameState.PAUSEMENU);
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
        
        Camera cam = Game.instance.getCamera();
        Rectangle r = cam.getCameraBounds();
        
        // render score
        this.renderString(Game.instance.getSession().getScore() + "",
                r.x + r.width - 20, r.y + 70, Color.white, 50f, ElementAlign.LEFT, g);
        
        this.guiElementManager.render(g, GameState.INGAME);
        
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
            
            double playerx = player.getWorldPosition().getX();
            double playery = player.getWorldPosition().getY();
            Rectangle camRect = Game.instance.getCamera().getCameraBounds();
            
            // create info strings
            String info = "---- SYSTEM ----\n";
            info += "frame time: " + this.df.format(Game.instance.getTimeBetweenFrames()) + "\n"; 
            info += "fps: " + Game.FPS + "\n";
            info += "version: " + Game.VERSION + "\n";
            info += "---- PLAYER ----\n";
            info += "pos: " + playerx + ", " + playery + "\n";
            info += "cam pos: " + camRect.x + ", " + camRect.y + "\n";
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
