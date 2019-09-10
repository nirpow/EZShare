package com.colman.ezshare.Upload;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.colman.ezshare.Helpers.AuthHelper;
import com.colman.ezshare.Helpers.DatabaseHelper;
import com.colman.ezshare.Helpers.StoageHelper;
import com.colman.ezshare.MainActivity;
import com.colman.ezshare.Models.Post;
import com.colman.ezshare.Models.User;
import com.colman.ezshare.R;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class UploadFragment extends Fragment {
    private static final String TAG = "UploadFragment";

    final public int CHOOSE_FILE_CODE = 991;
    final public int TAKE_PIC_CODE = 992;


    MainActivity activity;

    private Uri imguri;
    private String imageUrl;
    Bitmap bitmap;

    ImageView imageViewReview;
    Button buttonChooseFile;
    Button buttonUpload;
    EditText editTextCaption;


    Object mLockObject;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: Design a better layout UI
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        activity  =(MainActivity) getContext();

        editTextCaption = view.findViewById(R.id.et_caption);
        imageViewReview = view.findViewById(R.id.iv_image_review);

        buttonChooseFile = view.findViewById(R.id.btn_from_phone);
        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        buttonUpload = view.findViewById(R.id.btn_upload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.stoageHelper.getUploadTask() != null && activity.stoageHelper.getUploadTask().isInProgress()){
                    // Upload is in progress
                    Toast.makeText(getContext(), "Upload in progress...", Toast.LENGTH_SHORT).show();
                }
                else{
                    String imgUri = activity.stoageHelper.imageUploader(imguri, new StoageHelper.imageUploaderListener() {
                        @Override
                        public void onComplete(String imgURL) {
                            imageUrl = imgURL;
                            Date now = new Date();
                            final String makePostId = "post_"+now.hashCode();
                            AuthHelper.instamce.getCurrentUserUID(new AuthHelper.GetCurrentUserUidListener() {
                                @Override
                                public void onComplete(final  String userUID) {
                                    DatabaseHelper.instance.getUserByUid(userUID, new DatabaseHelper.GetUserByUidListener() {
                                        @Override
                                        public void onComplete(User user) {
                                            final Post newPost = new Post(makePostId, getCaption(),imageUrl ,new Date(),userUID, user.getUserName());
                                            DatabaseHelper.instance.addPostToDB(newPost, new DatabaseHelper.AddPostListener() {
                                                @Override
                                                public void onComplete() {
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });

                    //TODO : wait untill image is uploaded, then add post to DB.
                    //TODO: Update ui when upload finished.
                }
            }
        });

        Button buttonTakePic = view.findViewById(R.id.btn_take_pic);
        buttonTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        return view;
    }

    private String getCaption() {
        String caption = editTextCaption.getText().toString();
        return caption;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PIC_CODE);
    }


    private void FileChooser(){
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
            Glide.with(getContext())
                    .load(imguri)
                    .into(imageViewReview);
        }
        else if(requestCode == TAKE_PIC_CODE && resultCode == RESULT_OK ){
            bitmap = (Bitmap)data.getExtras().get("data");

          // imguri = getImageUri(getContext(), bitmap);

            Glide.with(getContext())
                    .load(bitmap)
                    .into(imageViewReview);
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        Log.d(TAG, "getImageUri 1 : " +path  +" . bitmap "  + bitmap.toString());
        return Uri.parse(path);
    }

}
