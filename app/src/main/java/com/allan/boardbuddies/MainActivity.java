package com.allan.boardbuddies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Note> notes = new ArrayList<Note>();
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notes.add(new Note("hello", "bob"));
        notes.add(new Note("my", "bob"));
        notes.add(new Note("friend", "bob"));
        notes.add(new Note("Kaitlyn", "bob"));

        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteAdapter adapter = new NoteAdapter(notes);
        recyclerView.setAdapter(adapter);

    }
}