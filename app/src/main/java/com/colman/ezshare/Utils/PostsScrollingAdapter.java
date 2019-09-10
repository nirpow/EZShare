package com.colman.ezshare.Utils;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostsScrollingAdapter extends RecyclerView.Adapter<PostsScrollingAdapter.PostViewHolder> {

    static class PostViewHolder extends RecyclerView.ViewHolder{

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
