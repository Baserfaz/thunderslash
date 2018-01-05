package com.thunderslash.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.thunderslash.data.Animation;
import com.thunderslash.data.Room;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.GameState;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.utilities.Animator;
import com.thunderslash.utilities.SpriteCreator;

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
        
        this.setRenderingHints(g2d);
        
        if(Game.instance.getGamestate() == GameState.INGAME) this.renderIngame(g2d);
        else if(Game.instance.getGamestate() == GameState.MAINMENU) this.renderMenu(g2d);
        else if(Game.instance.getGamestate() == GameState.LOADING) this.renderLoading(g2d);
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
    
    private void renderLoading(Graphics2D g) {
        this.fillScreen(g, Color.black);
        this.guirenderer.renderLoading(g);        
    }
    
    private void renderMenu(Graphics2D g) {
        this.fillScreen(g, Color.black);
        this.guirenderer.renderMenu(g);
    }
    
    private void renderIngame(Graphics2D g) {
        
        Rectangle r = cam.getCameraBounds();
        
        // set background
        this.fillScreen(g, new Color(51, 20, 82, 255));
        
        // set zoom level
        g.scale(1, 1);

        // move the camera
        g.translate(-r.x, -r.y);
        
        this.renderBackground(g);
        handler.renderGameObjects(g);
        
        this.renderAnimations(g);
        
        this.renderDebug(g);
        this.guirenderer.renderIngameGui(g);
        
    }
    
    private void renderAnimations(Graphics g) {
        if(this.animator.getCurrentAnims().isEmpty()) return;
        for(Animation anim : this.animator.getCurrentAnims()) {
            g.drawImage(anim.getCurrentFrame(), anim.getX(), anim.getY(), null);
        }
    }
    
    private void renderBackground(Graphics g) {
        
        Room room = Game.instance.getWorld().getCurrentRoom();
        SpriteCreator spriteCreator = Game.instance.getSpriteCreator();
        
        BufferedImage img = spriteCreator.CreateSprite(SpriteType.BACKGROUND_TILE_01);
        
        for(Block block : room.getBlocks()) {
            if(block.getBlocktype() != BlockType.NOT_ASSIGNED) {
                g.drawImage(img, block.getWorldPosition().x, block.getWorldPosition().y, null);
            }
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
        
        // action area
        if(Game.drawActorCollisionPoints) {
            float dist = player.getCollisionDistance();
            g.setColor(Game.actorCollisionPointColor);
            g.drawOval((int)(playerCenterHitbox.x - dist / 2),
                    (int)(playerCenterHitbox.y - dist / 2),
                    (int)dist, (int)dist);
        }
        
        if(Game.drawActorCollisionPoints) {
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
