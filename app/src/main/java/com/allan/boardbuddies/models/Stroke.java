package com.allan.boardbuddies.models;

import android.graphics.Path;

import androidx.annotation.ColorInt;

import com.allan.boardbuddies.Utilities;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Stroke {
    @ColorInt
    @Expose
    private int color;
    @Expose
    private int width;
    @Expose
    private ArrayList<float[]> points;
    private Path path;

    public Stroke(@ColorInt int color, int width, Path path){
        this.color = color;
        this.width = width;
        this.path = path;
        this.points = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stroke stroke = (Stroke) o;
        return color == stroke.color && width == stroke.width && Utilities.compareArraylist(points, stroke.points);
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
