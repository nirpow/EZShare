package com.colman.ezshare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.colman.ezshare.Comment.CommentsActivity;
import com.colman.ezshare.Helpers.AuthHelper;
import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Likes.LikesActivity;
import com.colman.ezshare.Models.Like;
import com.colman.ezshare.Models.Post;
import com.colman.ezshare.Models.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class ViewPostActivity extends AppCompatActivity {

    private static final String TAG = "ViewPostActivity";
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
    TextView editAboutTextView;
    TextView deletePostTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);


        Intent intent = getIntent();
        Post tmpPost = intent.getParcelableExtra("postId");
        DatabaseHelper.instance.getPostByPostId(tmpPost.getPostId(), new DatabaseHelper.GetPostByIdListener() {
            @Override
            public void onComplete(Post thePost) {
                if(thePost != null) {
                    init(thePost);
                    Log.d(TAG, "onComplete: thePost:" + thePost.toString()) ;
                }
            }
        });

        profileImageView = findViewById(R.id.profile_photo);
        textViewName = findViewById(R.id.username_post);
        photo = findViewById(R.id.post_image);
        likeRedBtn = findViewById(R.id.image_heart_red);
        likeEmptyBtn = findViewById(R.id.image_heart);
        commentBtn = findViewById(R.id.speech_bubble);
        likesTextView = findViewById(R.id.image_likes);
        captionTextView = findViewById(R.id.image_caption);
        viewCommentsTextView = findViewById(R.id.image_comments_link);
        dateTextView = findViewById(R.id.image_time_posted);
        editAboutTextView = findViewById(R.id.tv_edit_post);
        deletePostTextView = findViewById(R.id.tv_delete_post);
//        Log.d(TAG, "onCreate: the post: " + post.toString());

    }

    @Override
    protected void onStart() {
        super.onStart();
        // set profile pic
    }
    private void init(final Post post){

        DatabaseHelper.instance.getUserByUid(post.getUserUID(), new DatabaseHelper.GetUserByUidListener() {
            @Override
            public void onComplete(User user) {

                Log.d(TAG, "onComplete: user: "+ user);
                // set profile pic
                Picasso.get()
                        .load(user.getProfilePicUrl())
                        .resize(400, 400)
                        .centerCrop()
                        .into(profileImageView);
                // set user name text view
                textViewName.setText(user.getUserName());

            }
        });

        AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
            @Override
            public void onComplete(String userUID) {
                if(!post.getUserUID().equals(userUID)){
                    editAboutTextView.setVisibility(View.GONE);
                    deletePostTextView.setVisibility(View.GONE);
                }
            }
        });

        editAboutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAbout(post.getPostId());
            }
        });
        deletePostTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(post.getPostId());
            }
        });


        //set main photo
        Picasso.get()
                .load(post.getImageUrl())
                .into(photo);

        captionTextView.setText(post.getCaption());

        //Check if current user liked the post

        if(isCurrentUserLikeThePost(post.getLikeList())) {
            likeRedBtn.setVisibility(View.VISIBLE);
            likeEmptyBtn.setVisibility(View.GONE);
        }
        //unlike post
        likeRedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
                    @Override
                    public void onComplete(String userUID) {
                        DatabaseHelper.instance.currentUserUnlikePost(post.getPostId(),userUID);
                        likeRedBtn.setVisibility(View.GONE);
                        likeEmptyBtn.setVisibility(View.VISIBLE);
                        updateNumOfLikes(post, likesTextView );
                    }
                });
            }
        });
        //like post
        likeEmptyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
                    @Override
                    public void onComplete(String userUID) {
                        DatabaseHelper.instance.currentUserLikePost(post.getPostId(), userUID);
                        likeRedBtn.setVisibility(View.VISIBLE);
                        likeEmptyBtn.setVisibility(View.GONE);
                    }
                });
            }
        });

        //count likes
        updateNumOfLikes(post, likesTextView);
        //count commnents
        updateNumOfComments(post, viewCommentsTextView);

        //show comments
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment(post.getPostId());
            }
        });

        //set date
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDt = simpleDate.format(post.getDate());
        dateTextView.setText(strDt);
    }

    private void deletePost(final String postId) {
        // open dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete this post?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
                DatabaseHelper.instance.deletePost(postId);
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }


    private void addComment(final String postId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add Comment");
        alert.setMessage("(And be nice)");
// Set an EditText view to get user input
        final EditText input = new EditText(this);
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

    private void editAbout(final String postId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edit Post Caption");
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
                    @Override
                    public void onComplete(String userUID) {
                        DatabaseHelper.instance.changeAboutOfPostListener(postId, input.getText().toString(), new DatabaseHelper.ChangeAboutOfPostListener() {
                            @Override
                            public void onComplete(String caption) {
                                captionTextView.setText(caption);
                            }
                        });
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
        Intent intent = new Intent(this , LikesActivity.class);
        intent.putExtra("postId", post);
        startActivity(intent);
    }

    private void showCommentsActivity(Post  post) {
        Log.d(TAG, "showCommentsActivity:post " +post.toString());
        Intent intent = new Intent(this , CommentsActivity.class);
        intent.putExtra("postId", post);
        startActivity(intent);
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
}
