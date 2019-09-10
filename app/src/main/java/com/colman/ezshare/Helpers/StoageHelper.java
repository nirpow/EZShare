package com.colman.ezshare.Helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class StoageHelper {
    private StorageReference mStorageRef;
    private Uri imguri;
    private Context context;
    private StorageTask uploadTask;

    public StoageHelper(Context context) {
        this.mStorageRef =  FirebaseStorage.getInstance().getReference();
        this.context = context;
    }

    public StorageTask getUploadTask() {
        return uploadTask;
    }

    public void setUploadTask(StorageTask uploadTask) {
        this.uploadTask = uploadTask;
    }

    public Uri getImguri() {
        return imguri;
    }

    public void setImguri(Uri imguri) {
        this.imguri = imguri;
    }

    public interface imageUploaderListener {
        void onComplete(String imgURL);
    }


    public String imageUploader(Uri uri, final imageUploaderListener listener) {
        String fileName = System.currentTimeMillis()+"."+getExtention(uri);
        final StorageReference  reference = mStorageRef.child("Images")
                .child(fileName);

        uploadTask = reference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(context, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                listener.onComplete(uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(context, "Upload Failed, Try again later...", Toast.LENGTH_SHORT).show();
                    }
                });
        return fileName;
    }

    private String getExtention(Uri uri){
        ContentResolver resolver = Objects.requireNonNull(context.getContentResolver());
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));

    }

    public StorageReference getmStorageRef() {
        return mStorageRef;
    }

    public void setmStorageRef(StorageReference mStorageRef) {
        this.mStorageRef = mStorageRef;
    }


}

