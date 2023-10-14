package com.allan.boardbuddies.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Board {
    @Expose
    private String title;
    @Expose
    private ArrayList<Stroke> strokes;
    @Expose
    private ArrayList<TextBox> texts;
    @Expose
    private String filename;

    public Board(String title, ArrayList<Stroke> strokes, ArrayList<TextBox> texts, String filename) {
        this.title = title;
        this.strokes = strokes;
        this.texts = texts;
        this.filename = filename;
    }

    public Board() {
        title = "";
        strokes = new ArrayList<>();
        texts = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Stroke> getStrokes() {
        return strokes;
    }

    public ArrayList<TextBox> getTexts() {
        return texts;
    }

    public String getFileName() {
        return filename;
    }
}
