package com.thunderslash.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class GuiElementManager {

    private List<GuiElement> elements;
    
    public GuiElementManager() {
        this.elements = new ArrayList<GuiElement>();
    }
    
    public void addElement(GuiElement element) {
        this.elements.add(element);
    }
    
    public List<GuiElement> getElements() {
        return elements;
    }

    public void render(Graphics g) {
        for(GuiElement e : this.elements) e.render(g);
    }
    
    public void tick() {
        for(GuiElement e : this.elements) e.tick();
    }
    
}
