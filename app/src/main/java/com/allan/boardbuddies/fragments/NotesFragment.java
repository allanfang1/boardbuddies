package com.allan.boardbuddies.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allan.boardbuddies.Note;
import com.allan.boardbuddies.NoteAdapter;
import com.allan.boardbuddies.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NotesFragment extends Fragment {
    private ArrayList<Note> notes = new ArrayList<Note>();
    private RecyclerView recyclerView;
    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notes.add(new Note("hello", "bob"));
        notes.add(new Note("my", "bob"));
        notes.add(new Note("friend", "bob"));
        notes.add(new Note("Kaitlyn", "bob"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        NoteAdapter adapter = new NoteAdapter(notes);
        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }
}