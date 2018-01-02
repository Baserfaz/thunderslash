
package com.thunderslash.data;

public class Power {

    private int currentPower = 0;
    private int maxPower = 3;
    
    public Power() {
        this.currentPower = this.maxPower;
    }
    
    public Power(int current, int max) {
        this.currentPower = current;
        this.maxPower = max;
    }
    
    public Power(int max) {
        this.maxPower = max;
        this.currentPower = max;
    }

    public int getCurrentPower() {
        return this.currentPower;
    }

    public int getMaxPower() {
        return this.maxPower;
    }
    
    public void addCurrentPower(int amount) {
        this.currentPower += amount;
        if(this.currentPower > this.maxPower) this.currentPower = this.maxPower;
    }
    
    public void setCurrentPower(int currentPower) {
        this.currentPower = currentPower;
    }
    
}
