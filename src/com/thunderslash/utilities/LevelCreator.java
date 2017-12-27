package com.thunderslash.utilities;

import java.awt.Color;
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
    
    public static List<Block> createLevel(String path) {
        
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
            //int alpha = (current & 0xff000000) >>> 24;
            
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
            if(red == 0 && green == 0 && blue == 0) {
               
                found = true;
                blockType = BlockType.SOLID;
             
            // gray = platform
            } else if(red == 100 && green == 100 && blue == 100) { 
                
                found = true;
                blockType = BlockType.PLATFORM;
                
            // green = player spawn
            } else if(red == 0 && green == 255 && blue == 0) {
                
                // no sprite
                found = true;
                isEnabled = false;
                isVisible = false;
                blockType = BlockType.PLAYER_SPAWN;
            
            // blue = water
            } else if(red == 0 && green == 0 && blue == 255) {
                
                found = true;
                blockType = BlockType.WATER;
                spriteType = SpriteType.WATER;
                
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
        
        return blocks;
    }

    public static List<Block> calculateSprites(List<Block> blocks) {
        List<Block> calcBlocks = new ArrayList<Block>(blocks);
        World world = Game.instance.getWorld();
        SpriteCreator spriteCreator = Game.instance.getSpriteCreator();
        
        // get neighbors and decide sprite
        for(Block block : calcBlocks) {
         
            if(block.getIsEnabled() == false) continue;
            
            if(block.getBlocktype() == BlockType.WATER) continue;
            if(block.getBlocktype() == BlockType.PLATFORM) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.PLATFORM));
                continue;
            }
            
            // -------------------------------------------------
            
            NeighborData data = world.getNeighbors(block);
            
            boolean hasNorth = data.getNeighbors().containsKey(Direction.NORTH);
            boolean hasSouth = data.getNeighbors().containsKey(Direction.SOUTH);
            boolean hasWest  = data.getNeighbors().containsKey(Direction.WEST);
            boolean hasEast  = data.getNeighbors().containsKey(Direction.EAST);
            
//            Block northNeighbor = null;
//            Block southNeighbor = null;
//            Block westNeighbor  = null;
//            Block eastNeighbor  = null;
//            
//            if(hasNorth) northNeighbor = data.getNeighbors().get(Direction.NORTH);
//            if(hasSouth) southNeighbor = data.getNeighbors().get(Direction.SOUTH);
//            if(hasWest)  westNeighbor  = data.getNeighbors().get(Direction.WEST);
//            if(hasEast)  eastNeighbor  = data.getNeighbors().get(Direction.EAST);
            
            if(hasNorth && hasSouth && hasWest && hasEast) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_C));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasWest && hasSouth && hasEast) {
            
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_N));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
            
            } else if(hasWest && hasNorth && hasEast) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_S));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasWest && hasNorth && hasSouth) { 
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_E));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasEast && hasNorth && hasSouth) { 
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_W));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasNorth && hasWest) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_SE));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasNorth && hasEast) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_SW));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasNorth && hasSouth) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_VERTICAL));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasEast && hasWest) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_HORIZONTAL));
                        break;
                    default:
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasSouth && hasWest) {
            
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_NE));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasSouth && hasEast) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_NW));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasNorth) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_BOTTOM));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasSouth) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_TOP));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasWest) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_RIGHT));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(hasEast) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_LEFT));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
            } else if(!hasNorth && !hasSouth && !hasWest && !hasEast) {
                
                switch(block.getBlocktype()) {
                    case SOLID:
                        block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_SINGLE));
                        break;
                    default: 
                        System.out.println("LevelCreator.calculateSprites: "
                                + "Unsupported blocktype " + block.getBlocktype());
                        break;
                }
                
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
