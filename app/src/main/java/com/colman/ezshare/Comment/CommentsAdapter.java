package com.colman.ezshare.Comment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Models.Comment;
import com.colman.ezshare.Models.User;
import com.colman.ezshare.Profile.ViewOtherUserProfileActivity;
import com.colman.ezshare.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentsAdapter extends  RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    List<Comment> comments;
    Context context;


    public CommentsAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, int position) {
        final Comment comment = comments.get(position);
        holder.contentTextView.setText(comment.getContent());
        DatabaseHelper.instance.getUserByUid(comment.getUserUID(), new DatabaseHelper.GetUserByUidListener() {
            @Override
            public void onComplete(User user) {
                Picasso.get()
                        .load(user.getProfilePicUrl())
                        .resize(400, 400)
                        .into(holder.userPic);
                holder.userNameTextView.setText(user.getUserName());
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewOtherUserProfileActivity.class);
                intent.putExtra("userUid", comment.getUserUID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView userNameTextView;
        TextView contentTextView;
        ImageView userPic;
        LinearLayout linearLayout;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView= itemView.findViewById(R.id.tv_username_comment);
            contentTextView= itemView.findViewById(R.id.tv_content_comment);
            userPic = itemView.findViewById(R.id.profile_photo_user_comment);
            linearLayout = itemView.findViewById(R.id.linLayout_name_pic);

        }
    }

}
