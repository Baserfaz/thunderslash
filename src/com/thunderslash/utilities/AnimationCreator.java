package com.thunderslash.utilities;


import com.thunderslash.data.Animation;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.AnimationType;

public class AnimationCreator {

    public static Animation createAnimation(AnimationType animType) {
        
        SpriteCreator spriteCreator = Game.instance.getSpriteCreator();
        Animation animation = null;
        
        switch(animType) {
            case PLAYER_IDLE:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 0, 4));
                break;
                
            case PLAYER_WALK:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 1, 2));
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
                animation = new Animation(spriteCreator.createMultipleSprites(8, 9, 3));
                break;
                
            default:
                System.out.println("AnimationCreator:createAnimation: Animationtype not supported: " + animType);
                break;
            
        }
        
        return animation;
    }
    
}
