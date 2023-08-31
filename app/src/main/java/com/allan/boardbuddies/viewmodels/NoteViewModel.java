package com.allan.boardbuddies.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.allan.boardbuddies.models.Note;
import com.allan.boardbuddies.repositories.NoteRepository;

import java.util.ArrayList;

public class NoteViewModel extends ViewModel {
    private NoteRepository noteRepository;

    public NoteViewModel(){
        noteRepository = NoteRepository.getInstance();
    }

    public LiveData<ArrayList<Note>> getNotes(){
        return noteRepository.getNotes();
    }
}
