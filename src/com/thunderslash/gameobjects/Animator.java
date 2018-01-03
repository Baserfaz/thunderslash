package com.thunderslash.gameobjects;

import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.Animation;
import com.thunderslash.enumerations.AnimationType;
import com.thunderslash.utilities.AnimationCreator;

public class Animator {

    private Animation lightningStrikeAnim;
    private List<Animation> currentAnims;
    
    public Animator() {
        this.lightningStrikeAnim = AnimationCreator.createAnimation(AnimationType.LIGHTNING_STRIKE);
        this.currentAnims = new ArrayList<Animation>();
    }
    
    public void play(AnimationType animType, int x, int y) {
        
        Animation anim = null;
        
        switch(animType) {
            
            case LIGHTNING_STRIKE:
                anim = new Animation(this.lightningStrikeAnim.getFrames(), x, y);
                this.currentAnims.add(anim);
                break;
                
                default:
                    System.out.println("animation type not supported!");
        }
    }
    
    public void removeAnim(Animation anim) {
        this.currentAnims.remove(anim);
    }

    public List<Animation> getCurrentAnims() {
        return currentAnims;
    }
}
