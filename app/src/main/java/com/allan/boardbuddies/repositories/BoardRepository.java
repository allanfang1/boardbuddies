package com.allan.boardbuddies.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.allan.boardbuddies.Utilities;
import com.allan.boardbuddies.models.Board;
import com.allan.boardbuddies.models.Note;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BoardRepository {
    private static volatile BoardRepository boardRepository;

    private final MutableLiveData<ArrayList<Board>> boards = new MutableLiveData<>();

    private File directory;

    public BoardRepository(){
    }

    public void initBoardRepository(File file){
        directory = file;
        if (!directory.exists()){
            directory.mkdir();
        }
        loadNotes(directory);
    }

    private void loadNotes(File directory){
        File[] files = directory.listFiles();
        ArrayList<Board> temp = new ArrayList<>();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName).reversed());
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    String fileString = Utilities.getFileAsString(file);
                    temp.add(new Gson().fromJson(fileString, Board.class));
                }
            }
            boards.setValue(temp);
        }
    }

    public static BoardRepository getInstance() {
        if (boardRepository == null) {
            synchronized (NoteRepository.class) {
                if (boardRepository == null) {
                    boardRepository = new BoardRepository();
                }
            }
        }
        return boardRepository;
    }

    public LiveData<ArrayList<Board>> getBoards() {
        return boards;
    }

    public void addBoard(int position, Board board) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        if (Utilities.writeFile(new File(directory, board.getFileName()), gson.toJson(board))){
            boards.getValue().add(position, board);
        }
    }

    public void deleteNote(int position) {
        new File(directory, boards.getValue().get(position).getFileName()).delete();
        boards.getValue().remove(position);
    }

    public Board getBoard(int position) {
        if (position == -1){
            return new Board();
        }
        return boards.getValue().get(position);
    }
}
