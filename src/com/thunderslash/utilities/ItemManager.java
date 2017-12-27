package com.thunderslash.utilities;

import java.util.ArrayList;
import java.util.List;

import com.thunderslash.engine.Camera;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ItemType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.gameobjects.Item;

public class ItemManager {

    public static List<Item> items = new ArrayList<Item>();

    public static List<Item> createAllItems() {
        List<Item> _items = new ArrayList<Item>();
        
        // get camera
        Camera cam = Game.instance.getCamera();
        
        int yCount = 0;
        int spriteSize = 32 * Game.SPRITESIZEMULT;
        
        int startx = cam.getCameraBounds().x + 50;
        
        int x = startx;
        int y = 20;
        
        for(SpriteType type : SpriteType.values()) {
            
            if(x > Game.WIDTH - 350) {
                yCount += 1;
                x = startx;
                y = 20 + spriteSize * yCount;
            }
            
            Item item = new Item(type.toString(), ItemType.OTHER, true, new Coordinate(x, y), type);
            _items.add(item);
            items.add(item);
            
            x += spriteSize;
        }
        
        return _items;
    }

    public static Item createItem(String name, ItemType itemType, boolean isDraggable,
            Coordinate worldPos, Coordinate tilePos,
            SpriteType spriteType, int spriteSize, int spriteSizeMult) {
        Item item = new Item(name, itemType, isDraggable, worldPos, spriteType);
        items.add(item);
        return item;
    }

}
