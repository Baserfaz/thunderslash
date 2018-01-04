package com.thunderslash.engine;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import com.thunderslash.enumerations.GameState;
import com.thunderslash.ui.GuiElement;

public class MouseInput implements MouseMotionListener, MouseListener {
    
    public void mousePressed(MouseEvent e) {
        if(Game.instance.getGamestate() == GameState.MENU) {
            this.handleMousePressedInMenu(e);
        } else if(Game.instance.getGamestate() == GameState.INGAME) {
            this.handleMousePressedInGame(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(Game.instance.getGamestate() == GameState.MENU) {
            this.handleMouseReleaseInMenu(e);
        } else if(Game.instance.getGamestate() == GameState.INGAME) {
            this.handleMouseReleaseInGame(e);
        }
    }

    public void mouseMoved(MouseEvent e) {
        Game.instance.setMousePos(e.getPoint());
    }

    public void mouseDragged(MouseEvent e) {
        Game.instance.setMousePos(e.getPoint());
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
    
    private void handleMouseReleaseInMenu(MouseEvent e) {
        
        // cache 
        Point mousePos = Game.instance.getMousePos();
        List<GuiElement> elements = Game.instance.getGuiElementManager().getElements();
        
        if(elements.isEmpty() || mousePos == null) return;
        
        for(GuiElement element : elements) {
            if(element.isEnabled()) {
                if(element.getBounds().contains(mousePos)) {
                    element.onClick();
                    break;
                }
            }
        }
        
    }
}
