package com.allan.boardbuddies.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.boardbuddies.MemoListAdapter;
import com.allan.boardbuddies.R;
import com.allan.boardbuddies.models.Board;
import com.allan.boardbuddies.viewmodels.MemoListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BoardsFragment extends Fragment implements MemoListAdapter.OnMemoClickListener {
    private MemoListAdapter adapter;
    private MemoListViewModel memoListViewModel;

    public BoardsFragment() {        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memoListViewModel = new ViewModelProvider(requireActivity()).get(MemoListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MemoListAdapter<Board>(new BoardDiffCallback(), this){
            @Override
            protected void populateMemoViewHolder(MemoViewHolder holder, Board element) {
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
        addNoteFab.setOnClickListener(v -> onMemoClick(-1));
        memoListViewModel.getBoards().observe(getViewLifecycleOwner(), boards -> adapter.submitList(boards));
    }

    public void onMemoClick(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.action_to_editBoardFragment, bundle);
    }

    private static class BoardDiffCallback<Board> extends DiffUtil.ItemCallback<Board> {
        @Override
        public boolean areItemsTheSame(Board oldItem, Board newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(Board oldItem, Board newItem) {
            return oldItem.equals(newItem);
        }
    }

}