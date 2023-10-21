package com.allan.boardbuddies.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.allan.boardbuddies.Constants;
import com.allan.boardbuddies.R;
import com.allan.boardbuddies.viewmodels.EditBoardViewModel;
import com.allan.boardbuddies.views.CanvasView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class EditBoardFragment extends Fragment implements NavigationBarView.OnItemSelectedListener {
    private EditBoardViewModel editBoardViewModel;
    private TextView boardTitle;
    private CanvasView canvasView;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private ImageButton deleteButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_board, container, false);
        toolbar = view.findViewById(R.id.board_toolbar);
        boardTitle = view.findViewById(R.id.board_title);
        canvasView = view.findViewById(R.id.canvas_view);
        bottomNavigationView = view.findViewById(R.id.board_bottom_nav);
        deleteButton = view.findViewById(R.id.delete_icon);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editBoardViewModel = new ViewModelProvider(requireActivity()).get(EditBoardViewModel.class);
        editBoardViewModel.getTitle().observe(getViewLifecycleOwner(), title -> boardTitle.setText(title));

        ViewTreeObserver viewTreeObserver = canvasView.getViewTreeObserver(); // gaurentees view is laid out vs post runnable
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    canvasView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    canvasView.init(canvasView.getWidth(), canvasView.getHeight());
                }
            });
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBoardViewModel.deleteBoard();
                Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
            }
        });

        toolbar.setNavigationOnClickListener(v -> {
            editBoardViewModel.saveBoard(boardTitle.getText().toString(), canvasView.getStrokes(), canvasView.getTextBoxes());
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
        });

        if (savedInstanceState == null) {
            editBoardViewModel.setBoard(getArguments().getInt("position", -1));
        }
        boardTitle.setText(editBoardViewModel.getTitle().getValue());
        canvasView.setStrokes(editBoardViewModel.getStrokes().getValue());
        canvasView.setTexts(editBoardViewModel.getTexts().getValue());

        bottomNavigationView.setOnItemSelectedListener(this);

        getChildFragmentManager().setFragmentResultListener(Constants.TEXT_DIALOG_FRAGMENT_ADD_TEXT_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString(Constants.TEXT_DIALOG_FRAGMENT_ADDED_TEXT);
                if (!result.isEmpty()){
                    canvasView.newTextBox(result);
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_text:
                showDialog(TextboxDialogFragment.newInstance());
                break;
            case R.id.edit_canvas_title:
                showDialog(TitleDialogFragment.newInstance());
                break;
            default:
                return false;
        }
        return true;
    }

    private void showDialog(DialogFragment dialogFragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getChildFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");
    }
}
