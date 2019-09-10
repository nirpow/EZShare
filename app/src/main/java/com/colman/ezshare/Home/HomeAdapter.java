package com.colman.ezshare.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.ezshare.Comment.CommentsActivity;
import com.colman.ezshare.Helpers.AuthHelper;
import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Likes.LikesActivity;
import com.colman.ezshare.Models.Like;
import com.colman.ezshare.Models.Post;
import com.colman.ezshare.Models.User;
import com.colman.ezshare.Profile.ViewOtherUserProfileActivity;
import com.colman.ezshare.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{

    private static final String TAG = "HomeAdapter";
    private List<Post> posts;
    private Context context;
    private IHomeAdapterListener listener;


    public HomeAdapter(List<Post> posts, Context context) {
        Log.d(TAG, "HomeAdapter: ");
        this.posts = posts;
        this.context = context;
    }

    public void setClickListener(IHomeAdapterListener clickListener) {
        listener = clickListener;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post,viewGroup,false);
        HomeViewHolder viewHolder = new HomeViewHolder(view);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder viewHolder, int i) {
        final Post post  = posts.get(i);

        // set profile pic
        DatabaseHelper.instance.getUserByUid(post.getUserUID(), new DatabaseHelper.GetUserByUidListener() {
            @Override
            public void onComplete(User user) {
                // set profile pic
                Picasso.get()
                        .load(user.getProfilePicUrl())
                        .resize(400, 400)
                        .centerCrop()
                        .into(viewHolder.profileImageView);
                // set user name text view
                viewHolder.textViewName.setText(user.getUserName());

            }
        });
        //set on top click
        viewHolder.topBarPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(context, ViewOtherUserProfileActivity.class);
                intent.putExtra("userUid", post.getUserUID());
                context.startActivity(intent);
            }
        });

        //set main photo
        Picasso.get()
                .load(post.getImageUrl())
                .into(viewHolder.photo);

        viewHolder.captionTextView.setText(post.getCaption());

        //Check if current user liked the post
        if(isCurrentUserLikeThePost(post.getLikeList())) {
            viewHolder.likeRedBtn.setVisibility(View.VISIBLE);
            viewHolder.likeEmptyBtn.setVisibility(View.GONE);
        }
        //unlike post
        viewHolder.likeRedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
                    @Override
                    public void onComplete(String userUID) {
                        DatabaseHelper.instance.currentUserUnlikePost(post.getPostId(),userUID);
                        viewHolder.likeRedBtn.setVisibility(View.GONE);
                        viewHolder.likeEmptyBtn.setVisibility(View.VISIBLE);
                        updateNumOfLikes(post, viewHolder.likesTextView );
                    }
                });
            }
        });
        //like post
        viewHolder.likeEmptyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
                    @Override
                    public void onComplete(String userUID) {
                        DatabaseHelper.instance.currentUserLikePost(post.getPostId(), userUID);
                        viewHolder.likeRedBtn.setVisibility(View.VISIBLE);
                        viewHolder.likeEmptyBtn.setVisibility(View.GONE);
                    }
                });
            }
        });

        //count likes
        updateNumOfLikes(post, viewHolder.likesTextView);
        //count commnents
        updateNumOfComments(post, viewHolder.viewCommentsTextView);

        //show comments
        viewHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment(post.getPostId());
            }
        });

        //set date
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String strDt = simpleDate.format(post.getDate());
        viewHolder.dateTextView.setText(strDt);
    }

    private void updateNumOfComments(final Post post, TextView textView) {
        if(post.getCommentList() != null){
            int numOfComments = (post.getCommentList().size() - 1);
            String commentsLine;
            if(numOfComments < 1){
                commentsLine = "No one commented on the post";
            }
            else {
                commentsLine = "View all "+ numOfComments + " comments";
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCommentsActivity(post);
                    }
                });
            }
            textView.setText(commentsLine);
        }
    }

    private void addComment(final String postId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Add Comment");
        alert.setMessage("(And be nice)");
// Set an EditText view to get user input
        final EditText input = new EditText(context);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
                    @Override
                    public void onComplete(String userUID) {
                        DatabaseHelper.instance.currentUserCommented(postId, userUID,input.getText().toString());
                    }
                });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    private void updateNumOfLikes(final Post post, TextView textView) {
        if(post.getLikeList() != null){
            int numOfLikes = (post.getLikeList().size()-1);
            textView.setText(numOfLikes+" Likes");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLikesActivity(post);
                }
            });
        }
    }

    private boolean isCurrentUserLikeThePost(final List<Like> likeList) {
        final boolean[] ans = {false};
        AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
            @Override
            public void onComplete(String userUID) {
                for (Like like: likeList
                     ) {
                    try {
                        if(userUID.equals(like.getUserUID())){
                            ans[0] = true;
                        }
                    }catch (Exception e){
                    }
                }
            }
        });
        return ans[0];
    }

    private void showLikesActivity(Post  post) {
        Intent intent = new Intent(context , LikesActivity.class);
        intent.putExtra("postId", post);
        context.startActivity(intent);
    }

    private void showCommentsActivity(Post  post) {
        Log.d(TAG, "showCommentsActivity:post " +post.toString());
        Intent intent = new Intent(context , CommentsActivity.class);
        intent.putExtra("postId", post);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView textViewName;
        ImageView photo;
        ImageView likeRedBtn;
        ImageView likeEmptyBtn;
        ImageView commentBtn;
        TextView likesTextView;
        TextView captionTextView;
        TextView viewCommentsTextView;
        TextView dateTextView;
        RelativeLayout topBarPost;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profile_photo);
            textViewName = itemView.findViewById(R.id.username_post);
            photo  = itemView.findViewById(R.id.post_image);
            likeRedBtn = itemView.findViewById(R.id.image_heart_red);
            likeEmptyBtn = itemView.findViewById(R.id.image_heart);
            commentBtn = itemView.findViewById(R.id.speech_bubble);
            likesTextView = itemView.findViewById(R.id.image_likes);
            captionTextView = itemView.findViewById(R.id.image_caption);
            viewCommentsTextView = itemView.findViewById(R.id.image_comments_link);
            dateTextView = itemView.findViewById(R.id.image_time_posted);
            topBarPost = itemView.findViewById(R.id.relLayout1);


        }
    }

    public interface IHomeAdapterListener{
        void OnPostClicked(int position, View view);
    }
}
