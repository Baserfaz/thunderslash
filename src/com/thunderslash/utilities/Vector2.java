package com.thunderslash.utilities;

import java.lang.Math;
import java.text.DecimalFormat;

public class Vector2 {

    public float x;
    public float y;
    public float magnitude;
    
    public Vector2() {
        this.x = 0f;
        this.y = 0f;
        this.magnitude = 0f;
    }
    
    public Vector2(float x, float y, float magnitude) {
        this.x = x;
        this.y = y;
        this.magnitude = magnitude;
    }
    
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
        this.magnitude = 1f;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return df.format(this.x) + ", " + df.format(this.y);
    }
    
    public float distance(Vector2 v2) {
        float xx = (v2.x - this.x) * (v2.x - this.x);
        float yy = (v2.y - this.y) * (v2.y - this.y);
        return (float)Math.sqrt(xx + yy);
    }
    
    public Vector2 add(Vector2 v2) {
        return new Vector2(this.x + v2.x, this.y + v2.y);
    }
    
    public Vector2 subtract(Vector2 v2) {
        return this.add(new Vector2(-v2.x, -v2.y));
    }
    
}
