package com.thunderslash.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.enumerations.DepthLevel;
import com.thunderslash.enumerations.GameState;

public class GuiElementManager {

    private List<GuiElement> mainmenuElements;
    private List<GuiElement> loadingElements;
    private List<GuiElement> ingameElements;
    
    public GuiElementManager() {
        this.mainmenuElements = new ArrayList<GuiElement>();
        this.loadingElements = new ArrayList<GuiElement>();
        this.ingameElements = new ArrayList<GuiElement>();
    }

    public void render(Graphics g, GameState state) {
        
        List<GuiElement> buttons = new ArrayList<GuiElement>();
        List<GuiElement> imagesBackground = new ArrayList<GuiElement>();
        List<GuiElement> imagesForeground = new ArrayList<GuiElement>();
        
        List<GuiElement> selectedList = new ArrayList<GuiElement>();
        
        switch(state) {
        case INGAME:
            selectedList = this.ingameElements;
            break;
        case LOADING:
            selectedList = this.loadingElements;
            break;
        case MAINMENU:
            selectedList = this.mainmenuElements;
            break;
        default:
            System.out.println("GuiElementManager::render: Gamestate not supported!");
            break;
        }
        
        for(GuiElement e : selectedList) {
            if(e instanceof Button) buttons.add(e);
            else if(e instanceof GuiImage) {
                
                GuiImage img = (GuiImage) e;
                
                if(img.getDeptLevel() == DepthLevel.BACKGROUND) {
                    imagesBackground.add(e);
                } else if(img.getDeptLevel() == DepthLevel.FOREGROUND) {
                    imagesForeground.add(e);
                }
            }
        }
        
        // render in queue
        for(GuiElement e : imagesBackground) e.render(g);
        for(GuiElement e : imagesForeground) e.render(g);
        for(GuiElement e : buttons) e.render(g);
    }
    
    public void tick(GameState state) {
        
        List<GuiElement> selectedList = new ArrayList<GuiElement>();
        
        switch(state) {
        case INGAME:
            selectedList = this.ingameElements;
            break;
        case LOADING:
            selectedList = this.loadingElements;
            break;
        case MAINMENU:
            selectedList = this.mainmenuElements;
            break;
        default:
            System.out.println("GuiElementManager::render: Gamestate not supported!");
            break;
        }
        
        for(GuiElement e : selectedList) e.tick();
    }
    
    // ---- GETTERS & SETTERS ----
    public void addElementToMainmenu(GuiElement element) { this.mainmenuElements.add(element); }
    public List<GuiElement> getMainmenuElements() { return mainmenuElements; }
    public void addMultipleElementsToMainmenu(List<GuiElement> es) { this.mainmenuElements.addAll(es); }
    
    public void addElementToLoading(GuiElement element) { this.loadingElements.add(element); }
    public List<GuiElement> getLoadingElements() { return loadingElements; }
    public void addMultipleElementsToLoading(List<GuiElement> loadingElements) { this.loadingElements.addAll(loadingElements); }
    
    public void addElementToIngame(GuiElement element) { this.ingameElements.add(element); }
    public List<GuiElement> getIngameElements() { return ingameElements; }
    public void addMultipleElementsToIngame(List<GuiElement> ingameElements) { this.ingameElements.addAll(ingameElements); }
}
