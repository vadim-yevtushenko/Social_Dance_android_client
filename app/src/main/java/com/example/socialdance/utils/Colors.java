package com.example.socialdance.utils;

import android.graphics.Color;

public enum Colors {
    RED(Color.RED),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE),
    ORANGE(Color.parseColor("#FF7800"));

    private int color;

    Colors(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
