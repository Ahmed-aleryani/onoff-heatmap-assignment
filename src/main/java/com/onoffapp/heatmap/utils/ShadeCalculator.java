package com.onoffapp.heatmap.utils;

public class ShadeCalculator {
    public static int calculateShade(double rate, int numberOfShades) {
        double segment = 100.0 / numberOfShades;
        int shade = (int) Math.ceil(rate / segment);
        return Math.max(1, Math.min(shade, numberOfShades));
    }
}