package com.allan.boardbuddies;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
// RecyclerView imports
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {
    // Array of Note objects
    private ArrayList<Note> notes = new ArrayList<Note>();
    private RecyclerView recyclerView;
    /** On activity creation
    * Param passes data between states of activity (e.g. orientation change/running in background)
    * R for res folder, auto-generated resource IDs from activity_main
    */
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