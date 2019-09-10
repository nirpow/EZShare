package com.colman.ezshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.colman.ezshare.Helpers.AuthHelper;
import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Helpers.StorageExtention;
import com.colman.ezshare.Models.Profile;
import com.colman.ezshare.Models.User;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class EditInfoActivity extends AppCompatActivity {
    private ImageView profilePic;
    private EditText about;
    private EditText newUserName;
    private TextView fromDeviceButton;
    private Button doneButton;
    private Uri imguri;
    private String imageUrl;
    final public int CHOOSE_FILE_CODE = 991;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        Intent intent = getIntent();
        String userUid=intent.getStringExtra("userUid");
        profilePic = findViewById(R.id.iv_image_review);
        about = findViewById(R.id.et_about_edit);
        newUserName = findViewById(R.id.et_username_edit);
        fromDeviceButton = findViewById(R.id.btn_from_phone_edit);
        doneButton = findViewById(R.id.btn_update);


        DatabaseHelper.instance.getProfileByUid(userUid, new DatabaseHelper.GetProfileByUidListener() {
            @Override
            public void onComplete(Profile profile) {


                about.setText(profile.getAbout());
                DatabaseHelper.instance.getUserByUid(profile.getUserProfileID(), new DatabaseHelper.GetUserByUidListener() {
                    @Override
                    public void onComplete(User user) {
                        Picasso.get()
                                .load(user.getProfilePicUrl())
                                .resize(400, 400)
                                .centerCrop()
                                .into(profilePic);
                        newUserName.setText(user.getUserName());
                    }
                });
            }
        });

        fromDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
                            @Override
                            public void onComplete(final  String userUID) {
                                DatabaseHelper.instance.changeUserName(userUID,newUserName.getText()+"");
                                DatabaseHelper.instance.changeAboutOfUser(userUID, about.getText()+"");
                                finish();
                            }
                        });
                    }
                });

    }





    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_FILE_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_FILE_CODE  && resultCode == RESULT_OK && data != null && data.getData() != null){
            imguri = data.getData();
            Glide.with(this)
                    .load(imguri)
                    .into(profilePic);

            StorageExtention.instance.imageUploader(imguri, getApplicationContext(), new StorageExtention.imageUploaderListener() {
                @Override
                public void onComplete(final String imgURL) {
                    imageUrl = imgURL;
                    Date now = new Date();
                    AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
                        @Override
                        public void onComplete(final  String userUID) {
                            DatabaseHelper.instance.changeProfilePicture(userUID, imgURL);

                        }
                    });
                }
            });

        }
    }
}
