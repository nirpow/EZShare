package com.colman.ezshare.Utils;

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

import com.colman.ezshare.Models.User;
import com.colman.ezshare.Profile.ViewOtherUserProfileActivity;
import com.colman.ezshare.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowUserListAdapter  extends RecyclerView.Adapter<ShowUserListAdapter.ShowListViewHolder> {
    List<User> users;
    Context context;

    public ShowUserListAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ShowListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        ShowListViewHolder viewHolder = new ShowListViewHolder(view);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull ShowListViewHolder holder, int position) {
        final User user = users.get(position);
        holder.userNameTextView.setText(user.getUserName());
        Picasso.get()
                .load(user.getProfilePicUrl())
                .resize(400, 400)
                .centerCrop()
                .into(holder.userPic);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewOtherUserProfileActivity.class);
                intent.putExtra("userUid", user.getUserUID());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public  class ShowListViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        ImageView userPic;
        LinearLayout linearLayout;

        public ShowListViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView= itemView.findViewById(R.id.tv_username_comment);
            userPic = itemView.findViewById(R.id.profile_photo_user_comment);
            linearLayout = itemView.findViewById(R.id.linLayout_name_pic);
        }
    }
}
