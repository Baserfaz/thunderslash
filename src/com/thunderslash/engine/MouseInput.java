package com.thunderslash.engine;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.enumerations.GameState;
import com.thunderslash.enumerations.SoundEffect;
import com.thunderslash.ui.GuiElement;

public class MouseInput implements MouseMotionListener, MouseListener {
    
    private GuiElement lastElementHovered;
    
    public void mousePressed(MouseEvent e) {
        if(Game.instance.getGamestate() == GameState.MAINMENU) {
            this.handleMousePressedInMenu(e);
        } else if(Game.instance.getGamestate() == GameState.INGAME) {
            this.handleMousePressedInGame(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(Game.instance.getGamestate() == GameState.MAINMENU) {
            this.handleMouseReleaseInMenu(e);
        } else if(Game.instance.getGamestate() == GameState.INGAME) {
            this.handleMouseReleaseInGame(e);
        }
    }

    // hover effects on gui elements.
    public void mouseMoved(MouseEvent e) {
        Game.instance.setMousePos(e.getPoint());
        
        if(Game.instance.getGuiElementManager() == null) return;
        
        List<GuiElement> elements = new ArrayList<GuiElement>();
        
        switch(Game.instance.getGamestate()) {
        case INGAME:
            elements = Game.instance.getGuiElementManager().getIngameElements();
            break;
        case MAINMENU:
            elements = Game.instance.getGuiElementManager().getMainmenuElements();
            break;
        default:
            System.out.println("MouseInput::mouseMoved: unsupported gamestate \'" + Game.instance.getGamestate() + "\'");
            break;
        }

        
        if(elements.isEmpty()) return;
        
        boolean hoveredOnSomething = false;
        
        for(GuiElement element : elements) {
            if(element.isEnabled()) {
                
                element.setIsHovering(false);
                
                if(element.getBounds().contains(e.getPoint())) {
                    
                    hoveredOnSomething = true;
                    
                    if(this.lastElementHovered != element) {
                        Game.instance.getSoundManager().play(SoundEffect.HOVER);
                    }
                    
                    element.onHover();
                    
                    this.lastElementHovered = element;
                    break;
                }
            }
        }
        
        // didnt hover on anything
        if(hoveredOnSomething == false) {
            this.lastElementHovered = null;
        }
        
    }

    public void mouseDragged(MouseEvent e) {
        Game.instance.setMousePos(e.getPoint());
    }

    // not used
    public void mouseEntered(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    private void handleMouseReleaseInGame(MouseEvent e) {}
    private void handleMousePressedInGame(MouseEvent e) {}
    private void handleMousePressedInMenu(MouseEvent e) {}
    
    private void handleMouseReleaseInMenu(MouseEvent e) {
        
        // cache 
        Point mousePos = Game.instance.getMousePos();
        List<GuiElement> elements = Game.instance.getGuiElementManager().getMainmenuElements();
        
        if(elements.isEmpty() || mousePos == null) return;
        
        for(GuiElement element : elements) {
            if(element.isEnabled()) {
                if(element.getBounds().contains(mousePos)) {
                    Game.instance.getSoundManager().play(SoundEffect.SELECT);
                    element.onClick();
                    break;
                }
            }
        }
        
    }
}
