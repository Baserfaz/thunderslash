package com.thunderslash.utilities;

import com.thunderslash.gameobjects.Item;

public class DynamicGuiManager {
    
    // this class caches dynamically
    // created GUI-elements and data
    
    private Item mouseHoverItem;
    
    public Item getMouseHoverItem() {
        return mouseHoverItem;
    }

    public void setMouseHoverItem(Item mouseHoverItem) {
        this.mouseHoverItem = mouseHoverItem;
    }
    
}
