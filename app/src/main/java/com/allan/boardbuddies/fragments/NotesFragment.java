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

import com.allan.boardbuddies.MemoListAdapter;
import com.allan.boardbuddies.viewmodels.MemoListViewModel;
import com.allan.boardbuddies.models.Note;
import com.allan.boardbuddies.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesFragment extends Fragment implements MemoListAdapter.OnMemoClickListener {
    private MemoListAdapter adapter;
    private MemoListViewModel memoListViewModel;

    public NotesFragment() {}        // Required empty public constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memoListViewModel = new ViewModelProvider(requireActivity()).get(MemoListViewModel.class);
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
            onMemoClick(-1);
        });
        memoListViewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
            adapter.submitList(notes);
        });
    }

    public void onMemoClick(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.action_to_editNoteFragment, bundle);
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