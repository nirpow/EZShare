package com.colman.ezshare.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String userUID;
    private String userName;
    private String email;
    private String profilePicUrl;


    public User(String userUID, String userName, String email) {
        this.userUID = userUID;
        this.userName = userName;
        this.email = email;
        this.profilePicUrl = "https://firebasestorage.googleapis.com/v0/b/ezshare-74d30.appspot.com/o/Defualts%2Fuser.png?alt=media&token=2d507713-6698-47f1-a16c-040e8ec41aaf";
    }

    public User() {
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }



    protected User(Parcel in) {
        userUID = in.readString();
        userName = in.readString();
        email = in.readString();
        profilePicUrl = in.readString();

    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUID);
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeString(profilePicUrl);
    }

    @Override
    public String toString() {
        return "User{" +
                "userUID='" + userUID + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", profilePicUrl='" + profilePicUrl + '\'' +
                '}';
    }
}
