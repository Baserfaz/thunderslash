package com.thunderslash.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import com.thunderslash.data.Animation;
import com.thunderslash.enumerations.GameState;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.utilities.Animator;

public class Renderer {

    private Handler handler;
    private GuiRenderer guirenderer;
    private Camera cam;
    private Animator animator;
    
    public Renderer() {
        this.handler = Game.instance.getHandler();
        this.guirenderer = Game.instance.getGuiRenderer();
        this.cam = Game.instance.getCamera();
        this.animator = Game.instance.getAnimator();
    }
    
    public void preRender(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        GameState gamestate = Game.instance.getGamestate();
        
        this.setRenderingHints(g2d);
        
        if(gamestate == GameState.INGAME) this.renderIngame(g);
        else if(gamestate== GameState.MAINMENU) this.renderMenu(g);
        else if(gamestate == GameState.LOADING) this.renderLoading(g);
        else if(gamestate == GameState.PAUSEMENU) this.renderPauseMenu(g);
        else if(gamestate == GameState.GAME_OVER) this.renderGameOver(g);
    }
    
    private void setRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
    
    private void renderGameOver(Graphics g) {
        this.guirenderer.renderGameOver(g);
    }
    
    private void renderPauseMenu(Graphics g) {
        this.guirenderer.renderPauseMenu(g);
    }
    
    private void renderLoading(Graphics g) {
        this.fillScreen(g, Color.white);
        this.guirenderer.renderLoading(g);        
    }
    
    private void renderMenu(Graphics g) {
        this.fillScreen(g, Color.white);
        this.guirenderer.renderMenu(g);
    }
    
    private void renderIngame(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        Rectangle r = cam.getCameraBounds();
        
        // set background
        this.fillScreen(g, Color.BLACK);
        
        // set zoom level
        g2d.scale(1, 1);

        // move the camera
        g2d.translate(-r.x, -r.y);
        
        handler.renderGameObjects(g2d);
        
        this.renderAnimations(g2d);
        handler.renderParticles(g2d);
        
        this.renderDebug(g2d);
        this.guirenderer.renderIngame(g2d);
        
    }
    
    private void renderAnimations(Graphics g) {
        if(this.animator.getCurrentAnims().isEmpty()) return;
        for(Animation anim : this.animator.getCurrentAnims()) {
            g.drawImage(anim.getCurrentFrame(), anim.getX(), anim.getY(), null);
        }
    }
    
    private void renderDebug(Graphics g) {
        
        Actor player = Game.instance.getActorManager().getPlayerInstance();
        Point playerCenterHitbox = player.getHitboxCenter();
        
        // render camera debug 
        if(Game.drawCameraRect) {
            Camera cam = Game.instance.getCamera();
            Rectangle camRect = cam.getCameraBounds();
            g.setColor(Game.cameraRectColor);
            g.drawRect(camRect.x, camRect.y, camRect.width, camRect.height);
        }
        
        if(Game.drawCurrentBlock) {
            Block block = player.getLastBlock();
            if(block != null) {
                g.setColor(Game.currentBlockColor);
                Rectangle r = block.getHitbox();
                g.drawRect(r.x, r.y, r.width, r.height);
            }
        }
        
        if(Game.drawAttackBoxes) {
            Rectangle box = player.getAttackBox();
            if(box != null) {
                g.setColor(Game.attackBoxDrawColor);
                g.drawRect(box.x, box.y, box.width, box.height);
                
                g.drawLine(playerCenterHitbox.x, playerCenterHitbox.y,
                        box.x + box.width / 2, box.y + box.height / 2);
                
            }
        }
        
        if(Game.drawActorCollisionPoints) {
            
            // draw the action area around player
            float dist = player.getCollisionDistance();
            g.setColor(Game.actorCollisionPointColor);
            g.drawOval((int)(playerCenterHitbox.x - dist / 2),
                    (int)(playerCenterHitbox.y - dist / 2),
                    (int)dist, (int)dist);
            
            // draw collision points
            for(Actor actor : Game.instance.getActorManager().getActorInstances()) {
                if(actor.getCollisionPoints() == null || actor.getCollisionPoints().isEmpty()) continue;
                for(Point p : actor.getCollisionPoints()) {
                    g.setColor(Game.actorCollisionPointColor);
                    g.drawOval(p.x, p.y, 2, 2);
                }
            }
        }
        
        if(Game.drawGameObjectRects) {
            g.setColor(Game.gameObjectRectColor);
            for(GameObject go : Game.instance.getHandler().getObjects()) {
                if(go.getIsVisible()) {
                    Rectangle hitbox = go.getHitbox();
                    g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
                    g.drawOval(go.getHitboxCenter().x, go.getHitboxCenter().y, 2, 2);
                }
            }
        }
    }
    
    private void fillScreen(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
    }

}
