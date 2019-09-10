package com.colman.ezshare.Helpers;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthHelper {
    private static final String TAG = "AuthHelper";

    final public static AuthHelper instamce = new AuthHelper();

    private FirebaseAuth mAuth;

    public AuthHelper() {
        Log.d(TAG, "AuthHelper: ");
        mAuth = FirebaseAuth.getInstance();
    }

    public interface GetCurrentUserUidListener {
        void onComplete(String userUID);
    }

    public void getCurrentUserUID(GetCurrentUserUidListener listener ){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userUid = currentUser.getUid();
        listener.onComplete(userUid);
    }

}
