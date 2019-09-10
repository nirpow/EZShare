package com.colman.ezshare.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    String userUID;
    String content;

    public Comment(String userUID, String content) {
        this.userUID = userUID;
        this.content = content;
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        userUID = in.readString();
        content = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUID);
        dest.writeString(content);
    }
}
