package com.allan.boardbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

// RecyclerView imports
import androidx.recyclerview.widget.RecyclerView;

import com.allan.boardbuddies.fragments.NotesFragment;
import com.allan.boardbuddies.fragments.SharedFragment;
import com.allan.boardbuddies.fragments.TrashFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    // Array of Note objects
    private RecyclerView recyclerView;
    /** On activity creation
    * Param passes data between states of activity (e.g. orientation change/running in background)
    * R for res folder, auto-generated resource IDs from activity_main
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_notes);
    }
    NotesFragment notesFragment = new NotesFragment();
    SharedFragment sharedFragment = new SharedFragment();
    TrashFragment trashFragment = new TrashFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notes:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, notesFragment)
                        .commit();
                return true;

            case R.id.menu_shared:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, sharedFragment)
                        .commit();
                return true;

            case R.id.menu_trash:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, trashFragment)
                        .commit();
                return true;
        }
        return false;
    }
}