package com.allan.boardbuddies.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.allan.boardbuddies.R;


public class MainActivity extends AppCompatActivity{

    /** On activity creation
    * Param passes data between states of activity (e.g. orientation change/running in background)
    * R for res folder, auto-generated resource IDs from activity_main
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        NavController navController = navHostFragment.getNavController();

    }
}