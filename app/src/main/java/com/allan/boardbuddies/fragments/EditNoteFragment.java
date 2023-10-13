package com.allan.boardbuddies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.allan.boardbuddies.R;
import com.allan.boardbuddies.viewmodels.EditNoteViewModel;

public class EditNoteFragment extends Fragment {
    private EditText editTextTitle;
    private EditText editTextContent;
    private Toolbar toolbar;
    private View extraView;
    private EditNoteViewModel editNoteViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        toolbar = view.findViewById(R.id.note_toolbar);
        extraView = view.findViewById(R.id.extra_scrollspace);
        editTextTitle = view.findViewById(R.id.edit_text_note_title);
        editTextContent = view.findViewById(R.id.edit_text_note_content);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editNoteViewModel = new ViewModelProvider(requireActivity()).get(EditNoteViewModel.class);

        toolbar.setNavigationOnClickListener(v -> {
            editNoteViewModel.saveNote(editTextTitle.getText().toString(), editTextContent.getText().toString());
            Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack();
        });

        editNoteViewModel.setNote(getArguments().getInt("position", -1));
        editTextTitle.setText(editNoteViewModel.getLocalNote().getTitle());
        editTextContent.setText(editNoteViewModel.getLocalNote().getContent());

        extraView.setOnClickListener(v -> {
            editTextContent.requestFocus();
            editTextContent.setSelection(editTextContent.getText().length());
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTextContent, InputMethodManager.SHOW_IMPLICIT);
        });
    }
}