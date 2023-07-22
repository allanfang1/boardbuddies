package com.allan.boardbuddies.models;

import android.graphics.Path;

public class Stroke {
    private int color;
    private int width;
    private Path path;

    public Stroke(int color, int width, Path path){
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
