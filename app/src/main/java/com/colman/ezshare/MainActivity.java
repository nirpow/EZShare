package com.colman.ezshare;

//import android.material.tabs.TabLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Helpers.StoageHelper;
import com.colman.ezshare.Home.HomeFragment;
import com.colman.ezshare.Profile.ProfileFragment;
import com.colman.ezshare.Search.SearchFragment;
import com.colman.ezshare.Upload.UploadFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private StorageReference mStorageRef;

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

    public StoageHelper stoageHelper;
    public DatabaseHelper databaseHelper;

    Fragment fragmentHome;
    Fragment fragmentSearch;
    Fragment fragmentUpload;
    Fragment fragmentProfile;
    FragmentManager fm;
    Fragment active;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        frameLayout = findViewById(R.id.container);
        active = fragmentHome;

        fm.beginTransaction().add(R.id.container, fragmentProfile, "4").hide(fragmentProfile).commit();
        fm.beginTransaction().add(R.id.container, fragmentUpload, "3").hide(fragmentUpload).commit();
        fm.beginTransaction().add(R.id.container, fragmentSearch, "2").hide(fragmentSearch).commit();
        fm.beginTransaction().add(R.id.container,fragmentHome, "1").commit();

        bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        Log.d(TAG, "onNavigationItemSelected: navigation_home " + active.toString());
                        fm.beginTransaction().hide(active).show(fragmentHome).commit();
                        active = fragmentHome;
                        return true;
                    case R.id.navigation_search:
                        fm.beginTransaction().hide(active).show(fragmentSearch).commit();
                        active = fragmentSearch;
                        return true;

                    case R.id.navigation_add:
                        fm.beginTransaction().hide(active).show(fragmentUpload).commit();
                        active = fragmentUpload;
                        return true;

                    case R.id.navigation_profile:
                        Log.d(TAG, "onNavigationItemSelected: navigation_profile "+ active.toString());
                        fm.beginTransaction().hide(active).show(fragmentProfile).commit();
                        active = fragmentProfile;
                        return true;
                }
                return false;
            }
        });


    }


    private void  init(){
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        stoageHelper = new StoageHelper( this);
        //databaseHelper = new DatabaseHelper();

        fragmentHome = new HomeFragment();
        fragmentSearch = new SearchFragment();
        fragmentUpload = new UploadFragment();
        fragmentProfile = new ProfileFragment();
        fm = getSupportFragmentManager();
    }
    public FirebaseAuth getAuth(){
        return mAuth;
    }
}
