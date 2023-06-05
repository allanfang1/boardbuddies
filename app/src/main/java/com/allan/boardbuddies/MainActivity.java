package com.allan.boardbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import com.allan.boardbuddies.fragments.NotesFragment;
import com.allan.boardbuddies.fragments.SharedFragment;
import com.allan.boardbuddies.fragments.TrashFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity
        implements NavigationBarView
        .OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    /** On activity creation
    * Param passes data between states of activity (e.g. orientation change/running in background)
    * R for res folder, auto-generated resource IDs from activity_main
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_notes);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.menu_notes:
                fragment = new NotesFragment();
                break;
            case R.id.menu_shared:
                fragment = new SharedFragment();
                break;
            case R.id.menu_trash:
                fragment = new TrashFragment();
                break;
            default:
                return false;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        return true;
    }
}