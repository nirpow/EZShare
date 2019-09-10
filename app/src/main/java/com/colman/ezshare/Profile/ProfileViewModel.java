package com.colman.ezshare.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.colman.ezshare.Helpers.AuthHelper;
import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Models.Post;
import com.colman.ezshare.Models.Profile;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private static final String TAG = "ProfileViewModel";

    MutableLiveData <List<Post>> postListLiveData = new MutableLiveData<>();
    MutableLiveData <Profile> profileLiveData = new MutableLiveData<>();

    public void setUserFromActivity(final String userUid) {
        DatabaseHelper.instance.getPostsOfSpesificUser(userUid, new DatabaseHelper.GetPostsOfSpesificUserListener() {
            @Override
            public void onComplete(List<Post> postsList) {
                postListLiveData.setValue(postsList);
            }
        });
        DatabaseHelper.instance.getProfileByUid(userUid, new DatabaseHelper.GetProfileByUidListener() {
            @Override
            public void onComplete(Profile profile) {
                profileLiveData.setValue(profile);
            }
        });
    }

    public LiveData<List<Post>> getPostList(){
        return postListLiveData;
    }

    public MutableLiveData<Profile> getProfileLiveData() {
        return profileLiveData;
    }

    public ProfileViewModel() {
        AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
            @Override
            public void onComplete(String userUID) {
                DatabaseHelper.instance.getSpecificUserPost(userUID, new DatabaseHelper.GetSpecificUserPostListener() {
                    @Override
                    public void onComplete(List<Post> data) {
                        postListLiveData.setValue(data);
                    }
                });
                DatabaseHelper.instance.getProfileByUid(userUID, new DatabaseHelper.GetProfileByUidListener() {
                    @Override
                    public void onComplete(Profile profile) {
                        profileLiveData.setValue(profile);
                    }
                });
            }
        });
    }
}
