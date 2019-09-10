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
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class StorageExtention {
    private static final String TAG = "StorageExtention";

    public static final StorageExtention  instance= new StorageExtention();

    private FirebaseStorage theReference;

    public StorageExtention() {
        this.theReference = FirebaseStorage.getInstance();
    }

    public String imageUploader(Uri uri,final Context context, final imageUploaderListener listener) {
        String fileName = System.currentTimeMillis()+"."+getExtention(uri, context);
        final StorageReference reference = theReference
                .getReference("Images")
                .child(fileName);

        reference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Toast.makeText(context, "Profile Pic Uploaded", Toast.LENGTH_SHORT).show();
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

    private String getExtention(Uri uri , Context context){
        ContentResolver resolver = Objects.requireNonNull(context.getContentResolver());
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    public interface imageUploaderListener {
        void onComplete(String imgURL);
    }

}
