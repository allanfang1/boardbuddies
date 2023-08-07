package com.allan.boardbuddies.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allan.boardbuddies.R;
import com.allan.boardbuddies.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class NoteActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextContent;
    private File filePath;
    private String localFilename;
    private String localTitle;
    private String localContent;
    private int localPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.note_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            Intent resultIntent = new Intent();
            if (localFilename == null){ //if there is no local file: saveTextNote()
                resultIntent.putExtra("addedFilename", saveTextNote());
            } else if (!localTitle.equals(editTextTitle.getText().toString()) || !localContent.equals(editTextContent.getText().toString())) { //if local file has been changed
                resultIntent.putExtra("addedFilename", saveTextNote());
                resultIntent.putExtra("deletedPosition", localPosition);
                new File(filePath, localFilename).delete();
            }
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        View extraView = findViewById(R.id.extra_scrollspace);
        editTextTitle = findViewById(R.id.edit_text_note_title);
        editTextContent = findViewById(R.id.edit_text_note_content);

        if (getIntent().getExtras() != null){
            filePath = (File)getIntent().getExtras().get("FILEPATH");
            localFilename = getIntent().getExtras().getString("FILENAME");
            localTitle = getIntent().getExtras().getString("TITLE");
            localContent = getIntent().getExtras().getString("CONTENT");
            localPosition = getIntent().getExtras().getInt("POSITION");
            editTextTitle.setText(localTitle);
            editTextContent.setText(localContent);
        }
        extraView.setOnClickListener(v -> {
            editTextContent.requestFocus();
            editTextContent.setSelection(editTextContent.getText().length());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTextContent, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    private @Nullable String saveTextNote(){
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();
        if (!title.trim().isEmpty() || !content.trim().isEmpty()){
            String fileName = System.currentTimeMillis() + ".json";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", title);
                jsonObject.put("content", content);
                String jsonString = jsonObject.toString();

                File file = new File(filePath, fileName);

                if (Utilities.writeFile(file, jsonString)){
                    return fileName;
                };
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}