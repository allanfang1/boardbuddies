package com.allan.boardbuddies.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.allan.boardbuddies.fragments.TextDialogFragment;
import com.allan.boardbuddies.models.Stroke;
import com.allan.boardbuddies.models.TextBox;
import com.allan.boardbuddies.viewmodels.EditBoardViewModel;
import com.allan.boardbuddies.views.CanvasView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EditBoardFragment extends Fragment implements NavigationBarView.OnItemSelectedListener {
    private EditBoardViewModel editBoardViewModel;
    private TextView boardTitle;
    private CanvasView canvasView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_board, container, false);
        Toolbar toolbar = view.findViewById(R.id.board_toolbar);

        boardTitle = view.findViewById(R.id.board_title);
        canvasView = view.findViewById(R.id.canvas_view);
        editBoardViewModel = new ViewModelProvider(requireActivity()).get(EditBoardViewModel.class);

        ViewTreeObserver viewTreeObserver = canvasView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    canvasView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    canvasView.init(canvasView.getWidth(), canvasView.getHeight());
                }
            });
        }

        toolbar.setNavigationOnClickListener(v -> {
            editBoardViewModel.saveBoard(boardTitle.getText().toString(), canvasView.getStrokes(), canvasView.getTextBoxes());
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
        });

        editBoardViewModel.setBoard(getArguments().getInt("position", -1));
        boardTitle.setText(editBoardViewModel.getLocalBoard().getTitle());
        canvasView.setStrokes(new ArrayList<Stroke>(editBoardViewModel.getLocalBoard().getStrokes().stream().map(Stroke::new).collect(Collectors.toList())));
        canvasView.setTexts(new ArrayList<TextBox>(editBoardViewModel.getLocalBoard().getTexts().stream().map(TextBox::new).collect(Collectors.toList())));

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.board_bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(this);

        getChildFragmentManager().setFragmentResultListener(Constants.TEXT_DIALOG_FRAGMENT_ADD_TEXT_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString(Constants.TEXT_DIALOG_FRAGMENT_ADDED_TEXT);
                String resultKey = bundle.getString("resultKey");
                if ("add_text".equals(resultKey) && !result.isEmpty()){
                    canvasView.newTextBox(result);
                } else if ("edit_canvas_title".equals(resultKey)){
                    boardTitle.setText(result);
                }
            }
        });

        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_text:
                showDialog("add_text");
                break;
            case R.id.edit_canvas_title:
                showDialog("edit_canvas_title");
                break;
            default:
                return false;
        }
        return true;
    }

    private void showDialog(String resultKey) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getChildFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment newFragment = TextDialogFragment.newInstance("", resultKey);
        newFragment.show(ft, "dialog");
    }


}
