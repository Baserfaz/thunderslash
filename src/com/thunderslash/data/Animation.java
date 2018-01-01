package com.thunderslash.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {

    private BufferedImage[] frames;
    
    public Animation(BufferedImage[] frames) {
        this.frames = frames;
    }

    public Animation(ArrayList<BufferedImage> frames) {
        this.frames = new BufferedImage[frames.size()];
        
        for(int i = 0; i < frames.size(); i++) {
            this.frames[i] = frames.get(i);
        }
    }
    
    public int getAnimationLength() {
        return this.frames.length;
    }
    
    public BufferedImage getFrame(int index) {
        
        BufferedImage img = null;
        
        try {
            img = this.frames[index];
        } catch(ArrayIndexOutOfBoundsException e) {
            //System.out.println(e);
            img = this.frames[0];
        }
        
        return img;
    }
    
    public BufferedImage[] getFrames() {
        return frames;
    }
    
}
