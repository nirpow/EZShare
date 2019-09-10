package com.colman.ezshare.LogInOut;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Models.Profile;
import com.colman.ezshare.Models.User;
import com.colman.ezshare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    EditText mETUserName, mETPassword, mETEmail;
    private ProgressBar mProgressBar;
    private TextView mPleaseWait;
    private NestedScrollView scrollView;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Log.d(TAG, "onCreate: ");
        mETUserName = findViewById(R.id.et_username);
        mETPassword = findViewById(R.id.et_password);
        mETEmail = findViewById(R.id.et_email);

        mAuth = FirebaseAuth.getInstance();

        mProgressBar =  findViewById(R.id.progressBar);
        mPleaseWait =  findViewById(R.id.pleaseWait);

        mPleaseWait.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);



        Button btnJoinNow = findViewById(R.id.btn_joinnow);

        btnJoinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                String email = mETEmail.getText().toString();
                String password = mETPassword.getText().toString();

                mProgressBar.setVisibility(View.VISIBLE);
                mPleaseWait.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    mProgressBar.setVisibility(View.GONE);
                                    mPleaseWait.setVisibility(View.GONE);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    addUserToDB(user, mETUserName.getText().toString());
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                                // ...
                            }
                        });
            }
        });



    }

    private void updateUI(FirebaseUser user) {

        if(user == null){
            mProgressBar.setVisibility(View.GONE);
            mPleaseWait.setVisibility(View.GONE);
            // TODO: add explain to user

        }
    }

    private void addUserToDB(FirebaseUser user, String userName) {
        Log.d(TAG, "addUserToDB: ");
        assert user != null;
        String userUID = user.getUid();
        String userEmail = user.getEmail();
        User newUser = new User(userUID, userName, userEmail);

        DatabaseHelper.instance.addUserToDB(newUser, new DatabaseHelper.AddUserToDBListener() {
            @Override
            public void onComplete(User user) {
                Profile newProfile = new Profile(user.getUserUID(), user);
                DatabaseHelper.instance.addUserProfileToDB(newProfile, new DatabaseHelper.AddProfileToDBListener() {
                    @Override
                    public void onComplete() {
                        mProgressBar.setVisibility(View.GONE);
                        mPleaseWait.setVisibility(View.GONE);
                        finish();
                    }
                });
            }
        });

//
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(userUID);
//        reference.setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "onSuccess: ");
//                mProgressBar.setVisibility(View.GONE);
//                mPleaseWait.setVisibility(View.GONE);
//                finish();
//            }
//
//
//        });

    }


}
