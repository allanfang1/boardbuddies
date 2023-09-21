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
import com.allan.boardbuddies.viewmodels.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BoardsFragment extends Fragment implements MemoListAdapter.OnMemoClickListener {
    private MemoListAdapter adapter;
    private NoteViewModel memoViewModel;
//    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//                        @Nullable String addedFilename = data.getStringExtra("addedFilename");
//                        int deletedPosition = data.getIntExtra("deletedPosition", -1);
//                        if (deletedPosition != -1){
//                            boards.remove(deletedPosition);
//                            adapter.notifyItemRemoved(deletedPosition);
//                        }
//                        if (addedFilename != null) {
//                            loadSingle(addedFilename, 0);
//                            adapter.notifyItemInserted(0);
//                        }
//                    }
//                }
//            });

    public BoardsFragment() {        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memoViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
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
        addNoteFab.setOnClickListener(v -> {
            onMemoClick(-1);
        });
        memoViewModel.getBoards().observe(getViewLifecycleOwner(), boards ->{
            adapter.submitList(boards);
        });
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