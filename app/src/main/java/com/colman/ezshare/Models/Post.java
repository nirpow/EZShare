package com.colman.ezshare.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.room.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Post implements Parcelable ,Comparable<Post> {
    private static final String TAG = "Post";
    private String postId;
    private String caption;
    private String imageUrl;
    private Date date;
    private String userUID;
    private List<Like> likeList;
    private List<Comment> commentList;

    public Post() {
    }

    public Post(String postId, String caption, String imageUrl, Date date, String userUID, String userNamePostOwner) {
        this.postId = postId;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.date = date;
        this.userUID = userUID;
        this.likeList =  new ArrayList<>();
        likeList.add(new Like("dummy"));
        this.commentList = new ArrayList<>();
        commentList.add(new Comment("dummy", "dummy"));
    }



    protected Post(Parcel in) {
        Log.d(TAG, "Post: ");
        caption = in.readString();
        imageUrl = in.readString();
        userUID = in.readString();
        postId = in.readString();
        this.likeList = in.readArrayList(Like.class.getClassLoader());
        this.commentList = in.readArrayList( Comment.class.getClassLoader());
        this.date = (java.util.Date) in.readSerializable();
      //  in.readList(likeList, Like.class.getClassLoader());
     //   in.readList(commentList, Comment.class.getClassLoader());
    }

    public static Creator<Post> getCREATOR() {
        return CREATOR;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }




    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public List<Like> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<Like> likeList) {
        this.likeList = likeList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(imageUrl);
        dest.writeString(userUID);
        dest.writeString(postId);
        dest.writeList(likeList);
        dest.writeList(commentList);
        dest.writeSerializable(date);
    }


    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", caption='" + caption + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", date=" + date +
                ", userUID='" + userUID + '\'' +
                ", likeList=" + likeList +
                ", commentList=" + commentList +
                '}';
    }

    @Override
    public int compareTo(Post o) {
        if (getDate() == null || o.getDate() == null)
            return 0;
        return o.getDate().compareTo(getDate());

    }
}
