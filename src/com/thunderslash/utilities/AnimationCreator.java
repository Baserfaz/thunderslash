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

            default:
                System.out.println("AnimationCreator:createAnimation: Animationtype not supported: " + animType);
                break;
            
        }
        
        return animation;
    }
    
}
