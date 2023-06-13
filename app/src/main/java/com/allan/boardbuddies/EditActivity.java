package com.allan.boardbuddies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextContent;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.edit_note_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            //if there no extras or the extras are changed, saveTextNote()
            // delete old text note only if there are extra and extras are changed
            if (extras == null){
                saveTextNote();
            } else if (!extras.getString("TITLE").equals(editTextTitle.getText().toString()) || !extras.getString("CONTENT").equals(editTextContent.getText().toString())) {
                saveTextNote();
                getApplicationContext().deleteFile(extras.getString("NAME"));
            }
            onBackPressed();
        });

        View extraView = findViewById(R.id.extra_scrollspace);
        editTextTitle = findViewById(R.id.edit_textnote_title);
        editTextContent = findViewById(R.id.edit_textnote_content);
        extras = getIntent().getExtras();
        if (extras != null){
            editTextTitle.setText(extras.getString("TITLE"));
            editTextContent.setText(extras.getString("CONTENT"));
        }
        extraView.setOnClickListener(v -> {
            editTextContent.requestFocus();
            editTextContent.setSelection(editTextContent.getText().length());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTextContent, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    private void saveTextNote(){
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();
        if (!title.trim().isEmpty() || !content.trim().isEmpty()){
            String fileName = System.currentTimeMillis() + ".json";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", title);
                jsonObject.put("content", content);
                String jsonString = jsonObject.toString();

                File file = new File(getApplicationContext().getFilesDir(), fileName);
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jsonString);
                fileWriter.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

}