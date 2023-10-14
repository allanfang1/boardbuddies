package com.allan.boardbuddies.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.allan.boardbuddies.R;
import com.allan.boardbuddies.viewmodels.EditBoardViewModel;

public class TitleDialogFragment extends DialogFragment {

    private EditText captionText;
    private EditBoardViewModel editBoardViewModel;

    public static TitleDialogFragment newInstance() {
        TitleDialogFragment f = new TitleDialogFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editBoardViewModel = new ViewModelProvider(requireActivity()).get(EditBoardViewModel.class);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4D000000")));
        return inflater.inflate(R.layout.text_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        captionText = view.findViewById(R.id.dialog_input);
        captionText.setText(editBoardViewModel.getTitle().getValue());
        captionText.requestFocus();
        captionText.setSelection(captionText.getText().length());
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(captionText, InputMethodManager.SHOW_IMPLICIT);

        Button button = view.findViewById(R.id.dialog_button);
        button.setOnClickListener(v -> {
            editBoardViewModel.setTitle(String.valueOf(captionText.getText()));
            dismiss();
        });
    }
}
