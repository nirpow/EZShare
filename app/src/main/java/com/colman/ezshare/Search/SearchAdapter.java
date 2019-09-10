package com.colman.ezshare.Search;

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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<User> users;
    private Context context;

    public SearchAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search,parent,false);
        SearchViewHolder viewHolder = new SearchViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        final User user = users.get(position);
        holder.userName.setText(user.getUserName());
        Picasso.get()
                .load(user.getProfilePicUrl())
                .resize(400, 400)
                .centerCrop()
                .into(holder.userImage);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(context, ViewOtherUserProfileActivity.class);
                intent.putExtra("userUid", user.getUserUID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        ImageView userImage;
        LinearLayout linearLayout;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_username_search);
            userImage = itemView.findViewById(R.id.profile_photo_search);
            linearLayout = itemView.findViewById(R.id.linLayout_name_pic2);
        }
    }
}
