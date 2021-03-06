package com.thunderslash.utilities;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.thunderslash.data.LevelData;
import com.thunderslash.data.NeighborData;
import com.thunderslash.data.Room;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.EnemyType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.gameobjects.Chest;
import com.thunderslash.gameobjects.Crystal;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.gameobjects.Torch;
import com.thunderslash.gameobjects.Trap;
import com.thunderslash.gameobjects.VanityObject;

public class LevelCreator {
    
    public static LevelData createLevel(String path) {
        
        List<Block> blocks = new ArrayList<Block>();
        List<VanityObject> vanityObjects = new ArrayList<VanityObject>();
        List<GameObject> items = new ArrayList<GameObject>();
        List<Actor> actors = new ArrayList<Actor>();
        
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
            Point pos = new Point(0, 0);
            Point gridPos = new Point(0, 0);
            BlockType blockType = BlockType.NOT_ASSIGNED;
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
                spriteType = SpriteType.PLATFORM;
                
            // green = player spawn
            } else if(red == 0 && green == 255 && blue == 0 && alpha == 255) {
                
                // no sprite
                found = true;
                blockType = BlockType.PLAYER_SPAWN;
                spriteType = SpriteType.GATE_CLOSED;
            
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
                
            // purple = exit
            } else if(red == 233 && green == 0 && blue == 187 && alpha == 255) {
                
                found = true;
                blockType = BlockType.EXIT;
                spriteType = SpriteType.GATE_OPEN;
                
            // teal = trap
            } else if(red == 16 && green == 221 && blue == 219 && alpha == 255) {
                
                found = true;
                blockType = BlockType.HURT;
                spriteType = SpriteType.SPIKES;
                
            // any other color = play area
            } else if(alpha == 255) {
                
                found = true;
                blockType = BlockType.PLAY_AREA;
                spriteType = SpriteType.NONE;
                
            }
            
