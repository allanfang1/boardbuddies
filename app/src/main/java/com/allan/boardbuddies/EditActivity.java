package com.allan.boardbuddies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_note_toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        View extraView = findViewById(R.id.extra_scrollspace);
        EditText editTextNoteContent = findViewById(R.id.edit_textnote_content);
        extraView.setOnClickListener(v -> editTextNoteContent.requestFocus());

    }
}