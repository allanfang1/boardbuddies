package com.allan.boardbuddies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.allan.boardbuddies.models.Note;

import java.util.ArrayList;

public class MemoViewModel extends ViewModel {
    private final MutableLiveData<Integer> selectedNote = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Note>> notes = new MutableLiveData<>();

    public LiveData<Integer> getSelectedNotePosition(){
        return selectedNote;
    }

    public LiveData<ArrayList<Note>> getNotes(){
        return notes;
    }

    public void setSelectedNote(int position){
        selectedNote.setValue(position);
    }

    public void addNote(int position, Note in){
        ArrayList<Note> currentNotes = notes.getValue();

        if (currentNotes == null) {
            currentNotes = new ArrayList<>();
        }
        if (position == -1) {
            currentNotes.add(in);
        } else {
            currentNotes.add(position, in);
        }
        notes.setValue(currentNotes);
    }

    public void clearNotes() {
        notes.setValue(new ArrayList<>());
    }

    public void deleteNote(int position){
        notes.getValue().remove(position);
    }
}