            // only create blocks when we have found "color coded" pixel
            if(found) {
                
                // calculate world position
                int y = i / levelWidth;
                int x = i % levelWidth;
                
                pos.x = x * Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
                pos.y = y * Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
                
                gridPos.x = x;
                gridPos.y = y;
                
                if(blockType == BlockType.HURT) {
                    
                    Trap trap = new Trap(pos, gridPos, blockType, spriteType, 1);
                    blocks.add(trap);
                    
                } else {
                    
                    Block block = new Block(pos, gridPos, blockType, spriteType);
                    
                    blocks.add(block);
                    
                    // create item
                    items.add(LevelCreator.createItem(red, green, blue, alpha, pos.x, pos.y));
                    
                    // create actors
                    actors.add(LevelCreator.createActor(red, green, blue, alpha, pos.x, pos.y));
                    
                    // create vanity objects
                    vanityObjects.addAll(LevelCreator.createVanityObjects(block));
                }
            }
        }
        
        return new LevelData(levelWidth, levelHeight, blocks, vanityObjects, items, actors);
    }

    public static List<Block> createBackground(Room room) {
        
        List<Block> blocks = new ArrayList<Block>();
        SpriteType spriteType = SpriteType.BACKGROUND_TILE_01;
        
        for(Block block : room.getData().getBlocks()) {
            
            if(block.getBlocktype() != BlockType.SOLID) {
                
                NeighborData data = room.getNeighbors(block, true);
                
                Block n = data.getNeighbors().get(Direction.NORTH);
                Block s = data.getNeighbors().get(Direction.SOUTH);
                
                if(n != null && s != null && 
                        n.getBlocktype() == BlockType.SOLID &&
                        s.getBlocktype() == BlockType.SOLID) {
                    spriteType = SpriteType.BRICK_BG_TOP_BOTTOM;
                } else if(s != null && s.getBlocktype() == BlockType.SOLID) {
                    spriteType = SpriteType.BRICK_BG_BOTTOM;
                } else if(n != null && n.getBlocktype() == BlockType.SOLID) { 
                    spriteType = SpriteType.BRICK_BG_TOP;
                } else {
                    spriteType = SpriteType.BRICK_BG_MAIN_01;
                }
                
                blocks.add(new Block(block.getWorldPosition(), block.getGridPosition(), BlockType.BACKGROUND, spriteType));
            }
        }
        
        System.out.println("Background tile count: " + blocks.size());
        
        return blocks;
    }
    
    public static Actor createActor(int red, int green, int blue, int alpha, int x, int y) {
        
        Actor actor = null;
        Point pos = new Point(x, y);
        
        // red = roller
        if(red == 255 && green == 0 && blue == 0 && alpha == 255) {
            actor = Game.instance.getActorManager().createEnemyInstance("Roller", pos, EnemyType.ROLLER, 3);
        } else if(red == 142 && green == 24 && blue == 61 && alpha == 255) {
          actor = Game.instance.getActorManager().createEnemyInstance("Slime", pos, EnemyType.SLIME, 3);
        }
        
        return actor;
    }
    
    public static GameObject createItem(int red, int green, int blue, int alpha, int x, int y) {
        GameObject item = null;
        
        // yellow = chest
        if(red == 254 && green == 216 && blue == 35 && alpha == 255) {
            
            item = new Chest(new Point(x, y), SpriteType.CHEST_CLOSED);
            
        // orange = crystal
        } else if(red == 251 && green == 154 && blue == 7 && alpha == 255) {
            
            item = new Crystal(new Point(x, y), SpriteType.CRYSTAL);
            
        }
        
        return item;
    }
    
    public static List<VanityObject> createVanityObjects(Block block) {
        
        List<VanityObject> objs = new ArrayList<VanityObject>();
        
        // create torches around the exit and player spawn.
        if(block.getBlocktype() == BlockType.EXIT || block.getBlocktype() == BlockType.PLAYER_SPAWN) {
            
            int x = block.getWorldPosition().x;
            int y = block.getWorldPosition().y;
            
            int spriteSize = Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
            
            objs.add(new Torch(new Point(x - spriteSize, y)));
            objs.add(new Torch(new Point(x + spriteSize, y)));
        }
        
        return objs;
    }
    
    public static List<Block> calculateSprites(Room room) {
        
        List<Block> calcBlocks = new ArrayList<Block>(room.getData().getBlocks());
        SpriteCreator spriteCreator = Game.instance.getSpriteCreator();
        
        // get neighbors and decide sprite
        for(Block block : calcBlocks) {
         
            if(block.getIsEnabled() == false) continue;
            
            // only support solid blocks for now.
            if(block.getBlocktype() != BlockType.SOLID) continue;
            
            // -------------------------------------------------
            
            NeighborData data = room.getNeighbors(block, false);
            
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
            } 
            
            else if(w && n && e) {
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
            } 
            
            else if(s && w && ne) { 
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_90_DEGREE_CORNER), 3));
            } else if(w && n && se) { 
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_90_DEGREE_CORNER));
            } else if(n && e && sw) { 
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_90_DEGREE_CORNER), 1));
            } else if(s && e && nw) { 
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_90_DEGREE_CORNER), 2));
            } 
            
            else if(sw && se && nw && ne && !n && !s && !w && !e) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_FOUR_CORNER));
            } 
            
            else if(nw && ne && sw && !n && !s && !e && !w) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_THREE_CORNER), 3));
            } else if(nw && sw && se && !n && !s && !e && !w) { 
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_THREE_CORNER), 2));
            } else if(nw && ne && se && !n && !s && !e && !w) { 
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_THREE_CORNER));
            } else if(ne && sw && se && !n && !s && !e && !w) { 
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_THREE_CORNER), 1));
            } 
            
            else if(n && e) {
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
            } 
            
            else if(n && s) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_TWO_SIDED), 1));
            } else if(w && e) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_TWO_SIDED));
                
            } else if(w && ne && se && !n && !e && !s) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_TWO_CORNER), 3));
            } else if(n && sw && se && !w && !e && !s) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_TWO_CORNER));
            } else if(e && nw && sw && !w && !s && !n) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_TWO_CORNER), 1));
            } else if(s && nw && ne && !w && !e && !n) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_TWO_CORNER), 2));
            }
                
            else if(nw && e && !n && !s && !w) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_ONE_CORNER), 1));
            } else if(ne && s && !w && !e && !n) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_ONE_CORNER), 2));
            } else if(w && se && !n && !s && !e) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_ONE_CORNER), 3));
            } else if(n && sw && !w && !e && !s) {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_ONE_CORNER));
                
                
            } else if(w && ne && !n && !s && !e) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        RenderUtils.flipSpriteHorizontally(
                                spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_ONE_CORNER)
                                ), 3));
            } else if(n && se && !w && !s && !e) {
                block.setSprite(RenderUtils.flipSpriteHorizontally(
                        spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_ONE_CORNER)));
            } else if(e && sw && !n && !s && !w) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        RenderUtils.flipSpriteHorizontally(
                                spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_ONE_CORNER)
                                ), 1));
            } else if(s && nw && !w && !n && !e) {
                block.setSprite(RenderUtils.rotateImageClockwise(
                        RenderUtils.flipSpriteHorizontally(
                                spriteCreator.CreateSprite(SpriteType.WALL_ONE_SIDED_ONE_CORNER)
                                ), 2));
            
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
            }
            
            else if(nw && ne) {
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
            } 
            
            else if(nw) {
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
            
            else {
                block.setSprite(spriteCreator.CreateSprite(SpriteType.WALL_FULL));
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
        
        System.out.println("Loading level data from path: \'" + Game.LEVELFOLDER + path + "\'");

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
