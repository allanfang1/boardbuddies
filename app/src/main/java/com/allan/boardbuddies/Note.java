package com.allan.boardbuddies;

// defining a Note
public class Note {
    private String title;
    private String content;
    private String name;

    public Note(String title, String content, String name){
        this.title = title;
        this.content = content;
        this.name = name;
    }

    public String getTitle(){
        return this.title;
    }
    public String getContent(){
        return this.content;
    }
    public String getName(){
        return this.name;
    }

}
