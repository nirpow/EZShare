package com.colman.ezshare.Home;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.ezshare.MainActivity;
import com.colman.ezshare.Models.Post;
import com.colman.ezshare.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {
    public HomeFragment() {
    }

    private static final String TAG = "HomeFragment";
    HomeViewModel viewData;

    LiveData<List<Post>>  postsListLiveData;

    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;

    MainActivity activity;

    HomeAdapter adapter;
    TextView noPosts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView:  " );

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        activity =(MainActivity)getContext();

        noPosts = view.findViewById(R.id.tv_noposts);
        recyclerView = view.findViewById(R.id.rv_home);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        updateDisplay(new ArrayList<Post>());


        return view;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
        viewData = ViewModelProviders.of(this).get(HomeViewModel.class);

        postsListLiveData = viewData.getPostList();
        postsListLiveData.observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> postList) {
                updateDisplay(postList);
            }
        });
    }

    private void updateDisplay(List<Post> postList) {

//        List<Post> data = postsListLiveData.getValue();
        Collections.sort(postList);


        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        adapter = new HomeAdapter(postList, getContext());
        recyclerView.setAdapter(adapter);

        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        adapter.notifyDataSetChanged();
        if(postList != null && postList.size() > 0) {
            noPosts.setVisibility(View.INVISIBLE);
        }else {
            noPosts.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "updateDisplay: num of posts:" + postList.size()+" posts: "+postList.toString());

    }
}

