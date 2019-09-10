package com.colman.ezshare.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.ezshare.MainActivity;
import com.colman.ezshare.Models.User;
import com.colman.ezshare.R;

import java.util.ArrayList;
import java.util.List;

public class ShowUserListActivity extends AppCompatActivity {

    private static final String TAG = "ShowUserListActivity";
    private RecyclerView recyclerView;
    MainActivity activity;
    ShowUserListAdapter adapter;

    ArrayList<String> usersList;
    List<User> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_list);

        Intent intent= getIntent();
        usersList= intent.getStringArrayListExtra("users");
        Log.d(TAG, "onCreate:usersList = " +usersList);
         getObjFromStringUsers();

        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new ShowUserListAdapter(users, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void getObjFromStringUsers() {

        }
    }

