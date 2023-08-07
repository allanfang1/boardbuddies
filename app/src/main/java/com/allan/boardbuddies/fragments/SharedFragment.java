package com.allan.boardbuddies.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.boardbuddies.CustomAdapter;
import com.allan.boardbuddies.R;
import com.allan.boardbuddies.Utilities;
import com.allan.boardbuddies.activities.CanvasActivity;
import com.allan.boardbuddies.models.Board;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class SharedFragment extends Fragment implements CustomAdapter.OnElementListener {
    private ArrayList<Board> boards = new ArrayList<>();
    private CustomAdapter adapter;
    private File directory;
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String addedFilename = data.getStringExtra("addedFilename");
                        int deletedPosition = data.getIntExtra("deletedPosition", -1);
                        if (deletedPosition != -1){
                            boards.remove(deletedPosition);
                            adapter.notifyItemRemoved(deletedPosition);
                        }
                        if (addedFilename != null) {
                            loadSingle(addedFilename, 0);
                            adapter.notifyItemInserted(0);
                        }
                    }
                }
            });

    public SharedFragment() {        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        directory = new File(getActivity().getApplicationContext().getFilesDir(), "canvases");
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
        adapter = new CustomAdapter<Board>(boards, this){
            @Override
            protected void populateViewHolder(ElementViewHolder holder, Board element) {
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
            Intent intent = new Intent(v.getContext(), CanvasActivity.class);
            intent.putExtra("FILEPATH", directory);
            resultLauncher.launch(intent);
        });
    }

    public void onElementClick(int position){
        Intent intent = new Intent(requireContext(), CanvasActivity.class);
        intent.putExtra("FILENAME", boards.get(position).getFileName());
        intent.putExtra("FILEPATH", directory);
        intent.putExtra("POSITION", position);
        resultLauncher.launch(intent);
    }

    private void loadSingle(String filename, int position){
        String fileString = Utilities.getFileAsString(new File(directory, filename));
        boards.add(position, new Gson().fromJson(fileString, Board.class));
    }

    private void loadNotes(){
        File[] files = directory.listFiles();
        boards.clear();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName).reversed());
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    String fileString = Utilities.getFileAsString(file);
                    boards.add(new Gson().fromJson(fileString, Board.class));
                }
            }
        }
    }
}