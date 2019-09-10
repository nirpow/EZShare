package com.colman.ezshare.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private String userProfileID;
    private String about;

    public Profile(String profileID, User userProfile) {
        this.userProfileID = profileID;
        this.about = "";
    }

    public Profile() {
    }

    public String getUserProfileID() {
        return userProfileID;
    }

    public void setUserProfileID(String userProfileID) {
        this.userProfileID = userProfileID;
    }


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public static Creator<Profile> getCREATOR() {
        return CREATOR;
    }

    protected Profile(Parcel in) {
        userProfileID = in.readString();
        about = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userProfileID);
        dest.writeString(about);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "userProfileID='" + userProfileID + '\'' +
                ", about='" + about + '\'' +
                '}';
    }
}
