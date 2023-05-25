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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
    private FloatingActionButton addNoteFab;
    private NoteAdapter adapter;
    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            notes = (ArrayList<Note>)bundle.getSerializable("myNotes");
            // Handle the retrieved data as needed
        }
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new NoteAdapter(notes);
        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){
        addNoteFab = view.findViewById(R.id.add_note_fab);
        addNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notes.add(new Note("yay", "bob"));
                adapter.notifyDataSetChanged();
            }
        });
    }
}