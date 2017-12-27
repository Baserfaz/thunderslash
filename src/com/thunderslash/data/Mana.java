package com.thunderslash.data;

public class Mana {

    private int maxMana = 3;
    private int currentMana;
    
    public Mana() {
        this.currentMana = this.maxMana;
    }
    
    public Mana(int maxMana) {
        this.maxMana = maxMana;
        this.currentMana = this.maxMana;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }
    
    public int getMaxMana() {
        return this.maxMana;
    }
    
    public void setMaxMana(int mana) {
        this.maxMana = mana;
    }
    
    public void addMaxMana(int amount) {
        this.maxMana += amount;
        if(this.maxMana > 10) this.maxMana = 10;
    }
    
    public void subtractMaxMana(int amount) {
        this.maxMana -= amount;
        if(this.maxMana < 1) this.maxMana = 1;
    }
    
    public void addCurrentMana(int amount) {
        this.currentMana += amount;
        if(this.currentMana > this.maxMana) this.currentMana = this.maxMana;
    }
    
    public void subtractCurrentMana(int amount) {
        this.currentMana -= amount;
        if(this.currentMana < 0) this.currentMana = 0;
    }
    
}
