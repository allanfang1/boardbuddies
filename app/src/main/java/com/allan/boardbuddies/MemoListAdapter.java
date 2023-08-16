package com.allan.boardbuddies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// binds data to views displayed by RecyclerView
public abstract class MemoListAdapter<T> extends ListAdapter<T, MemoListAdapter.MemoViewHolder> {
    private ArrayList<T> elements;
    private OnElementListener onElementListener;
    //constructor
    public MemoListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback, OnElementListener onElementListener){
        super(diffCallback);
        this.onElementListener = onElementListener;
    }

    //Creates ViewHolder for view (note_item.xml), converts xml to View object, this method runs when no ViewHolders are available
    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_item, parent, false);
        return new MemoViewHolder(itemView, this.onElementListener);
    }

    // Prepare view to be added to layout, called after ViewHolder obtained
    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position){
        populateMemoViewHolder(holder, getItem(position));
    }

    protected abstract void populateMemoViewHolder(MemoViewHolder holder, T element);


    // Allows access to each list item view without needing to lookup every time
    public static class MemoViewHolder extends RecyclerView.ViewHolder{
        public TextView titleTextView;

        public MemoViewHolder(View itemView, OnElementListener onElementListener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.memo_title_text);
            itemView.setOnClickListener(view -> onElementListener.onElementClick(getBindingAdapterPosition()));
        }
    }

    public interface OnElementListener {
        void onElementClick(int position);
    }
}
