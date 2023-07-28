package com.allan.boardbuddies.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.allan.boardbuddies.R;

public class TextDialogFragment extends DialogFragment {

    private EditText editText;

    public static TextDialogFragment newInstance(String in) {
        TextDialogFragment f = new TextDialogFragment();

        Bundle args = new Bundle();
        args.putString("text", in);
        f.setArguments(args);

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

        editText = view.findViewById(R.id.dialog_input);
        editText.setText(getArguments().getString("text"));
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        Button button = view.findViewById(R.id.dialog_button);
        button.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putString("bundleKey", String.valueOf(editText.getText()));
            getParentFragmentManager().setFragmentResult("requestKey", result);
            dismiss();
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
