package com.thunderslash.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.thunderslash.engine.Game;

public class Animation {

    private BufferedImage[] frames;
   
    private double currentFrameTime = 0;
    private int currentAnimIndex = 0;
    private double frameTime = 20.0;
    
    private BufferedImage currentFrame = null;
    private boolean hasFinished = false;
    
    private int x = 0, y = 0;
    
    public Animation(BufferedImage[] frames) {
        this.frames = frames;
    }
    
    public Animation(ArrayList<BufferedImage> frames) {
        this.frames = new BufferedImage[frames.size()];
        
        for(int i = 0; i < frames.size(); i++) {
            this.frames[i] = frames.get(i);
        }
    }
    
    public Animation(BufferedImage[] frames, int x, int y) {
        this.frames = frames;
        this.x = x;
        this.y = y;
    }
    
    public Animation(ArrayList<BufferedImage> frames, int x, int y) {
        this.frames = new BufferedImage[frames.size()];
        
        for(int i = 0; i < frames.size(); i++) {
            this.frames[i] = frames.get(i);
        }
        
        this.x = x;
        this.y = y;
    }
    
    public int getAnimationLength() {
        return this.frames.length;
    }
    
    public void tick() {
        
        this.currentFrame = this.getFrame(this.currentAnimIndex);
        double dt = Game.instance.getTimeBetweenFrames();
        
        if(this.currentFrameTime > this.frameTime) {
            this.currentFrameTime = 0.0;
            this.currentAnimIndex += 1;
            if(this.currentAnimIndex >= this.getAnimationLength()) {
                this.currentAnimIndex = 0;
                this.hasFinished = true;
            }
        }
        
        this.currentFrameTime += dt;
        
    }
    
    public BufferedImage getFrame(int index) {
        
        BufferedImage img = null;
        
        try {
            img = this.frames[index];
        } catch(ArrayIndexOutOfBoundsException e) {
            img = this.frames[0];
        }
        
        return img;
    }
    
    public BufferedImage[] getFrames() {
        return frames;
    }

    public BufferedImage getCurrentFrame() {
        return currentFrame;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getHasFinished() {
        return this.hasFinished;
    }

    public void setHasFinished(boolean hasFinished) {
        this.hasFinished = hasFinished;
    }
    
}
