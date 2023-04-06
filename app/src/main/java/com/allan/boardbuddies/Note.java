package com.allan.boardbuddies;

public class Note {
    private String title;
    private String content;

    public Note(String title, String content){
        this.title = title;
        this.content = content;
    }

    public String getTitle(){
        return this.title;
    }
}
