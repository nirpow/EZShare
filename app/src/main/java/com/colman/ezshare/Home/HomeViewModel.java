package com.colman.ezshare.Home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.colman.ezshare.Helpers.AuthHelper;
import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Models.Post;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";

    MutableLiveData< List<Post>> postListLiveData = new MutableLiveData<>();

    public LiveData< List<Post>> getPostList() {
        Log.d(TAG, "getPostList: ");
        return postListLiveData;
    }

    public HomeViewModel() {
        Log.d(TAG, "HomeViewModel: ");

        AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
            @Override
            public void onComplete(String userUID) {
                DatabaseHelper.instance.getUserFollowingList(userUID, new DatabaseHelper.GetUsersFolloweringListListener() {
                    @Override
                    public void onComplete(List<String> postList) {
                        DatabaseHelper.instance.getAllReleviantPosts(postList, new DatabaseHelper.GetRelevantPostListener() {
                            @Override
                            public void onComplete(List<Post> data) {
                                postListLiveData.setValue(data);

                            }
                        });
                    }
                });
            }
        });


//        DatabaseHelper.instance.getAllPosts(new DatabaseHelper.GetPostListener() {
//            @Override
//            public void onComplete(List<Post> data) {
//                postListLiveData.setValue(data);
//                Log.d(TAG, "onComplete: data : "+data.toString() );
//            }
//        });
    }
}
