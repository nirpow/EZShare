package com.colman.ezshare.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.ezshare.Helpers.AuthHelper;
import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Models.Post;
import com.colman.ezshare.R;
import com.colman.ezshare.ViewPostActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileAdapter extends  RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>{
    List<Post>posts;
    Context context;

    public ProfileAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img,parent,false);
        ProfileViewHolder viewHolder = new ProfileViewHolder(view);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        final Post post =posts.get(position);
        Picasso.get()
                .load(post.getImageUrl())
                .resize(500,500)
                .centerCrop()
                .into(holder.imageView);

        //open post view
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPostActivity.class);
                intent.putExtra("postId", post);
                context.startActivity(intent);
            }
        });

        //long press to delete picture
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
                    @Override
                    public void onComplete(String userUID) {
                        if(post.getUserUID().equals(userUID)){
                            deletePost(post);
                        }
                    }
                });
                return false;
            }
        });
    }

    private void deletePost(final Post post) {
        // open dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Delete this post?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                DatabaseHelper.instance.deletePost(post.getPostId());
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.previewImage);
        }
    }
}