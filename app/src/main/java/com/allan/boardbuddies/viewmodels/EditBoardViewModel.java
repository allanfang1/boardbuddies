package com.allan.boardbuddies.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.allan.boardbuddies.models.Board;
import com.allan.boardbuddies.models.Stroke;
import com.allan.boardbuddies.models.TextBox;
import com.allan.boardbuddies.repositories.BoardRepository;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EditBoardViewModel extends ViewModel {
    private Board localBoard;
    private int position;
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Stroke>> strokes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TextBox>> texts = new MutableLiveData<>();

    public void setBoard(int position){
        this.position = position;
        this.localBoard = BoardRepository.getInstance().getBoard(position);
        this.title.setValue(localBoard.getTitle());
        this.strokes.setValue(new ArrayList<Stroke>(localBoard.getStrokes().stream().map(Stroke::new).collect(Collectors.toList())));
        this.texts.setValue(new ArrayList<TextBox>(localBoard.getTexts().stream().map(TextBox::new).collect(Collectors.toList())));
    }

    public LiveData<String> getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title.setValue(title);
    }

    public LiveData<ArrayList<Stroke>> getStrokes(){
        return strokes;
    }

    public LiveData<ArrayList<TextBox>> getTexts(){
        return texts;
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
    public void deleteBoard () {
        if (position > -1) {
            BoardRepository.getInstance().deleteBoard(position);
        }
    }
}