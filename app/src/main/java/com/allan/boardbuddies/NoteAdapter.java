package com.allan.boardbuddies;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.allan.boardbuddies.models.Note;

// binds data to views displayed by RecyclerView
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note> notes;
    private OnNoteListener onNoteListener;
    //constructor
    public NoteAdapter(ArrayList<Note> notes, OnNoteListener onNoteListener){
        this.notes = notes;
        this.onNoteListener = onNoteListener;
    }

    //Creates ViewHolder for view (note_item.xml), converts xml to View object, this method runs when no ViewHolders are available
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(itemView, this.onNoteListener);
    }

    // Prepare view to be added to layout, called after ViewHolder obtained
    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position){
        Note note = notes.get(position);
        holder.titleTextView.setText(note.getTitle());
    }

    //item count
    @Override
    public int getItemCount() {
        return notes.size();
    }

    // Allows access to each list item view without needing to lookup every time
    public static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;

        public NoteViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title_text);
            itemView.setOnClickListener(view -> onNoteListener.onNoteClick(getBindingAdapterPosition()));
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
