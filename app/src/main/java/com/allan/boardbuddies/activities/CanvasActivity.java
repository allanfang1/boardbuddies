package com.allan.boardbuddies.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.allan.boardbuddies.R;
import com.allan.boardbuddies.fragments.TextDialogFragment;
import com.allan.boardbuddies.views.CanvasView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CanvasActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private static String textDialogRequestKey = "TEXT_DIALOG_FRAGMENT_ADD_TEXT_REQUEST_KEY";
    private EditText editTextTitle;
    private EditText editTextContent;
    private File filePath;
    private String localFilename;
    private String localTitle;
    private String localContent;
    private int localPosition;
    private BottomNavigationView bottomNavigationView;
    private CanvasView canvasView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        Toolbar toolbar = findViewById(R.id.edit_canvas_toolbar);
        canvasView = findViewById(R.id.canvas_view);
        ViewTreeObserver viewTreeObserver = canvasView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    canvasView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    canvasView.init(canvasView.getWidth(), canvasView.getHeight());
                }
            });
        }
        bottomNavigationView = findViewById(R.id.canvas_bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(this);
        getSupportFragmentManager().setFragmentResultListener(textDialogRequestKey, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString("TEXT_DIALOG_FRAGMENT_ADDED_TEXT");
                canvasView.newTextBox(result);
            }
        });

        toolbar.setNavigationOnClickListener(v -> { finish();});
//        toolbar.setNavigationOnClickListener(v -> {
//            Intent resultIntent = new Intent();
//            if (localFilename == null){ //if there is no local file: saveTextNote()
//                resultIntent.putExtra("addedFilename", saveTextNote());
//            } else if (!localTitle.equals(editTextTitle.getText().toString()) || !localContent.equals(editTextContent.getText().toString())) { //if local file has been changed
//                resultIntent.putExtra("addedFilename", saveTextNote());
//                resultIntent.putExtra("deletedPosition", localPosition);
//                getApplicationContext().deleteFile(localFilename);
//            }
//            setResult(RESULT_OK, resultIntent);
//            finish();
//        });
//
//        if (getIntent().getExtras() != null){
//            filePath = (File)getIntent().getExtras().get("FILEPATH");
//            localFilename = getIntent().getExtras().getString("FILENAME");
//            localTitle = getIntent().getExtras().getString("TITLE");
//            localContent = getIntent().getExtras().getString("CONTENT");
//            localPosition = getIntent().getExtras().getInt("POSITION");
//            editTextTitle.setText(localTitle);
//            editTextContent.setText(localContent);
//        }
//    }
//
//    private String saveTextNote(){
//        String title = editTextTitle.getText().toString();
//        String content = editTextContent.getText().toString();
//        if (!title.trim().isEmpty() || !content.trim().isEmpty()){
//            String fileName = System.currentTimeMillis() + ".json";
//            try {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("title", title);
//                jsonObject.put("content", content);
//                String jsonString = jsonObject.toString();
//
//                File file = new File(filePath, fileName);
//                FileWriter fileWriter = new FileWriter(file);
//                fileWriter.write(jsonString);
//                fileWriter.close();
//                return fileName;
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_text:
                showDialog();
                break;
            default:
                return false;
        }
        return true;
    }

    private void showDialog() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = TextDialogFragment.newInstance("", textDialogRequestKey);
        newFragment.show(ft, "dialog");
    }
}