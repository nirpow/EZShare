package com.colman.ezshare.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.ezshare.Helpers.AuthHelper;
import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Models.Post;
import com.colman.ezshare.Models.Profile;
import com.colman.ezshare.Models.User;
import com.colman.ezshare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewOtherUserProfileActivity extends AppCompatActivity {

    ImageView profilePic;
    TextView postCount;
    TextView FollowersCount;
    TextView FollowingCount;
    TextView displayName;
    TextView about;
    RecyclerView recyclerView;
    TextView follow;
    TextView unfollow;

    ProfileViewModel viewData;
    private Parcelable recyclerViewState;
    LiveData<List<Post>> postsListLiveData;
    LiveData<Profile> prfileListLiveData;

    ProfileAdapter adapter;

    private static final String TAG = "ViewOtherUserProfileAct";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_use);
        profilePic = findViewById(R.id.profile_photo);
        postCount = findViewById(R.id.tvPosts);
        FollowersCount = findViewById(R.id.tvFollowers);
        FollowingCount = findViewById(R.id.tvFollowing);
        displayName = findViewById(R.id.display_name);
        about = findViewById(R.id.description);
        follow = findViewById(R.id.follow);

        unfollow = findViewById(R.id.unfollow);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userUid");


        viewData = ViewModelProviders.of(this).get(ProfileViewModel.class);
        viewData.setUserFromActivity(userId);

        postsListLiveData = viewData.getPostList();
        postsListLiveData.observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> postList) {
                updateDisplay(postList);
            }
        });
        prfileListLiveData = viewData.getProfileLiveData();
        prfileListLiveData.observe(this, new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                updateUserDisplay(profile);
            }
        });

        recyclerView = findViewById(R.id.rv_profile);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this,3, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        updateDisplay(new ArrayList<Post>());
    }


    private void updateUserDisplay(final Profile profile) {
        //set user pic
        DatabaseHelper.instance.getUserByUid(profile.getUserProfileID(), new DatabaseHelper.GetUserByUidListener() {
            @Override
            public void onComplete(User user) {
                Picasso.get()
                        .load(user.getProfilePicUrl())
                        .resize(500,500)
                        .centerCrop()
                        .into(profilePic);
            }
        });


        // set user name display
        DatabaseHelper.instance.getUserByUid(profile.getUserProfileID(), new DatabaseHelper.GetUserByUidListener() {
            @Override
            public void onComplete(User user) {
                displayName.setText(user.getUserName());
            }
        });
        //set "about"
        about.setText(profile.getAbout());
        //set followers&&following
        countFollowingAmdFollowers(profile);

        //set on click follow
        AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
            @Override
            public void onComplete(final String userUID) {
                DatabaseHelper.instance.isCurUserFollowOther(userUID, profile.getUserProfileID(), new DatabaseHelper.IsCurUserFollowOtherListener() {
                    @Override
                    public void onComplete(Boolean isFollow) {
                        if(isFollow){
                            follow.setVisibility(View.GONE);
                            unfollow.setVisibility(View.VISIBLE);
                        }
                    }
                });
                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseHelper.instance.setCurrentUserfollowOtherUser(userUID, profile.getUserProfileID());
                        follow.setVisibility(View.GONE);
                        unfollow.setVisibility(View.VISIBLE);
                    }
                });
                unfollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseHelper.instance.removeCurrentUserfollowOtherUser(userUID, profile.getUserProfileID());
                        follow.setVisibility(View.VISIBLE);
                        unfollow.setVisibility(View.GONE);
                    }
                });
            }
        });


    }

    private void updateDisplay(List<Post> postList) {

        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();


        adapter = new ProfileAdapter(postList, this);
        recyclerView.setAdapter(adapter);

        postCount.setText(postList.size()+"");


        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        adapter.notifyDataSetChanged();
    }


    private void countFollowingAmdFollowers(Profile profile) {
        DatabaseHelper.instance.getOtherUserFollowingList(profile.getUserProfileID(), new DatabaseHelper.GetUsersFolloweringListListener() {
            @Override
            public void onComplete(List<String> data1) {
                Log.d(TAG, "onComplete: FollowingCount data size "+ data1.size());
                FollowingCount.setText(data1.size()+"");
            }
        });
        DatabaseHelper.instance.getUserFollowersList(profile.getUserProfileID(), new DatabaseHelper.GetUsersFollowersListListener() {
            @Override
            public void onComplete(List<String> data) {
                FollowersCount.setText(data.size()+"");
                Log.d(TAG, "onComplete: FollowersCount data size "+ data.size());
            }
        });
    }



}
