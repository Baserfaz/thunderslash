package com.thunderslash.utilities;


import java.awt.image.BufferedImage;

import com.thunderslash.data.Animation;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.AnimationType;

public class AnimationCreator {

    public static Animation createDustCloudAnimation() {
        
        // create individual sprites, add them to an array and create an animation.
        
        SpriteCreator spriteCreator = Game.instance.getSpriteCreator();
        
        int spriteSizeMult = 3;
        
        BufferedImage img1 = spriteCreator.CreateCustomSizeSprite(3 * Game.SPRITEGRIDSIZE, 6 * Game.SPRITEGRIDSIZE, 12, 12, spriteSizeMult);
        BufferedImage img2 = spriteCreator.CreateCustomSizeSprite(3 * Game.SPRITEGRIDSIZE + 13, 6 * Game.SPRITEGRIDSIZE + 2,
                8, 8, spriteSizeMult);
        BufferedImage img3 = spriteCreator.CreateCustomSizeSprite(3 * Game.SPRITEGRIDSIZE + 22, 6 * Game.SPRITEGRIDSIZE + 2,
                5, 7, spriteSizeMult);
        
        BufferedImage[] frames = {img1, img2, img3};
        Animation animation = new Animation(frames);
        
        return animation;
    }
    
    public static Animation createAnimation(AnimationType animType) {
        
        SpriteCreator spriteCreator = Game.instance.getSpriteCreator();
        Animation animation = null;
        
        switch(animType) {
            case PLAYER_IDLE:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 0, 4));
                break;
                
            case PLAYER_WALK:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 1, 4));
                break;
            
            case PLAYER_FALL:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 4, 4));
                break; 
                
            case PLAYER_ATTACK:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 2, 8));
                break;
                
            case PLAYER_CAST:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 6, 2));
                break;
                
            case PLAYER_DEFEND:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 3, 2));
                break;
                
            case CRYSTAL_BOUNCE:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 8, 4));
                break;
                
            case LIGHTNING_STRIKE:
                System.out.println("AnimationCreator::createAnimation: Lightning strike animation not yet implemented!");
                break;
                
            case TORCH:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 9, 3));
                break;
                
            default:
                System.out.println("AnimationCreator:createAnimation: Animationtype not supported: " + animType);
                break;
            
        }
        
        return animation;
    }
    
}
