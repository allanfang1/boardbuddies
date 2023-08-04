package com.allan.boardbuddies.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.allan.boardbuddies.R;
import com.allan.boardbuddies.Utilities;
import com.allan.boardbuddies.Constants;
import com.allan.boardbuddies.fragments.TextDialogFragment;
import com.allan.boardbuddies.models.Board;
import com.allan.boardbuddies.models.Stroke;
import com.allan.boardbuddies.models.TextBox;
import com.allan.boardbuddies.views.CanvasView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CanvasActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private TextView canvasTitle;
    private EditText editTextContent;
    private File filePath;
    private String localFilename;
    private String localTitle;
    private Board localBoard;
    private ArrayList<Stroke> localStrokes;
    private ArrayList<TextBox> localTexts;
    private int localPosition;
    private BottomNavigationView bottomNavigationView;
    private CanvasView canvasView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        Toolbar toolbar = findViewById(R.id.edit_canvas_toolbar);
        canvasTitle = findViewById(R.id.canvas_title);
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
            localTitle = localBoard.getTitle();
            paint.setStrokes(localBoard.getStrokes());
            paint.setTexts(localBoard.getTexts());
            canvasTitle.setText(localBoard.getTitle());
        }

        bottomNavigationView = findViewById(R.id.canvas_bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(this);
        getSupportFragmentManager().setFragmentResultListener(Constants.TEXT_DIALOG_FRAGMENT_ADD_TEXT_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString(Constants.TEXT_DIALOG_FRAGMENT_ADDED_TEXT);
                String resultKey = bundle.getString("resultKey");
                if ("add_text".equals(resultKey)){
                    canvasView.newTextBox(result);
                } else if ("edit_canvas_title".equals(resultKey)){
                    canvasTitle.setText(result);
                }
            }
        });

        toolbar.setNavigationOnClickListener(v -> {
            Intent resultIntent = new Intent();
            if (localFilename == null){ //if there is no local file: saveTextNote()
                resultIntent.putExtra("addedFilename", saveTextNote());
            } else if (!localTitle.equals(canvasTitle.getText().toString()) || false) { //if local file has been changed
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

    private String saveTextNote(){
        String title = canvasTitle.getText().toString();
        ArrayList<Stroke> contentStroke = paint.getStrokes();
        ArrayList<TextBox> contentText = paint.getTextBoxes();
        if (!title.trim().isEmpty() || !contentStroke.isEmpty() || !contentText.isEmpty()){
            String fileName = System.currentTimeMillis() + ".json";
            try {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                Board newBoard = new Board(title, contentStroke, contentText, fileName);
                String jsonString = gson.toJson(newBoard);
                File file = new File(filePath, fileName);

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jsonString);
                fileWriter.close();

                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}