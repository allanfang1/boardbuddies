package com.allan.boardbuddies.models;

import android.graphics.Path;

import androidx.annotation.ColorInt;

public class Stroke {
    @ColorInt
    private int color;
    private int width;
    private Path path;

    public Stroke(@ColorInt int color, int width, Path path){
        this.color = color;
        this.width = width;
        this.path = path;
    }

    public int getColor() {
        return color;
    }

    public int getWidth() {
        return width;
    }

    public Path getPath() {
        return path;
    }
}
