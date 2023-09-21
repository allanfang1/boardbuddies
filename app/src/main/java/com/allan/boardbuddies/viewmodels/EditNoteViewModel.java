package com.allan.boardbuddies.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.allan.boardbuddies.models.Note;
import com.allan.boardbuddies.repositories.NoteRepository;

public class EditNoteViewModel extends ViewModel {
    private Note localNote;

    private int position;

    public EditNoteViewModel(){}

    public void setNote(int position){
        this.position = position;
        this.localNote = NoteRepository.getInstance().getNote(position);
    }

    public void saveNote(String title, String content){
        if (localNote.getFileName() == null || !localNote.getTitle().equals(title) || !localNote.getContent().equals(content)){ //if there is no local file: saveTextNote()
            if (!title.trim().isEmpty() || !content.trim().isEmpty()){
                String fileName = System.currentTimeMillis() + ".json";
                if (localNote.getFileName() != null) {
                    this.deleteNote();
                }
                NoteRepository.getInstance().addNote(0, new Note(title, content, fileName));
            }
        }
    }

    public Note getLocalNote() {
        return localNote;
    }

    public void deleteNote(){
        if (position > -1) {
            NoteRepository.getInstance().deleteNote(position);
        }
    }
}
