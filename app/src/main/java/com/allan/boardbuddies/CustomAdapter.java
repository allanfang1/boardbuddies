package com.allan.boardbuddies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// binds data to views displayed by RecyclerView
public abstract class CustomAdapter<T> extends RecyclerView.Adapter<CustomAdapter.ElementViewHolder> {
    private ArrayList<T> elements;
    private OnElementListener onElementListener;
    //constructor
    public CustomAdapter(ArrayList<T> elements, OnElementListener onElementListener){
        this.elements = elements;
        this.onElementListener = onElementListener;
    }

    //Creates ViewHolder for view (note_item.xml), converts xml to View object, this method runs when no ViewHolders are available
    @NonNull
    @Override
    public ElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ElementViewHolder(itemView, this.onElementListener);
    }

    // Prepare view to be added to layout, called after ViewHolder obtained
    @Override
    public void onBindViewHolder(@NonNull ElementViewHolder holder, int position){
        populateViewHolder(holder, elements.get(position));
    }

    protected abstract void populateViewHolder(ElementViewHolder holder, T element);

    //item count
    @Override
    public int getItemCount() {
        return elements.size();
    }

    // Allows access to each list item view without needing to lookup every time
    public static class ElementViewHolder extends RecyclerView.ViewHolder{
        public TextView titleTextView;

        public ElementViewHolder(View itemView, OnElementListener onElementListener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.note_title_text);
            itemView.setOnClickListener(view -> onElementListener.onElementClick(getBindingAdapterPosition()));
        }

    }

    public interface OnElementListener {
        void onElementClick(int position);
    }
}
