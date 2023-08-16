package com.allan.boardbuddies.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allan.boardbuddies.Constants;
import com.allan.boardbuddies.MemoListAdapter;
import com.allan.boardbuddies.MemoViewModel;
import com.allan.boardbuddies.Utilities;
import com.allan.boardbuddies.models.Note;
import com.allan.boardbuddies.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class NotesFragment extends Fragment implements MemoListAdapter.OnElementListener {
    private MemoListAdapter adapter;
    private File directory;
    private MemoViewModel memoViewModel;

    public NotesFragment() {        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        directory = new File(getActivity().getApplicationContext().getFilesDir(), Constants.NOTE_DIRECTORY_NAME);
        if (!directory.exists()){
            directory.mkdir();
        }
        memoViewModel = new ViewModelProvider(requireActivity()).get(MemoViewModel.class);
        loadNotes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MemoListAdapter<Note>(new NoteDiffCallback(), this){
            @Override
            protected void populateMemoViewHolder(MemoViewHolder holder, Note element) {
                holder.titleTextView.setText(element.getTitle());
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));
        return view;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){
        FloatingActionButton addNoteFab = view.findViewById(R.id.add_note_fab);
        addNoteFab.setOnClickListener(v -> {
            memoViewModel.setSelectedNote(-1);
            Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.action_to_editNoteFragment);
        });
        memoViewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
            adapter.submitList(notes);
        });
    }

    public void onElementClick(int position){
        memoViewModel.setSelectedNote(position);
        Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.action_to_editNoteFragment);
    }

    private void loadNotes(){
        File[] files = directory.listFiles();
        memoViewModel.clearNotes();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName).reversed());
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    String fileString = Utilities.getFileAsString(file);
                    try {
                        JSONObject jsonObject = new JSONObject(fileString);
                        memoViewModel.addNote(-1, new Note((String) jsonObject.get("title"), (String) jsonObject.get("content"), file.getName()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class NoteDiffCallback<Note> extends DiffUtil.ItemCallback<Note> {
        @Override
        public boolean areItemsTheSame(Note oldItem, Note newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(Note oldItem, Note newItem) {
            return oldItem.equals(newItem);
        }
    }
}