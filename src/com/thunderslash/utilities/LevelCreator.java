package com.thunderslash.utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.thunderslash.data.LevelData;
import com.thunderslash.data.NeighborData;
import com.thunderslash.data.World;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.gameobjects.Block;

public class LevelCreator {
    
    public static List<BufferedImage> createBackground(int x, int y) {
        List<BufferedImage> imgs = new ArrayList<BufferedImage>();
        
        SpriteCreator spriteCreator = Game.instance.getSpriteCreator();
        
        BufferedImage bgwall01 = spriteCreator.CreateSprite(SpriteType.BACKGROUND_TILE_01);
        
        for(int i = 0; i < x * y; i++) {
            imgs.add(bgwall01);
        }
        
        return imgs;
    }
    
    public static LevelData createLevel(String path) {
        
        List<Block> blocks = new ArrayList<Block>();
        
        LevelData data = LevelCreator.importLevelData(path);
        int levelWidth = data.getWidth();
        int levelHeight = data.getHeight();
        int[] pixels = data.getPixels();
         
        for(int i = 0; i < pixels.length; i++) {
        
            // get the current color in 32-bit integer value
            int current = pixels[i];
            
            // break it down 
            int blue = current & 0xff;
            int green = (current & 0xff00) >> 8;
            int red = (current & 0xff0000) >> 16;
            int alpha = (current & 0xff000000) >>> 24;
            
            // vars
            Coordinate pos = new Coordinate(0, 0);
            Coordinate gridPos = new Coordinate(0, 0);
            boolean isEnabled = true;
            boolean isVisible = true;
            BlockType blockType = BlockType.OTHER;
            SpriteType spriteType = SpriteType.NONE;
            
            // flag
            boolean found = false;
            
            // wall = black
            if(red == 0 && green == 0 && blue == 0 && alpha == 255) {
               
                found = true;
                blockType = BlockType.SOLID;
                spriteType = SpriteType.ERROR;
             
            // gray = platform
            } else if(red == 100 && green == 100 && blue == 100 && alpha == 255) { 
                
                found = true;
                blockType = BlockType.PLATFORM;
                
            // green = player spawn
            } else if(red == 0 && green == 255 && blue == 0 && alpha == 255) {
                
                // no sprite
                found = true;
                isEnabled = true;
                isVisible = false;
                blockType = BlockType.PLAYER_SPAWN;
            
            // blue = water
            } else if(red == 0 && green == 0 && blue == 255 && alpha == 255) {
                
                found = true;
                blockType = BlockType.WATER;
                spriteType = SpriteType.WATER;
                
            // white = play area
            } else if(red == 255 && green == 255 && blue == 255 && alpha == 255) {
                
                found = true;
                blockType = BlockType.PLAY_AREA;
                spriteType = SpriteType.NONE;
                isEnabled = true;
                isVisible = true;
                
            }
            
            // only create blocks
            // when we have found 
            // "color coded" pixel.
            if(found) {
                
                // calculate world position
                int y = i / levelWidth;
                int x = i % levelWidth;
                
                pos.x = x * Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
                pos.y = y * Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
                
                gridPos.x = x;
                gridPos.y = y;
                
                // sprite types are calculated after blocks are created.
                Block block = new Block(pos, gridPos, blockType, spriteType);
                
                // set block settings
                block.setIsEnabled(isEnabled);
                block.setIsVisible(isVisible);
                
                blocks.add(block);
                
            }
        }
        
        return new LevelData(levelWidth, levelHeight, blocks);
    }

    public static List<Block> calculateSprites(List<Block> blocks) {
        
        List<Block> calcBlocks = new ArrayList<Block>(blocks);
        World world = Game.instance.getWorld();
        SpriteCreator spriteCreator = Game.instance.getSpriteCreator();
        
        // get neighbors and decide sprite
        for(Block block : calcBlocks) {
         
            if(block.getIsEnabled() == false) continue;
            
            if(block.getBlocktype() == BlockType.PLATFORM) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.PLATFORM));
                block.recalculateBoundingBox();
                continue;
            }
            
            // only support solid blocks for now.
            if(block.getBlocktype() != BlockType.SOLID) continue;
            
            // -------------------------------------------------
            
            NeighborData data = world.getNeighboringPlayArea(block);
            
            boolean n = data.getNeighbors().containsKey(Direction.NORTH);
            boolean s = data.getNeighbors().containsKey(Direction.SOUTH);
            boolean w  = data.getNeighbors().containsKey(Direction.WEST);
            boolean e  = data.getNeighbors().containsKey(Direction.EAST);

            boolean ne  = data.getNeighbors().containsKey(Direction.NORTH_EAST);
            boolean nw  = data.getNeighbors().containsKey(Direction.NORTH_WEST);
            boolean se  = data.getNeighbors().containsKey(Direction.SOUTH_EAST);
            boolean sw  = data.getNeighbors().containsKey(Direction.SOUTH_WEST);
            
            if(n && s && w && e) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_SINGLE));
            } else if(w && n && e) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_THREE_SIDED));
            } else if(n && e && s) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_THREE_SIDED), 1));
            } else if(w && s && e) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_THREE_SIDED), 2));
            } else if(w && n && s) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_THREE_SIDED), 3));
            } else if(n && e) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_90_DEGREE), 1));
            } else if(n && w) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_90_DEGREE));
            } else if(s && e) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_90_DEGREE), 2));
            } else if(s && w) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_90_DEGREE), 3));
            } else if(n && s) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_TWO_SIDED), 1));
            } else if(w && e) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_TWO_SIDED));  
            } else if(n) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED));
            } else if(s) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED), 2));
            } else if(w) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED), 3));
            } else if(e) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED), 1));
            } else if(nw && ne) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_TWO_CORNER), 3));
            } else if(ne && se) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_TWO_CORNER));
            } else if(se && sw) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_TWO_CORNER), 1));
            } else if(sw && nw) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_TWO_CORNER), 2));
            } else if(nw) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_CORNER), 2));
            } else if(ne) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_CORNER), 3));
            } else if(se) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_CORNER));
            } else if(sw) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_CORNER), 1));
            }
            
            // we changed sprite, so
            // we want to recalculate bounding box
            // which is calculated using sprite size.
            block.recalculateBoundingBox();
            
        }
        
        return calcBlocks;
    }
    
    private static LevelData importLevelData(String path) {
        
        BufferedImage image = null;
        
        System.out.println("Loading level data from path: " + Game.LEVELFOLDER + path);

        // get the sprite sheet
        try { image = ImageIO.read(LevelCreator.class.getResourceAsStream(Game.LEVELFOLDER + path)); }
        catch (IOException e) { e.printStackTrace(); }

        if(image == null) {
            System.out.println("LevelCreator: No image found!");
            return null;
        } else {
            System.out.println("Level data \'" + path + "\' loaded succesfully!");
        }
        
        LevelData data = new LevelData(image.getWidth(), image.getHeight(),
                image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth()));
        
        return data;
    }
    
}
