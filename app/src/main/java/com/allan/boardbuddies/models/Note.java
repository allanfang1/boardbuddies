package com.allan.boardbuddies.models;

import com.google.gson.annotations.Expose;

// defining a Note
public class Note {
    @Expose
    private String title;
    @Expose
    private String content;
    @Expose
    private String filename;

    public Note(String title, String content, String filename){
        this.title = title;
        this.content = content;
        this.filename = filename;
    }

    public Note() {
        this.content = "";
        this.title = "";
    }

    public String getTitle(){
        return this.title;
    }
    public String getContent(){
        return this.content;
    }
    public String getFileName(){
        return this.filename;
    }

}
