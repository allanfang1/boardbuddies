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
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ScrollView scrollView = findViewById(R.id.noteScrollview);
        EditText editTextNoteContent = findViewById(R.id.editTextNoteContent);
        scrollView.setOnClickListener(v -> editTextNoteContent.requestFocus());

    }
}