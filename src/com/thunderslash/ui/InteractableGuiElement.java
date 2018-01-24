package com.thunderslash.ui;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.GameState;
import com.thunderslash.enumerations.InteractAction;

public abstract class InteractableGuiElement extends GuiElement {

    private InteractAction onClickAction;
    private InteractAction onHovertAction;
    
    public InteractableGuiElement(int x, int y, int width, int height,
            InteractAction onClickAction, InteractAction onHoverAction) {
        super(x, y, width, height);
        
        this.onClickAction = onClickAction;
        this.onHovertAction = onHoverAction;
    }

    public  void onClick() {
        if(this.isEnabled) {
            switch(this.onClickAction) {
                case EXIT_TO_OS:
                    System.exit(0);
                    break;
                case PLAY:
                    Game.instance.startNewGame();
                    break;
                case RESUME:
                    Game.instance.setGamestate(GameState.INGAME);
                    Game.isPaused = false;
                    break;
                default:
                    // System.out.println("Not supported action: " + this.onClickAction);
                    break;
            }
        }
    }
    
    public  void onHover() {
        if(this.isEnabled) {
            switch(this.onHovertAction) {
                case EXIT_TO_OS:
                    System.exit(0);
                    break;
                case PLAY:
                    Game.instance.startNewGame();
                    break;
                case RESUME:
                    Game.instance.setGamestate(GameState.INGAME);
                    Game.isPaused = false;
                    break;
                default:
                    // System.out.println("Not supported action: " + this.onHovertAction);
                    break;
            }
        }
    }
    
    // ---- GETTERS & SETTERS ----
    public InteractAction getOnClickAction() { return onClickAction; }
    public void setOnClickAction(InteractAction onClickAction) { this.onClickAction = onClickAction; }
    public InteractAction getOnHovertAction() { return onHovertAction; }
    public void setOnHovertAction(InteractAction onHovertAction) { this.onHovertAction = onHovertAction; }
    
}
