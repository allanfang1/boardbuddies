package com.allan.boardbuddies.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allan.boardbuddies.CustomAdapter;
import com.allan.boardbuddies.Utilities;
import com.allan.boardbuddies.activities.EditActivity;
import com.allan.boardbuddies.models.Note;
import com.allan.boardbuddies.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class NotesFragment extends Fragment implements CustomAdapter.OnElementListener {
    private ArrayList<Note> notes = new ArrayList<>();
    private CustomAdapter adapter;
    private File directory;
    ActivityResultLauncher<Intent> noteLauncher = registerForActivityResult(new StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String addedFilename = data.getStringExtra("addedFilename");
                        int deletedPosition = data.getIntExtra("deletedPosition", -1);
                        if (deletedPosition != -1){
                            notes.remove(deletedPosition);
                            adapter.notifyItemRemoved(deletedPosition);
                        }
                        if (addedFilename != null) {
                            loadSingle(addedFilename, 0);
                            adapter.notifyItemInserted(0);
                        }
                    }
                }
            });

    public NotesFragment() {        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        directory = new File(getActivity().getApplicationContext().getFilesDir(), "notes");
        if (!directory.exists()){
            directory.mkdir();
        }
        loadNotes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter<Note>(notes, this){
            @Override
            protected void populateViewHolder(ElementViewHolder holder, Note element) {
                holder.titleTextView.setText(element.getTitle());
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){
        FloatingActionButton addNoteFab = view.findViewById(R.id.add_note_fab);
        addNoteFab.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditActivity.class);
            intent.putExtra("FILEPATH", directory);
            noteLauncher.launch(intent);
        });
    }

    public void onElementClick(int position){
        Intent intent = new Intent(requireContext(), EditActivity.class);
        intent.putExtra("TITLE", notes.get(position).getTitle());
        intent.putExtra("CONTENT", notes.get(position).getContent());
        intent.putExtra("FILENAME", notes.get(position).getFileName());
        intent.putExtra("FILEPATH", directory);
        intent.putExtra("POSITION", position);
        noteLauncher.launch(intent);
    }

    private void loadSingle(String filename, int position){
        File file = new File(directory, filename);
        String fileString = Utilities.getFileAsString(file);
        try {
            JSONObject jsonObject = new JSONObject(fileString);
            notes.add(position, new Note((String) jsonObject.get("title"), (String) jsonObject.get("content"), file.getName()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadNotes(){
        File[] files = directory.listFiles();
        notes.clear();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName).reversed());
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    String fileString = Utilities.getFileAsString(file);
                    try {
                        JSONObject jsonObject = new JSONObject(fileString);
                        notes.add(new Note((String) jsonObject.get("title"), (String) jsonObject.get("content"), file.getName()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}