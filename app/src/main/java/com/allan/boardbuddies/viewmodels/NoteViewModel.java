package com.allan.boardbuddies.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.allan.boardbuddies.models.Board;
import com.allan.boardbuddies.models.Note;
import com.allan.boardbuddies.repositories.BoardRepository;
import com.allan.boardbuddies.repositories.NoteRepository;

import java.util.ArrayList;

public class NoteViewModel extends ViewModel {
    public NoteViewModel(){
    }

    public LiveData<ArrayList<Note>> getNotes(){
        return NoteRepository.getInstance().getNotes();
    }

    public LiveData<ArrayList<Board>> getBoards() {
        return BoardRepository.getInstance().getBoards();
    }
}
