package com.allan.boardbuddies.models;

import androidx.annotation.ColorInt;

import com.google.gson.annotations.Expose;

public class TextBox {
    @Expose
    private float x;
    @Expose
    private float y;
    @Expose
    private int textSize;
    @Expose
    private String text;
    @ColorInt
    @Expose
    private int color;

    public TextBox(float x, float y, String text, int size, @ColorInt int color){
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
