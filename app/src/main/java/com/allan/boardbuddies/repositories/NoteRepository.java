package com.allan.boardbuddies.repositories;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.allan.boardbuddies.Constants;
import com.allan.boardbuddies.Utilities;
import com.allan.boardbuddies.models.Note;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class NoteRepository {
    private static volatile NoteRepository noteRepository;

    private final MutableLiveData<ArrayList<Note>> notes = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedPosition = new MutableLiveData<>();

    private File directory;

    public NoteRepository(){
    }

    public void initNoteRepository(Context context){
        directory = new File(context.getFilesDir(), Constants.NOTE_DIRECTORY_NAME);
        if (!directory.exists()){
            directory.mkdir();
        }
        loadNotes(directory);
    }

    public static NoteRepository getInstance() {
        if (noteRepository == null) {
            synchronized (NoteRepository.class) {
                if (noteRepository == null) {
                    noteRepository = new NoteRepository();
                }
            }
        }
        return noteRepository;
    }

    private void loadNotes(File directory){
        File[] files = directory.listFiles();
        ArrayList<Note> temp = new ArrayList<>();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName).reversed());
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    String fileString = Utilities.getFileAsString(file);
                    temp.add(new Gson().fromJson(fileString, Note.class));
                }
            }
            notes.setValue(temp);
        }
    }

    public LiveData<ArrayList<Note>> getNotes(){
        return notes;
    }

    public LiveData<Integer> getSelectedPosition(){
        return selectedPosition;
    }

    @Nullable
    public Note getSelectedNote(){
        if (selectedPosition.getValue() == null){
            return null;
        } else if (selectedPosition.getValue() == -1){
            return new Note();
        }
        return notes.getValue().get(selectedPosition.getValue());
    }

    public void setSelectedPosition(int position){
        selectedPosition.setValue(position);
    }

    public void deleteNote(int position){
        new File(directory, notes.getValue().get(position).getFileName()).delete();
        notes.getValue().remove(position);
    }

    public void addNote(int position, Note note){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        if (Utilities.writeFile(new File(directory, note.getFileName()), gson.toJson(note))){
            notes.getValue().add(position, note);
        }
    }
}
