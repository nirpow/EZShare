package com.colman.ezshare.Likes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.colman.ezshare.Models.Post;

import java.util.List;

public class LikeActivityViewModel extends ViewModel {
    private static final String TAG = "LikeActivityViewModel";
    MutableLiveData<List<Post>> likesListLiveData = new MutableLiveData<>();

    public LiveData< List<Post>> getPostList() {
        return likesListLiveData;
    }

}
