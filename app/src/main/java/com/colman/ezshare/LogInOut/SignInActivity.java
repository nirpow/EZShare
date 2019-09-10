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

import com.colman.ezshare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    EditText mETPassword, mETEmail;
    private ProgressBar mProgressBar;
    private TextView mPleaseWait;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mETPassword = findViewById(R.id.et_password_signin);
        mETEmail = findViewById(R.id.et_email_signin);

        mAuth = FirebaseAuth.getInstance();

        mProgressBar =  findViewById(R.id.progressBar);
        mPleaseWait =  findViewById(R.id.pleaseWait);
        mPleaseWait.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        Button buttonSubmitSignIn = findViewById(R.id.btn_submit_signin);
        buttonSubmitSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mETEmail.getText().toString();
                String password = mETPassword.getText().toString();

                mProgressBar.setVisibility(View.VISIBLE);
                mPleaseWait.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "onComplete:task.isSuccessful ");
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    finish();
                                } else {
                                    Log.d(TAG, "onComplete: NOT Successful");
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                   updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });


    }

    private void updateUI(Object o) {
        //TODO: if  null tell user why fail login
        //else


    }
}
