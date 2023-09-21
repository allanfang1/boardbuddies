package com.allan.boardbuddies.models;

import android.graphics.Path;

import androidx.annotation.ColorInt;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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

    public Stroke(Stroke stroke){
        this.color = stroke.getColor();
        this.width = stroke.getWidth();
        this.points = stroke.getPoints();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stroke stroke = (Stroke) o;
        return color == stroke.color && width == stroke.width && arrayEquals(stroke.points);
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

    private Boolean arrayEquals(ArrayList<float[]> in){
        if (this.points == in){
            return true;
        }
        if (this.points == null || in == null){
            return false;
        }
        if (this.points.size() != in.size()){
            return false;
        }
        Iterator<float[]> it1 = this.points.iterator();
        Iterator<float[]> it2 = in.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            float[] t1 = it1.next();
            float[] t2 = it2.next();
            if (!Arrays.equals(t1, t2)) {
                return false;
            }
        }
        return true;
    }
}
