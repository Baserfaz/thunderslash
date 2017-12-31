package com.thunderslash.data;

public class Health {

    private int currentHP;
    private int maxHP = 3;
    private boolean isDead = false;

    public Health() {
        this.setCurrentHP(maxHP);
    }

    public Health(int hp) {
        this.maxHP = hp;
        this.currentHP = this.maxHP;
    }
    
    public void takeDamage(int amount) {
        if(this.isDead) return;
        this.currentHP -= amount;
        if(currentHP <= 0) { this.isDead = true; }
    }

    public void healDamage(int amount) {
        if(this.isDead) return;
        this.currentHP += amount;
        if(currentHP > maxHP) currentHP = maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public void setMaxHP(int amount) {
        this.maxHP = amount;
        if(this.maxHP > 10) this.maxHP = 10;
    }
  
    public int getMaxHP() {
        return this.maxHP;
    }
    
    public void subtractMaxHP(int amount) {
        this.maxHP -= amount;
        if(this.maxHP < 1) this.maxHP = 1;
    }
    
    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

}
