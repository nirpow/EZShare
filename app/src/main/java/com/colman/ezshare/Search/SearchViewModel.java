package com.colman.ezshare.Search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Models.User;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private static final String TAG = "SearchViewModel";

    MutableLiveData<List<User>> usersListLiveData = new MutableLiveData<>();

    public LiveData< List<User>> getUsersList() {
        Log.d(TAG, "getPostList: ");
        return usersListLiveData;
    }

    public SearchViewModel() {
        Log.d(TAG, "SearchViewModel : ");
        DatabaseHelper.instance.getAllUsers(new DatabaseHelper.GetUsersListListener() {
            @Override
            public void onComplete(List<User> data) {
                usersListLiveData.setValue(data);
                Log.d(TAG, "onComplete: data : "+data.toString() );
            }
        });
    }
}
