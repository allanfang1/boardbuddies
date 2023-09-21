package com.allan.boardbuddies.viewmodels;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;

import com.allan.boardbuddies.models.Board;
import com.allan.boardbuddies.models.Note;
import com.allan.boardbuddies.models.Stroke;
import com.allan.boardbuddies.models.TextBox;
import com.allan.boardbuddies.repositories.BoardRepository;
import com.allan.boardbuddies.repositories.NoteRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class EditBoardViewModel extends ViewModel {
    private Board localBoard;
    private int position;

    public EditBoardViewModel() {
    }

    public void setBoard(int position){
        this.position = position;
        this.localBoard = BoardRepository.getInstance().getBoard(position);
    }
    public void saveBoard(String title, ArrayList<Stroke> strokes, ArrayList<TextBox> texts) {
        if (localBoard.getFileName() == null || !localBoard.getTitle().equals(title) || !localBoard.getStrokes().equals(strokes) || !localBoard.getTexts().equals(texts)) { //if there is no local file: saveTextNote()
            if (!title.trim().isEmpty() || !strokes.isEmpty() || !texts.isEmpty()) {
                String fileName = System.currentTimeMillis() + ".json";
                if (localBoard.getFileName() != null) {
                    this.deleteBoard();
                }
                BoardRepository.getInstance().addBoard(0, new Board(title, strokes, texts, fileName));
            }
        }
    }
    private void deleteBoard () {
        if (position > -1) {
            BoardRepository.getInstance().deleteNote(position);
        }
    }

    public Board getLocalBoard() {
        return localBoard;
    }
}