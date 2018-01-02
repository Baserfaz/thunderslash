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
                animation = new Animation(spriteCreator.createMultipleSprites(8, 0, 5));
                break;
                
            case PLAYER_WALK:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 1, 5));
                break;
            
            case PLAYER_FALL:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 4, 4));
                break;
                
            case PLAYER_ATTACK:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 2, 9));
                break;
                
            case PLAYER_DEFEND:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 3, 7));
                break;
                
            case CRYSTAL_BOUNCE:
                animation = new Animation(spriteCreator.createMultipleSprites(8, 8, 7));
                break;
                
            default:
                System.out.println("AnimationCreator:createAnimation: Animationtype not supported: " + animType);
                break;
            
        }
        
        return animation;
    }
    
}
