package com.thunderslash.engine;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.thunderslash.enumerations.GameState;

public class MouseInput implements MouseMotionListener, MouseListener {
    
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

    public void mouseMoved(MouseEvent e) {
        if(Game.instance.getGamestate() == GameState.INGAME) {
            Game.instance.setMousePos(e.getPoint());
        }
    }

    public void mouseDragged(MouseEvent e) {
        if(Game.instance.getGamestate() == GameState.INGAME) {
            Game.instance.setMousePos(e.getPoint());
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
