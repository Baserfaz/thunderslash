package com.thunderslash.gameobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ItemType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.utilities.Coordinate;
import com.thunderslash.utilities.RenderUtils;

public class Item extends GameObject {

    private ItemType itemType;
    private String name;
    private boolean isIdentified;
    
    private boolean isDraggable;
    private Coordinate draggingStartPosition;
    private boolean dragging;
    
    public Item(String name, ItemType itemType, boolean isDraggable,
            Coordinate worldPos, SpriteType spriteType) {
        super(worldPos, spriteType);
        this.itemType = itemType;
        this.name = name;
        this.dragging = false;
        this.isDraggable = isDraggable;
        this.isIdentified = false;
    }

    public void tick() {
        if(dragging) {
            Point p = Game.instance.getMousePos();
            int offset = (Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT) / 2;
            setWorldPosition(p.x - offset, p.y - offset);
        }
    }

    public void render(Graphics g) {
        
        if(this.isIdentified) {
            if(this.dragging) {
                g.drawImage(RenderUtils.tint(this.sprite, true), 
                        this.worldPosition.x, this.worldPosition.y, null);
            } else {
                g.drawImage(this.sprite, this.worldPosition.x, this.worldPosition.y, null);
            }
        } else {
            
            // unidentified items are black
            g.drawImage(RenderUtils.changeImageColor(this.sprite, Color.black), 
                    this.worldPosition.x, this.worldPosition.y, null);
            
        }
        
        if(Game.drawItemRects) {
            int size = Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
            g.setColor(Game.itemRectColor);
            g.drawRect(this.worldPosition.x, this.worldPosition.y, size, size);
        }
    }
    
    public Rectangle getBounds() {
        int size = Game.SPRITEGRIDSIZE * Game.SPRITESIZEMULT;
        return new Rectangle(this.worldPosition.x, this.worldPosition.y, size, size);
    }
    
    public boolean isDragging() { return dragging; }
    public void setDragging(boolean dragging) { this.dragging = dragging; }
    
    public String getInfo() {
        String s = "Name: " + this.name;
        return s;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDraggable() {
        return isDraggable;
    }

    public void setDraggable(boolean isDraggable) {
        this.isDraggable = isDraggable;
    }

    public Coordinate getDraggingStartPosition() {
        return draggingStartPosition;
    }

    public void setDraggingStartPosition(Coordinate draggingStartPosition) {
        this.draggingStartPosition = draggingStartPosition;
    }

    public boolean isIdentified() {
        return isIdentified;
    }

    public void setIdentified(boolean isIdentified) {
        this.isIdentified = isIdentified;
    }
}
