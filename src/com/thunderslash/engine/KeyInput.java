package com.thunderslash.engine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import com.thunderslash.enumerations.GameState;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Player;
import com.thunderslash.utilities.Vector2;

public class KeyInput extends KeyAdapter {

    private Map<Integer, String> buttons = new HashMap<Integer, String>();
    private Map<Integer, String> keyBinds = new HashMap<Integer, String>();
    
    public KeyInput() {
        
        // bind keys
        this.keyBinds.put(KeyEvent.VK_A, "LEFT");
        this.keyBinds.put(KeyEvent.VK_LEFT, "LEFT");
        
        this.keyBinds.put(KeyEvent.VK_D, "RIGHT");
        this.keyBinds.put(KeyEvent.VK_RIGHT, "RIGHT");
        
        this.keyBinds.put(KeyEvent.VK_W, "JUMP");
        this.keyBinds.put(KeyEvent.VK_UP, "JUMP");
        
        this.keyBinds.put(KeyEvent.VK_S, "DOWN");
        this.keyBinds.put(KeyEvent.VK_DOWN, "DOWN");
        
        this.keyBinds.put(KeyEvent.VK_SPACE, "ATTACK");
        
        this.keyBinds.put(KeyEvent.VK_E, "ACTION");
        this.keyBinds.put(KeyEvent.VK_ENTER, "ACTION");
        
        this.keyBinds.put(KeyEvent.VK_F, "CAST");
        this.keyBinds.put(KeyEvent.VK_CONTROL, "CAST");
    }

    public void keyPressed(KeyEvent e) {

        // get the pressed key 
        int key = e.getKeyCode();
        
        if(buttons.containsKey(key)) return;
        buttons.put(key, this.keyBinds.get(key));

        // -------------- HANDLE INPUTS ------------------

        if(Game.instance.getGamestate() == GameState.MAINMENU) this.handleKeysInMenu(e);
        else if(Game.instance.getGamestate() == GameState.INGAME) this.handleKeysInGame(e);
        else if(Game.instance.getGamestate() == GameState.PAUSEMENU) this.handleKeysInPauseMenu(e);
        
    }

    public void keyReleased(KeyEvent e) {

        Actor player = Game.instance.getActorManager().getPlayerInstance();
        if(player == null || player.getHP().isDead()) return;
        
        // cache direction
        Vector2 dir = player.getDirection();
        
        // get the pressed key 
        int key = e.getKeyCode();

        String cmd = this.keyBinds.get(key);
        
        if(cmd == "RIGHT") {
            if(this.buttons.containsValue("LEFT")) {
                player.setDirection(new Vector2(-1f, dir.y));
            } else {
                player.setDirection(new Vector2(0f, dir.y));
            }
        } else if(cmd == "LEFT") {
            if(this.buttons.containsValue("RIGHT")) {
                player.setDirection(new Vector2(1f, dir.y));
            } else {
                player.setDirection(new Vector2(0f, dir.y));
            }
        } else if(cmd == "JUMP" || cmd == "DOWN") {
            player.setDirection(new Vector2(dir.x, 0f));
        }
        
        buttons.remove(key);
    }

    private void handleKeysInGame(KeyEvent e) {
        int key = e.getKeyCode();
        Player player = Game.instance.getActorManager().getPlayerInstance();
        
        if(player != null && player.getHP().isDead() == false) {
            if(keyBinds.containsKey(key)) {
                String cmd = this.keyBinds.get(key);
                if(cmd == "RIGHT") player.right();
                else if(cmd == "LEFT") player.left();
                else if(cmd == "JUMP") player.jump();
                else if(cmd == "ATTACK") player.attack();
                else if(cmd == "DOWN") player.drop();
                else if(cmd == "ACTION") player.action();
                else if(cmd == "CAST") player.cast();
            }
        }
        
        // debug keys ingame.
        if(key == KeyEvent.VK_F1) {
            Game.drawDebugInfo = !Game.drawDebugInfo;
        } else if(key == KeyEvent.VK_F2) {
            Game.drawCameraRect = !Game.drawCameraRect;
        } else if(key == KeyEvent.VK_F3) {
            Game.drawCurrentBlock = !Game.drawCurrentBlock;
        } else if(key == KeyEvent.VK_F4) {
            Game.drawGameObjectRects = !Game.drawGameObjectRects;
        } else if(key == KeyEvent.VK_F5) { 
            Game.drawActorCollisionPoints = !Game.drawActorCollisionPoints;
        } else if(key == KeyEvent.VK_F6) { 
            Game.drawAttackBoxes = !Game.drawAttackBoxes;
        } else if(key == KeyEvent.VK_F12) {
            Game.isPaused = !Game.isPaused;
        } else if(key == KeyEvent.VK_ESCAPE) {
            Game.instance.setGamestate(GameState.PAUSEMENU);
            Game.isPaused = true;
        }
    }

    private void handleKeysInPauseMenu(KeyEvent e) {
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_ESCAPE) {
            Game.instance.setGamestate(GameState.INGAME);
            Game.isPaused = false;
        }
    }
    
    private void handleKeysInMenu(KeyEvent e) {
        int key = e.getKeyCode();   
        
        if(key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
}
