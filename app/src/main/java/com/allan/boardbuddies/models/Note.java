package com.allan.boardbuddies.models;

// defining a Note
public class Note {
    private String title;
    private String content;
    private String filename;

    public Note(String title, String content, String filename){
        this.title = title;
        this.content = content;
        this.filename = filename;
    }

    public String getTitle(){
        return this.title;
    }
    public String getContent(){
        return this.content;
    }
    public String getName(){
        return this.filename;
    }

}
