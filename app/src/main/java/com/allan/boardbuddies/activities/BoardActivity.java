package com.allan.boardbuddies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.allan.boardbuddies.Constants;
import com.allan.boardbuddies.R;
import com.allan.boardbuddies.Utilities;
import com.allan.boardbuddies.fragments.TextDialogFragment;
import com.allan.boardbuddies.models.Board;
import com.allan.boardbuddies.models.Stroke;
import com.allan.boardbuddies.models.TextBox;
import com.allan.boardbuddies.views.CanvasView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private TextView boardTitle;
    private File filePath;
    private String localFilename;
    private Board localBoard;
    private int localPosition;
    private BottomNavigationView bottomNavigationView;
    private CanvasView canvasView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Toolbar toolbar = findViewById(R.id.board_toolbar);
        boardTitle = findViewById(R.id.board_title);
        canvasView = findViewById(R.id.canvas_view);

        ViewTreeObserver viewTreeObserver = canvasView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    canvasView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    canvasView.init(canvasView.getWidth(), canvasView.getHeight());
                }
            });
        }

        filePath = (File)getIntent().getExtras().get("FILEPATH");
        if (getIntent().getExtras().get("FILENAME") != null){
            localPosition = getIntent().getExtras().getInt("POSITION");
            localFilename = getIntent().getExtras().getString("FILENAME");
            localBoard = new Gson().fromJson(Utilities.getFileAsString(new File(filePath, localFilename)), Board.class);
            boardTitle.setText(localBoard.getTitle());
            Board tempBoard = new Gson().fromJson(Utilities.getFileAsString(new File(filePath, localFilename)), Board.class);
            canvasView.setStrokes(tempBoard.getStrokes());
            canvasView.setTexts(tempBoard.getTexts());
        } else {
            localBoard = new Board();
            canvasView.setStrokes(new ArrayList<>());
            canvasView.setTexts(new ArrayList<>());
        }

        bottomNavigationView = findViewById(R.id.board_bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(this);
        getSupportFragmentManager().setFragmentResultListener(Constants.TEXT_DIALOG_FRAGMENT_ADD_TEXT_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString(Constants.TEXT_DIALOG_FRAGMENT_ADDED_TEXT);
                String resultKey = bundle.getString("resultKey");
                if ("add_text".equals(resultKey) && !result.isEmpty()){
                    canvasView.newTextBox(result);
                } else if ("edit_canvas_title".equals(resultKey)){
                    boardTitle.setText(result);
                }
            }
        });

        toolbar.setNavigationOnClickListener(v -> {
            Intent resultIntent = new Intent();
            if (localFilename == null){ //if there is no local file: saveTextNote()
                resultIntent.putExtra("addedFilename", saveTextNote());
            } else if (!localBoard.getTitle().equals(boardTitle.getText().toString()) || !localBoard.getStrokes().equals(canvasView.getStrokes()) || !localBoard.getTexts().equals(canvasView.getTextBoxes())) { //if local file has been changed
                resultIntent.putExtra("addedFilename", saveTextNote());
                resultIntent.putExtra("deletedPosition", localPosition);
                new File(filePath, localFilename).delete();
            }
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_text:
                showDialog("add_text");
                break;
            case R.id.edit_canvas_title:
                showDialog("edit_canvas_title");
                break;
            default:
                return false;
        }
        return true;
    }

    private void showDialog(String resultKey) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment newFragment = TextDialogFragment.newInstance("", resultKey);
        newFragment.show(ft, "dialog");
    }

    private @Nullable String saveTextNote() {
        String title = boardTitle.getText().toString();
        ArrayList<Stroke> contentStroke = canvasView.getStrokes();
        ArrayList<TextBox> contentText = canvasView.getTextBoxes();
        if (!title.trim().isEmpty() || !contentStroke.isEmpty() || !contentText.isEmpty()){
            String fileName = System.currentTimeMillis() + ".json";
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            if (Utilities.writeFile(new File(filePath, fileName), gson.toJson(new Board(title, contentStroke, contentText, fileName)))){
                return fileName;
            }
        }
        return null;
    }
}