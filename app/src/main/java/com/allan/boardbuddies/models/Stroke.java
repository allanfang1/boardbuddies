package com.allan.boardbuddies.models;

import android.graphics.Path;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

public class Stroke {

    @Expose
    private int color;
    @Expose
    private int width;
    @Expose
    private ArrayList<float[]> points;
    private Path path;

    public Stroke(int color, int width, Path path){
        this.color = color;
        this.width = width;
        this.path = path;
        this.points = new ArrayList<float[]>();
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

    public ArrayList<float[]> getPoints() {
        return points;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void addPoint(float x, float y){
        points.add(new float[]{x, y});
    }
}
