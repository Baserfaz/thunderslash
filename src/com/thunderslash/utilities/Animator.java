package com.thunderslash.utilities;

import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.Animation;
import com.thunderslash.enumerations.AnimationType;

public class Animator {

    //private Animation lightningStrikeAnim;
    private List<Animation> currentAnims;
    
    public Animator() {
        // this.lightningStrikeAnim = AnimationCreator.createAnimation(AnimationType.LIGHTNING_STRIKE);
        this.currentAnims = new ArrayList<Animation>();
    }
    
    public void play(AnimationType animType, int x, int y) {
        
        Animation anim = null;
        
        switch(animType) {
            
//            case LIGHTNING_STRIKE:
//                anim = new Animation(this.lightningStrikeAnim.getFrames(), x, y);
//                this.currentAnims.add(anim);
//                break;
                
            default:
                System.out.println("Animator::play: Animation type not supported: " + animType);
        }
    }
    
    public void removeAnim(Animation anim) { this.currentAnims.remove(anim); }
    public List<Animation> getCurrentAnims() { return currentAnims; }
}
