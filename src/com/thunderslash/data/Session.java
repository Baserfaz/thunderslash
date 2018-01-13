package com.thunderslash.data;

public class Session {

    private int score;
    
    public Session() {
        this.score = 0;
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public void addScore(int amount) { 
        this.score += amount; 
        if(this.score < 0) this.score = 0;
    }
}
