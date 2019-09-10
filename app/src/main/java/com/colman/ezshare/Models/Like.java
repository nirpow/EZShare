package com.colman.ezshare.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Like implements Parcelable {
    private String userUID;

    public Like(String userUID) {
        this.userUID = userUID;
    }

    public Like() {
    }

    protected Like(Parcel in) {
        userUID = in.readString();
    }

    public static final Creator<Like> CREATOR = new Creator<Like>() {
        @Override
        public Like createFromParcel(Parcel in) {
            return new Like(in);
        }

        @Override
        public Like[] newArray(int size) {
            return new Like[size];
        }
    };

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public static Creator<Like> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUID);
    }
}
