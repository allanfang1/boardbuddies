package com.allan.boardbuddies.models;

import android.graphics.Path;

public class TextBox {
    private float x, y;
    private int textSize;
    private String text;
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
