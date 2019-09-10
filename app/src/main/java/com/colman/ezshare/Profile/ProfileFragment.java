package com.colman.ezshare.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.ezshare.EditInfoActivity;
import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Models.Post;
import com.colman.ezshare.Models.Profile;
import com.colman.ezshare.Models.User;
import com.colman.ezshare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    Profile currentProfile;
    ImageView profilePic;
    TextView postCount;
    TextView FollowersCount;
    TextView FollowingCount;
    TextView EditYourProfile;
    TextView displayName;
    TextView about;
    RecyclerView recyclerView;

    ProfileViewModel viewData;
    private Parcelable recyclerViewState;
    LiveData<List<Post>> postsListLiveData;
    LiveData<Profile> prfileListLiveData;

    ProfileAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false );

        profilePic = view.findViewById(R.id.profile_photo);
        postCount = view.findViewById(R.id.tvPosts);
        FollowersCount = view.findViewById(R.id.tvFollowers);
        FollowingCount = view.findViewById(R.id.tvFollowing);
        EditYourProfile = view.findViewById(R.id.textEditProfile);
        displayName = view.findViewById(R.id.display_name);
        about = view.findViewById(R.id.description);



        recyclerView = view.findViewById(R.id.rv_profile);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        updateDisplay(new ArrayList<Post>());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewData = ViewModelProviders.of(this).get(ProfileViewModel.class);

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
        DatabaseHelper.instance.getProfileByUid(profile.getUserProfileID(), new DatabaseHelper.GetProfileByUidListener() {
            @Override
            public void onComplete(Profile theProfile) {
                about.setText(theProfile.getAbout());

            }
        });
        //set followers&&following
        countFollowingAmdFollowers(profile);

        EditYourProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), EditInfoActivity.class);
                intent.putExtra("userUid", profile.getUserProfileID());
                startActivity(intent);
            }
        });

    }

    private void updateDisplay(List<Post> postList) {

        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();


        adapter = new ProfileAdapter(postList, getContext());
        recyclerView.setAdapter(adapter);

        postCount.setText(postList.size()+"");


        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        adapter.notifyDataSetChanged();
    }

    private void countFollowingAmdFollowers(final Profile profile) {
        DatabaseHelper.instance.getUserFollowingList(profile.getUserProfileID(), new DatabaseHelper.GetUsersFolloweringListListener() {
            @Override
            public void onComplete(final List<String> data) {
                Log.d(TAG, "onComplete: FollowingCount data size aa2 "+ data.size());
                FollowingCount.setText(data.size()+"");
            }
        });

        DatabaseHelper.instance.getUserFollowersList(profile.getUserProfileID(), new DatabaseHelper.GetUsersFollowersListListener() {
            @Override
            public void onComplete(List<String> data) {
                FollowersCount.setText(data.size()+"");
                Log.d(TAG, "onComplete: FollowersCount data size aa2"+ data.size());
            }
        });

//        FollowingCount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: imhere");
//                DatabaseHelper.instance.getUserFollowingList(profile.getUserProfileID(), new DatabaseHelper.GetUsersFolloweringListListener() {
//                    @Override
//                    public void onComplete(List<String> postList) {
//                        Intent intent = new Intent(getContext(), ShowUserListActivity.class);
//                        intent.putStringArrayListExtra("users", (ArrayList<String>) postList);
//                        startActivity(intent);
//                    }
//                });
//
//            }
//        });


    }

}
