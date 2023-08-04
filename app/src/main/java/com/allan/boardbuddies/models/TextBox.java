package com.allan.boardbuddies.models;

import android.graphics.Path;

import com.google.gson.annotations.Expose;

public class TextBox {
    @Expose
    private float x, y;
    @Expose
    private int textSize;
    @Expose
    private String text;
    @Expose
    private int color;

    public TextBox(float x, float y, String text, int size, int color){
        this.x = x;
        this.y = y;
        this.textSize = size;
        this.text = text;
        this.color = color;
    }

    public int getTextSize() {
        return textSize;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
