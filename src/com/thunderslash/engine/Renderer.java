package com.thunderslash.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.thunderslash.data.Room;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;

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
        
        guirenderer.render(g);
        
        // set zoom level
        g2d.scale(1, 1);

        // move the camera
        g.translate(r.x, r.y);
        
        // render everything
        //Renderer.renderBackground(g);
        handler.render(g);
        Renderer.renderDebug(g);
        
    }
    
    private static void renderBackground(Graphics g) {
        
        Room room = Game.instance.getWorld().getCurrentRoom();
        
        int increment = Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
        int i = 0;
        
        int x = 0;
        int y = 0;
        
        for(BufferedImage img : room.getBackGroundTiles()) {
            g.drawImage(img, x, y, null);
            
            x += increment;
            i += 1;
            
            if(i != 0 && i % room.getWidth() == 0) {
                y += increment;
                x = 0;
            }
        }
    }
    
    private static void renderDebug(Graphics g) {
        
        // render camera debug 
        if(Game.drawCameraRect) {
            Camera cam = Game.instance.getCamera();
            Rectangle camRect = cam.getCameraBounds();
            g.setColor(Game.cameraRectColor);
            g.drawRect(camRect.x, camRect.y, camRect.width, camRect.height);
        }
        
        if(Game.drawActorCollisionPoints) {
            Actor player = Game.instance.getActorManager().getPlayerInstance();
            if(player != null) {
                for(Point p : player.getCollisionPoints()) {
                 
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
