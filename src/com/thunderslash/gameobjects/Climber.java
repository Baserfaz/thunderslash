package com.thunderslash.gameobjects;

import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.RenderUtils;

public class Climber extends Enemy {

    public Climber(String name, Point worldPos, int hp) {
        super(name, worldPos, SpriteType.ENEMY_CLIMBER, true, hp);
    }

    public void onDeath() {
        BufferedImage img = Game.instance.getSpriteCreator().CreateSprite(SpriteType.ENEMY_CLIMBER_DEAD);
        img = RenderUtils.tint(img, true, 2);
        this.setSprite(img);
    }
    public void doBehaviour() {}
    protected void onCollision(Direction dir) {}

}
