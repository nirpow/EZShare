package com.colman.ezshare.Comment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Models.Comment;
import com.colman.ezshare.Models.Post;
import com.colman.ezshare.R;

import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private static final String TAG = "CommentsActivity";
    private RecyclerView recyclerView;
    CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        Post thePost = intent.getParcelableExtra("postId");

        Log.d(TAG, "onCreate:postId  " +thePost.toString());
        DatabaseHelper.instance.getCommentsOfPosts(thePost.getPostId(), new DatabaseHelper.GetCommentsOfPostListener() {
            @Override
            public void onComplete(List<Comment> comments) {
                updateList(comments);
            }
        });

        recyclerView = findViewById(R.id.rv_comments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void updateList(List<Comment> comments) {
        comments.remove(0); // delete dummy
        Log.d(TAG, "updateList: comments : "+ comments.toString());
        adapter = new CommentsAdapter(comments, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
