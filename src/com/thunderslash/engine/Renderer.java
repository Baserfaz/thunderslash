package com.thunderslash.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.thunderslash.data.Room;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.utilities.SpriteCreator;

public class Renderer {

    public static void preRender(Graphics g) {

        // get references
        Handler handler = Game.instance.getHandler();
        GuiRenderer guirenderer = Game.instance.getGuiRenderer();
        Camera cam = Game.instance.getCamera();
        Rectangle r = cam.getCameraBounds();
        Graphics2D g2d = (Graphics2D) g;

        // set rendering hints
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);   

        // set background
        Renderer.fillScreen(g, new Color(51, 20, 82, 255));
        
        //guirenderer.render(g);
        
        // set zoom level
        g2d.scale(1, 1);

        // move the camera
        g.translate(-r.x, -r.y);
        
        // render everything
        Renderer.renderBackground(g);
        handler.render(g);
        Renderer.renderDebug(g);
        guirenderer.render(g);
        
    }
    
    private static void renderBackground(Graphics g) {
        
        Room room = Game.instance.getWorld().getCurrentRoom();
        SpriteCreator spriteCreator = Game.instance.getSpriteCreator();
        
        BufferedImage img = spriteCreator.CreateSprite(SpriteType.BACKGROUND_TILE_01);
        
        for(Block block : room.getBlocks()) {
            if(block.getBlocktype() != BlockType.NOT_ASSIGNED) {
                g.drawImage(img, block.getWorldPosition().x, block.getWorldPosition().y, null);
            }
        }
    }
    
    private static void renderDebug(Graphics g) {
        
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
                for(Point p : actor.getCollisionPoints()) {
                    g.setColor(Game.actorCollisionPointColor);
                    g.drawOval(p.x, p.y, 2, 2);
                }
            }
        }
        
    }
    
    public static void fillScreen(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
    }

}
