package com.allan.boardbuddies.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allan.boardbuddies.EditActivity;
import com.allan.boardbuddies.Note;
import com.allan.boardbuddies.NoteAdapter;
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

public class NotesFragment extends Fragment implements NoteAdapter.OnNoteListener {
    private ArrayList<Note> notes = new ArrayList<>();
    private NoteAdapter adapter;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadNotes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(notes, this);
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
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
        adapter.notifyDataSetChanged();
    }

    public void onNoteClick(int position){
        Intent intent = new Intent(requireContext(), EditActivity.class);
        intent.putExtra("TITLE", notes.get(position).getTitle());
        intent.putExtra("CONTENT", notes.get(position).getContent());
        intent.putExtra("FILENAME", notes.get(position).getName());
        requireContext().startActivity(intent);
    }

    private void loadNotes(){
        File[] files = getActivity().getApplicationContext().getFilesDir().listFiles();
        notes.clear();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName).reversed());
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    try {
                        // Read the file contents
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        reader.close();

                        // Parse the JSON content
                        String jsonContent = stringBuilder.toString();
                        JSONObject jsonObject = new JSONObject(jsonContent);

                        // Add the parsed JSON content to the list
                        notes.add(new Note((String) jsonObject.get("title"), (String) jsonObject.get("content"), file.getName()));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}