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

import com.allan.boardbuddies.Constants;
import com.allan.boardbuddies.R;

public class TextboxDialogFragment extends DialogFragment {

    private EditText captionText;

    public static TextboxDialogFragment newInstance() {
        TextboxDialogFragment f = new TextboxDialogFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        captionText = view.findViewById(R.id.dialog_input);
        captionText.requestFocus();
        captionText.setSelection(captionText.getText().length());
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(captionText, InputMethodManager.SHOW_IMPLICIT);

        Button button = view.findViewById(R.id.dialog_button);
        button.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putString(Constants.TEXT_DIALOG_FRAGMENT_ADDED_TEXT, String.valueOf(captionText.getText()));
            getParentFragmentManager().setFragmentResult(Constants.TEXT_DIALOG_FRAGMENT_ADD_TEXT_REQUEST_KEY, result);
            dismiss();
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
