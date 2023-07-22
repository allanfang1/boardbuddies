package com.allan.boardbuddies.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allan.boardbuddies.R;
import com.allan.boardbuddies.views.CanvasView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CanvasActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextContent;
    private File filePath;
    private String localFilename;
    private String localTitle;
    private String localContent;
    private int localPosition;
    private CanvasView paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        Toolbar toolbar = findViewById(R.id.edit_canvas_toolbar);
        paint = findViewById(R.id.canvas_view);
        ViewTreeObserver viewTreeObserver = paint.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    paint.init(paint.getWidth(), paint.getHeight());
                }
            });
        }
        toolbar.setNavigationOnClickListener(v -> { finish();});
//        toolbar.setNavigationOnClickListener(v -> {
//            Intent resultIntent = new Intent();
//            if (localFilename == null){ //if there is no local file: saveTextNote()
//                resultIntent.putExtra("addedFilename", saveTextNote());
//            } else if (!localTitle.equals(editTextTitle.getText().toString()) || !localContent.equals(editTextContent.getText().toString())) { //if local file has been changed
//                resultIntent.putExtra("addedFilename", saveTextNote());
//                resultIntent.putExtra("deletedPosition", localPosition);
//                getApplicationContext().deleteFile(localFilename);
//            }
//            setResult(RESULT_OK, resultIntent);
//            finish();
//        });
//
//        if (getIntent().getExtras() != null){
//            filePath = (File)getIntent().getExtras().get("FILEPATH");
//            localFilename = getIntent().getExtras().getString("FILENAME");
//            localTitle = getIntent().getExtras().getString("TITLE");
//            localContent = getIntent().getExtras().getString("CONTENT");
//            localPosition = getIntent().getExtras().getInt("POSITION");
//            editTextTitle.setText(localTitle);
//            editTextContent.setText(localContent);
//        }
//    }
//
//    private String saveTextNote(){
//        String title = editTextTitle.getText().toString();
//        String content = editTextContent.getText().toString();
//        if (!title.trim().isEmpty() || !content.trim().isEmpty()){
//            String fileName = System.currentTimeMillis() + ".json";
//            try {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("title", title);
//                jsonObject.put("content", content);
//                String jsonString = jsonObject.toString();
//
//                File file = new File(filePath, fileName);
//                FileWriter fileWriter = new FileWriter(file);
//                fileWriter.write(jsonString);
//                fileWriter.close();
//                return fileName;
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
    }
}