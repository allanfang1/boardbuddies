package com.allan.boardbuddies.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.allan.boardbuddies.Constants;
import com.allan.boardbuddies.MemoViewModel;
import com.allan.boardbuddies.R;
import com.allan.boardbuddies.Utilities;
import com.allan.boardbuddies.models.Note;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class EditNoteFragment extends Fragment {
    private EditText editTextTitle;
    private EditText editTextContent;
    private File directory;
    private Note localNote;
    private MemoViewModel memoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        Toolbar toolbar = view.findViewById(R.id.note_toolbar);

        directory = new File(getActivity().getApplicationContext().getFilesDir(), Constants.NOTE_DIRECTORY_NAME);
        if (!directory.exists()){
            directory.mkdir();
        }
        memoViewModel = new ViewModelProvider(requireActivity()).get(MemoViewModel.class);

        toolbar.setNavigationOnClickListener(v -> {
            if (localNote.getFileName() == null){ //if there is no local file: saveTextNote()
                saveTextNote();
            } else if (!localNote.getTitle().equals(editTextTitle.getText().toString()) || !localNote.getContent().equals(editTextContent.getText().toString())) { //if local file has been changed
                memoViewModel.deleteNote(memoViewModel.getSelectedNotePosition().getValue());
                saveTextNote();
                new File(directory, localNote.getFileName()).delete();
            }
            System.out.println(Navigation.findNavController(requireActivity(), R.id.nav_host).popBackStack());
        });

        View extraView = view.findViewById(R.id.extra_scrollspace);
        editTextTitle = view.findViewById(R.id.edit_text_note_title);
        editTextContent = view.findViewById(R.id.edit_text_note_content);

        memoViewModel.getSelectedNotePosition().observe(getViewLifecycleOwner(), selectedNotePosition -> {
            if (selectedNotePosition == -1) {
                localNote = new Note();
            } else {
                localNote = memoViewModel.getNotes().getValue().get(selectedNotePosition);
            }
            editTextTitle.setText(localNote.getTitle());
            editTextContent.setText(localNote.getContent());
        });

        extraView.setOnClickListener(v -> {
            editTextContent.requestFocus();
            editTextContent.setSelection(editTextContent.getText().length());
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTextContent, InputMethodManager.SHOW_IMPLICIT);
        });
        return view;
    }

    private void saveTextNote(){
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();
        if (!title.trim().isEmpty() || !content.trim().isEmpty()){
            String fileName = System.currentTimeMillis() + ".json";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", title);
                jsonObject.put("content", content);

                if (Utilities.writeFile(new File(directory, fileName), jsonObject.toString())){
                    memoViewModel.addNote(0, new Note(title, content, fileName));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}