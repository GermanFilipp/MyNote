package com.example.note.model;

import java.util.ArrayList;

public class LocalData  {

    private String sessionID;
    public void setSessionID(String sessionId) {
        this.sessionID = sessionId;
    }

    public String getSessionID() {
        return sessionID;
    }

    // private ArrayList<Note> mNotes = new ArrayList<Note>();

 /*   public void setmNotes(ArrayList<Note> mNotes) {
        this.mNotes = mNotes;
    }
*/

    /*public ArrayList<Note> getmNotes() {
        return this.mNotes;
    }*/
  /*  public void addLocalNote(String title, String content, long id){
        mNotes.add(new Note(title, content, id));
    }

    public void addLocalNoteForIndex(String title, String content, long id, int position){
        mNotes.set(position, new Note(title, content, id));
    }

    public void clear(){
        sessionID = null;
        mNotes.clear();
    }*/

}
