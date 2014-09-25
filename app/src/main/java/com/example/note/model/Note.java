package com.example.note.model;


public class Note {

    long noteID;
    String title;
    String shortContent;

    public Note(){
    
}

    public long getId(){
        return noteID;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return shortContent;
    }

    public void setTitle(String _title){
        title = _title;
    }

    public void setDescription(String _description){
        shortContent = _description;
    }

    public void setID(long _id){
        noteID = _id;
    }

    public Note(String _title, String _description, long _id){
        title = _title;
        shortContent = _description;
        noteID = _id;
    }
}
