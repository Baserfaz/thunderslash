package com.thunderslash.data;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.AnimationType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.AnimationCreator;
import com.thunderslash.utilities.SpriteCreator;

public class SpriteStorage {

    private Map<SpriteType, BufferedImage> sprites;
    private Map<AnimationType, Animation> animations;
    
    public SpriteStorage() {
        this.sprites = new HashMap<SpriteType, BufferedImage>();
        this.animations = new HashMap<AnimationType, Animation>();
    }
 
    public void loadSprites() {
        SpriteCreator sc = Game.instance.getSpriteCreator();
        for(SpriteType s : SpriteType.values()) {
            this.addSprite(s, sc.CreateSprite(s));
        }
    }
    
    public void loadAnimations() {
        for(AnimationType s : AnimationType.values()) {
            this.addAnimation(s, AnimationCreator.createAnimation(s));
        }
    }
    
    // ---- SETTERS & GETTERS ----
    public void addSprite(SpriteType type, BufferedImage img) { this.sprites.put(type, img); }
    public BufferedImage getSprite(SpriteType type) { return this.sprites.get(type); }
    public void addAnimation(AnimationType type, Animation anim) { this.animations.put(type, anim); }
    public Animation getAnimation(AnimationType type) { return this.animations.get(type); }
}
