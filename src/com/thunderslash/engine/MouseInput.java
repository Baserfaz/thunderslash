package com.thunderslash.engine;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.GuiElement;
import com.thunderslash.enumerations.CursorMode;
import com.thunderslash.enumerations.GameState;
import com.thunderslash.gameobjects.Item;
import com.thunderslash.gameobjects.Player;
import com.thunderslash.utilities.ItemManager;

public class MouseInput implements MouseMotionListener, MouseListener {

    private GuiElement clickedElement;
    private Item clickedItem;

    public void mousePressed(MouseEvent e) {
        if(Game.instance.getGamestate() == GameState.MENU) {
            handleMousePressedInMenu(e);
        } else if(Game.instance.getGamestate() == GameState.INGAME) {
            handleMousePressedInGame(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(Game.instance.getGamestate() == GameState.MENU) {
            handleMouseReleaseInMenu(e);
        } else if(Game.instance.getGamestate() == GameState.INGAME) {
            handleMouseReleaseInGame(e);
        }
    }

    // mouse hover
    public void mouseMoved(MouseEvent e) {
        if(Game.instance.getGamestate() == GameState.INGAME) {
            
            // cache mouse position
            Game.instance.setMousePos(e.getPoint());
            
            // set hover item to null
            Game.instance.getDynamicGuiManager().setMouseHoverItem(null);
            
            // if we are hovering over an item
            for(Item item : ItemManager.items) {
                if(item.getIsVisible() && item.getIsEnabled()) {
                    if(item.getBounds().contains(e.getPoint())) {
                     
                        // hovering on an item
                        Game.instance.getDynamicGuiManager().setMouseHoverItem(item);
                        break;
                        
                    }
                }
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if(Game.instance.getGamestate() == GameState.INGAME) {
            Game.instance.setMousePos(e.getPoint());
            
            // set hover item to null
            Game.instance.getDynamicGuiManager().setMouseHoverItem(null);
        }
    }

    // not used
    public void mouseEntered(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    private void handleMouseReleaseInGame(MouseEvent e) {

    }

    private void handleMousePressedInGame(MouseEvent e) {

    }

    private void handleMousePressedInMenu(MouseEvent e) {}
    private void handleMouseReleaseInMenu(MouseEvent e) {}
}
