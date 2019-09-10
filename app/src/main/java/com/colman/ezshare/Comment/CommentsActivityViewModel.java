package com.colman.ezshare.Comment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.colman.ezshare.Models.Comment;

import java.util.List;

public class CommentsActivityViewModel extends ViewModel {
    private static final String TAG="CommentsActivityViewMod";
    MutableLiveData<List<Comment>> postListLiveData = new MutableLiveData<>();


    public LiveData< List<Comment>> getCommentsList() {
        Log.d(TAG, "getPostList: ");
        return postListLiveData;
    }

    public CommentsActivityViewModel() {
    }
}
/*
    MutableLiveData< List<Post>> postListLiveData = new MutableLiveData<>();

    public LiveData< List<Post>> getPostList() {
        Log.d(TAG, "getPostList: ");
        return postListLiveData;
    }

    public HomeViewModel() {
        Log.d(TAG, "HomeViewModel: ");
        DatabaseHelper.instance.getAllPosts(new DatabaseHelper.GetPostListener() {

            @Override
            public void onComplete(List<Post> data) {
                postListLiveData.setValue(data);
                Log.d(TAG, "onComplete: data : "+data.toString() );
            }
        });
    }
}

*/