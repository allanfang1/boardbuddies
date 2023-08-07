package com.allan.boardbuddies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// binds data to views displayed by RecyclerView
public abstract class MemoAdapter<T> extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    private ArrayList<T> elements;
    private OnElementListener onElementListener;
    //constructor
    public MemoAdapter(ArrayList<T> elements, OnElementListener onElementListener){
        this.elements = elements;
        this.onElementListener = onElementListener;
    }

    //Creates ViewHolder for view (note_item.xml), converts xml to View object, this method runs when no ViewHolders are available
    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new MemoViewHolder(itemView, this.onElementListener);
    }

    // Prepare view to be added to layout, called after ViewHolder obtained
    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position){
        populateMemoViewHolder(holder, elements.get(position));
    }

    protected abstract void populateMemoViewHolder(MemoViewHolder holder, T element);

    //item count
    @Override
    public int getItemCount() {
        return elements.size();
    }

    // Allows access to each list item view without needing to lookup every time
    public static class MemoViewHolder extends RecyclerView.ViewHolder{
        public TextView titleTextView;

        public MemoViewHolder(View itemView, OnElementListener onElementListener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title_text);
            itemView.setOnClickListener(view -> onElementListener.onElementClick(getBindingAdapterPosition()));
        }

    }

    public interface OnElementListener {
        void onElementClick(int position);
    }
}
