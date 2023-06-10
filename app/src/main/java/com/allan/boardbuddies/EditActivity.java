package com.allan.boardbuddies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.edit_note_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            saveTextNote();
            onBackPressed();
        });

        View extraView = findViewById(R.id.extra_scrollspace);
        EditText editTextNoteContent = findViewById(R.id.edit_textnote_content);
        extraView.setOnClickListener(v -> editTextNoteContent.requestFocus());

    }

    private void saveTextNote(){
        EditText editText1 = findViewById(R.id.edit_textnote_title);
        EditText editText2 = findViewById(R.id.edit_textnote_content);
        String title = editText1.getText().toString();
        String content = editText2.getText().toString();
        if (!title.trim().isEmpty() || !content.trim().isEmpty()){
            String fileName = System.currentTimeMillis() + ".json";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", title);
                jsonObject.put("content", content);
                String jsonString = jsonObject.toString();

                File file = new File(getFilesDir(), fileName);
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jsonString);
                fileWriter.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

}