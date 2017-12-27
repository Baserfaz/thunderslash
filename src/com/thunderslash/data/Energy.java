package com.thunderslash.data;

public class Energy {

    private int maxEnergy = 3;
    private int currentEnergy;
    
    public Energy() {
        this.currentEnergy = this.maxEnergy;
    }
    
    public Energy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
        this.currentEnergy = maxEnergy;
    }

    public int getMaxEnergy() {
        return this.maxEnergy;
    }
    
    public void setMaxEnergy(int e) {
        this.maxEnergy = e;
    }
    
    public void addMaxEnergy(int amount) {
        this.maxEnergy += amount;
        if(this.maxEnergy > 10) {
            this.maxEnergy = 10;
        }
    }
    
    public void subtractMaxEnergy(int amount) {
        this.maxEnergy -= amount;
        if(this.maxEnergy < 1) this.maxEnergy = 1;
    }
    
    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public void setCurrentEnergy(int currentEnergy) {
        this.currentEnergy = currentEnergy;
    }
    
    public void replenishCurrentEnergy(int amount) {
        this.currentEnergy =+ amount;
        if(this.currentEnergy > this.maxEnergy) this.currentEnergy = this.maxEnergy;
    }
    
    public void removeCurrentEnergy(int amount) {
        this.currentEnergy -= amount;
        if(this.currentEnergy < 0) this.currentEnergy = 0;
    }
    
}
