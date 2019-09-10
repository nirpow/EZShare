package com.colman.ezshare.Likes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Models.Like;
import com.colman.ezshare.Models.Post;
import com.colman.ezshare.Models.User;
import com.colman.ezshare.R;

import java.util.ArrayList;
import java.util.List;

public class LikesActivity extends AppCompatActivity {
    private static final String TAG = "LikesActivity";
    ListView listView;
    Post post;
    List<Like> likeList = new ArrayList<>();
    List<String> likeListStr = new ArrayList<>();
    Adapter rrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        listView = findViewById(R.id.likeslist);

        Intent intent = getIntent();
        Post thePost = intent.getParcelableExtra("postId");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, likeListStr);

        Log.d(TAG, "onCreate:  post: "  + thePost.toString());

        DatabaseHelper.instance.getLikesPost(thePost.getPostId(), new DatabaseHelper.GetLikesPostListener() {
            @Override
            public void onComplete(List<Like> postLikes) {
                likeList = postLikes;
                Log.d(TAG, "onComplete: postLikes "+ postLikes.toString());
                dropToList(likeList);
            }
        });

    }

    private void dropToList(List<Like> likeList) {
        Log.d(TAG, "dropToList:likeList:   " +likeList.toString());
        for (Like like: likeList
             ) {
            DatabaseHelper.instance.getUserByUid(like.getUserUID(), new DatabaseHelper.GetUserByUidListener() {
                @Override
                public void onComplete(User user) {
                    Log.d(TAG, "onComplete:getUserByUid ");
                    likeListStr.add(user.getUserName());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, likeListStr);
                    listView.setAdapter(adapter);
                }
            });
        }

    }

}