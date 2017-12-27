package com.thunderslash.engine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import com.thunderslash.enumerations.GameState;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.utilities.Vector2;

public class KeyInput extends KeyAdapter {

    private Map<Integer, String> buttons = new HashMap<Integer, String>();
    private Map<Integer, String> keyBinds = new HashMap<Integer, String>();
    
    public KeyInput() {
        
        // bind keys here
        
        this.keyBinds.put(KeyEvent.VK_A, "LEFT");
        this.keyBinds.put(KeyEvent.VK_D, "RIGHT");
        this.keyBinds.put(KeyEvent.VK_W, "JUMP");
        this.keyBinds.put(KeyEvent.VK_S, "DOWN");
        this.keyBinds.put(KeyEvent.VK_SPACE, "ATTACK");
    }

    public void keyPressed(KeyEvent e) {

        // get the pressed key 
        int key = e.getKeyCode();

        // add pressed button to a list of buttons
        if(buttons.containsKey(key)) {
            
            // one shot keys
            if(Game.instance.getGamestate() == GameState.INGAME) {
                
                Actor player = Game.instance.getActorManager().getPlayerInstance();
                if(player == null) return;
                Vector2 dir = player.getDirection();
                
                if(keyBinds.get(key) == "JUMP" || keyBinds.get(key) == "DOWN") {
                    player.setDirection(new Vector2(dir.x, 0f));
                }
            }
            
            return;
        }
        
        buttons.put(key, this.keyBinds.get(key));

        // -------------- HANDLE INPUTS ------------------

        if(Game.instance.getGamestate() == GameState.MENU) {
            handleKeysInMenu(e);
        } else if(Game.instance.getGamestate() == GameState.INGAME) {
            handleKeysInGame(e);
        }

        // debugging keys
        if(key == KeyEvent.VK_F1) {
            Game.drawDebugInfo = !Game.drawDebugInfo;
        } else if(key == KeyEvent.VK_F2) {
            Game.drawCameraRect = !Game.drawCameraRect;
        } else if(key == KeyEvent.VK_F3) {
            Game.drawItemRects = !Game.drawItemRects;
        } else if(key == KeyEvent.VK_F4) {
            Game.drawGameObjectRects = !Game.drawGameObjectRects;
        } else if(key == KeyEvent.VK_F5) { 
            Game.drawActorCollisionPoints = !Game.drawActorCollisionPoints;
        } else if(key == KeyEvent.VK_F12) {
            Game.isPaused = !Game.isPaused;
        } else if(key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

    }

    public void keyReleased(KeyEvent e) {

        Actor player = Game.instance.getActorManager().getPlayerInstance();
        if(player == null) return;
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
        } else if(cmd == "ATTACK") {
            // TODO
        }
        
        // remove all occurrences of key code from buttons
        buttons.remove(key);
    }

    private void handleKeysInGame(KeyEvent e) {
        int key = e.getKeyCode();
        
        if(keyBinds.containsKey(key)) {
            Actor player = Game.instance.getActorManager().getPlayerInstance();
            if(player == null) return;
            Vector2 dir = player.getDirection();
            
            String cmd = this.keyBinds.get(key);
            
            if(cmd == "RIGHT") {
                player.setDirection(new Vector2(1f, dir.y));
            } else if(cmd == "LEFT") {
                player.setDirection(new Vector2(-1f, dir.y));
            } else if(cmd == "JUMP") {
                player.setDirection(new Vector2(dir.x, 1f));
            } else if(cmd == "ATTACK") {
                // TODO
            } else if(cmd == "DOWN") {
                player.setDirection(new Vector2(dir.x, -1f));
            }
            
        }
    }

    private void handleKeysInMenu(KeyEvent e) {
        int key = e.getKeyCode();   
    }
}