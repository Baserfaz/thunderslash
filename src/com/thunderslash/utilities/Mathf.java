package com.thunderslash.utilities;

import java.util.Random;

public class Mathf {

    public static float lerp(float point1, float point2, float alpha) {
        return point1 + alpha * (point2 - point1);
    }

    public static float clamp(float min, float max, float current) {
        if(current < min) return min;
        else if(current > max) return max;
        else return current;
    }

    public static double randomRange(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }
    
}
