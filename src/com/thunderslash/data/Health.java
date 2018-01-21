package com.thunderslash.data;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.GameState;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Enemy;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.gameobjects.Player;
import com.thunderslash.utilities.SpriteCreator;

public class Health {

    private GameObject object;
    
    private int currentHP;
    private int maxHP = 3;
    private boolean isDead = false;

    public Health(int hp, GameObject obj) {
        this.maxHP = hp;
        this.currentHP = this.maxHP;
        this.object = obj;
    }
    
    private void die() {
        this.isDead = true;
        
        if(this.object instanceof Player) {
            ((Player)this.object).onDeath();
        } else if(this.object instanceof Enemy) { 
            ((Enemy)this.object).onDeath();
        } else {
            System.out.println("Health::die: not supported for type: " + this.object.getClass().getTypeName());
        }
    }
    
    public void takeDamage(int amount) {
        if(this.isDead) return;
        this.currentHP -= amount;
        if(currentHP <= 0) { this.die(); }
        
        // subtract score
        if(this.object instanceof Player) Game.instance.getSession().addScore(-amount * 10);
    }

    public void healDamage(int amount) {
        if(this.isDead) return;
        this.currentHP += amount;
        if(currentHP > maxHP) currentHP = maxHP;
    }

    public void setMaxHP(int amount) {
        this.maxHP = amount;
        if(this.maxHP > 10) this.maxHP = 10;
    }
    
    public void subtractMaxHP(int amount) {
        this.maxHP -= amount;
        if(this.maxHP < 1) this.maxHP = 1;
    }
    
    // ---- GETTERS & SETTERS ----
    public int getCurrentHP() { return currentHP; }
    public void setCurrentHP(int currentHP) { this.currentHP = currentHP; }
    public int getMaxHP() { return this.maxHP; }
    public boolean isDead() { return isDead; }
    public void setDead(boolean isDead) { this.isDead = isDead; }
    public GameObject getObject() { return object; }
    public void setObject(GameObject object) { this.object = object; }
}
