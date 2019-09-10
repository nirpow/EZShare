package com.colman.ezshare.Helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.colman.ezshare.Models.Comment;
import com.colman.ezshare.Models.Like;
import com.colman.ezshare.Models.Post;
import com.colman.ezshare.Models.Profile;
import com.colman.ezshare.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";

    final public static DatabaseHelper instance = new DatabaseHelper();

    //DatabaseReference
    private FirebaseDatabase reference;


    public DatabaseHelper() {
        Log.d(TAG, "DatabaseHelper: ");
        reference = FirebaseDatabase.getInstance();
    }

    public void changeAboutOfUser(String UserId, String newAbout){
        DatabaseReference theReference = reference.getReference("Profiles").child(UserId).child("about");
        theReference.setValue(newAbout);
    }

    public void changeUserName(String UserId, String newUserName){
        DatabaseReference theReference = reference.getReference("Users").child(UserId).child("userName");
        theReference.setValue(newUserName);
    }
    public void  changeProfilePicture(String UserId, String newPictureUrl){
        DatabaseReference theReference = reference.getReference("Users").child(UserId).child("profilePicUrl");
        theReference.setValue(newPictureUrl);

    }

    public void addPostToDB(Post newPost, final AddPostListener listener) {
        Log.d(TAG, "addPostToDB: ");
        assert newPost != null;
        String postId = newPost.getPostId();
        DatabaseReference postsReference = reference.getReference("Posts").child(postId);
        postsReference.setValue(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: ");
                listener.onComplete();
            }
        });
    }

    public void deletePost(final String postId){
        DatabaseReference postRefference = reference.getReference("Posts").child(postId);
        postRefference.removeValue();
    }
    public void changeAboutOfPostListener(final String postId, final String caption, final ChangeAboutOfPostListener listener){
        DatabaseReference postRefference = reference.getReference("Posts").child(postId).child("caption");
        postRefference.setValue(caption).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onComplete(caption);
            }
        });

    }
    public void  isCurUserFollowOther (final String currentUser, final String otherUser , final IsCurUserFollowOtherListener listener){
        DatabaseReference postRefference = reference.getReference("Users").child(currentUser)
                .child("following");
        postRefference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isFollow = false;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                ) {
                    if(snapshot.getKey().equals(otherUser))
                    {
                        isFollow = true;
                    }
                }
                listener.onComplete(isFollow);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getUserFollowingList(String userUid, final GetUsersFolloweringListListener listener){
        DatabaseReference postRefference = reference.getReference("Users").child(userUid)
                .child("following");
        postRefference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> followingUsers = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                ) {
                    followingUsers.add(snapshot.getKey());
                }
                Log.d(TAG, "onDataChange: followingUsers : "+followingUsers.toString());
                listener.onComplete(followingUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getOtherUserFollowingList(String userUid, final GetUsersFolloweringListListener listener){
        DatabaseReference postRefference = reference.getReference("Users").child(userUid)
                .child("following");
        postRefference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> followingUsers = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                ) {
                    followingUsers.add(snapshot.getKey());
                }
                Log.d(TAG, "onDataChange: followingUsers : "+followingUsers.toString());
                listener.onComplete(followingUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getUserFollowersList(String userUid, final GetUsersFollowersListListener listener){
        DatabaseReference postRefference = reference.getReference("Users").child(userUid)
                .child("followers");
        postRefference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> followersUsers = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                ) {
                    followersUsers.add(snapshot.getKey());
                }
                listener.onComplete(followersUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getOtherUserFollowersList(String userUid, final GetUsersFollowersListListener listener){
        DatabaseReference postRefference = reference.getReference("Users").child(userUid)
                .child("followers");
        postRefference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> followersUsers = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                ) {
                    followersUsers.add(snapshot.getKey());
                }
                listener.onComplete(followersUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void removeCurrentUserfollowOtherUser(String currentUser, String otherUser){
        DatabaseReference postRefference = reference.getReference("Users");
        postRefference.child(currentUser).child("following").child(otherUser).removeValue();
        postRefference.child(otherUser).child("followers").child(currentUser).removeValue();
    }

    public void setCurrentUserfollowOtherUser(String currentUser, String otherUser){
        DatabaseReference postRefference = reference.getReference("Users");
        postRefference.child(currentUser).child("following").child(otherUser).setValue(true);
        postRefference.child(otherUser).child("followers").child(currentUser).setValue(true);
    }

    public void getPostsOfSpesificUser(final String userUid, final GetPostsOfSpesificUserListener listener){
        DatabaseReference postRefference = reference.getReference("Posts");
        postRefference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> userPosts = new ArrayList<>();
                for (DataSnapshot  snapshot : dataSnapshot.getChildren()
                ) {
                    if(snapshot.child("userUID").getValue().equals(userUid)){
                        Post thePost = snapshot.getValue(Post.class);
                        userPosts.add(thePost);
                    }
                }
                Log.d(TAG, "onDataChange:getPostsOfSpesificUser aaa1 userPosts = "+userPosts);
                listener.onComplete(userPosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getPostByPostId(String postId, final GetPostByIdListener listener){
        DatabaseReference postRefference = reference.getReference("Posts").child(postId);
        postRefference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post thePost = dataSnapshot.getValue(Post.class);
                listener.onComplete(thePost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addUserProfileToDB(Profile newProfile, final AddProfileToDBListener listener){
        DatabaseReference profileRefference = reference.getReference("Profiles").child(newProfile.getUserProfileID());
        profileRefference.setValue(newProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onComplete();
            }
        });
    }

    public void addUserToDB(final User newUser, final AddUserToDBListener listener){
        DatabaseReference userRefference = reference.getReference("Users").child(newUser.getUserUID());
        userRefference.setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onComplete(newUser);
            }
        });

    }
    public void getCommentsOfPosts(String postId,  final GetCommentsOfPostListener listener){
        DatabaseReference postReference = reference.getReference("Posts").child(postId).child("commentList");
        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Comment> commentList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()
                ) {
                    Comment value = snapshot.getValue(Comment.class);
                    commentList.add(value);
                }
                listener.onComplete(commentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void currentUserCommented(String postId, final String userUid, final String content){
        DatabaseReference postReference = reference.getReference("Posts").child(postId).child("commentList");
        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = (int)dataSnapshot.getChildrenCount();
                dataSnapshot.child(counter+"").child("userUID").getRef().setValue(userUid);
                dataSnapshot.child(counter+"").child("content").getRef().setValue(content);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getAllUsers(final GetUsersListListener listener){
        DatabaseReference usersReference = reference.getReference("Users");
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                ) {
                    User theUser = snapshot.getValue(User.class);
                    users.add(theUser);
                }
                listener.onComplete(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void currentUserLikePost(String postId, final String userUid){
        DatabaseReference postsReference = reference.getReference("Posts").child(postId).child("likeList");
        postsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Like newLike = new Like(userUid);
                int counter = (int)dataSnapshot.getChildrenCount();
                Log.d(TAG, "onDataChange:first  dataSnapshot.getChildrenCount: " +dataSnapshot.getChildrenCount());

                dataSnapshot.child(counter+"").child("userUID").getRef().setValue(userUid);
//                dataSnapshot.child(newLike.getUserUID()).getRef().setValue(newLike);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void currentUserUnlikePost(String postId, final String userUid){
        DatabaseReference postsReference = reference.getReference("Posts").child(postId).child("likeList");
        postsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange:first  ");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange:seconed  "+snapshot.toString());
                    if(snapshot.child("userUID").getValue().equals(userUid)) {
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getLikesPost(String postId, final GetLikesPostListener listener){
        DatabaseReference postsReference = reference.getReference("Posts").child(postId).child("likeList");
        postsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            private List<Like> postLikes = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Like value = snapshot.getValue(Like.class);
                    postLikes.add(value);
                    Log.d(TAG, "getLikesPost: onDataChange: snapshot" + snapshot.toString());
                }
                listener.onComplete(postLikes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getUserByUid(final String Uid, final GetUserByUidListener listener) {
        DatabaseReference postsReference = reference.getReference("Users");
        postsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("userUID").getValue().equals(Uid)) {
                        Log.d(TAG, "onDataChange: userUID = "+Uid);
                        User theUser = snapshot.getValue(User.class);
                        listener.onComplete(theUser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getProfileByUid(final String Uid, final GetProfileByUidListener listener){
        DatabaseReference profileReference = reference.getReference("Profiles");
        profileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                ) {
                    if(snapshot.child("userProfileID").getValue().equals(Uid)){
                        Profile theProfile = snapshot.getValue(Profile.class);
                        listener.onComplete(theProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void getSinflePostById(final String postId, final GetSinglePostListener listener){
        Log.d(TAG, "getSinflePostById: ");
        DatabaseReference postsReference = reference.getReference("Posts");
        postsReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("imageUrl").getValue().equals(postId)) {
                        Post thePost= snapshot.getValue(Post.class);
                        listener.onComplete(thePost);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getSpecificUserPost(final String userUID, final GetSpecificUserPostListener listener) {
        DatabaseReference postsReference = reference.getReference("Posts");
        postsReference.addValueEventListener(new ValueEventListener() {
            private List<Post> thePosts = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                thePosts.clear();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if(post.getUserUID().equals(userUID)){
                        thePosts.add(post);
                    }
                }
                listener.onComplete(thePosts);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    public void getAllPosts(final GetPostListener listener){
        Log.d(TAG, "getAllPosts: ");
        // Read from the database
        DatabaseReference postsReference = reference.getReference("Posts");
        postsReference.addValueEventListener(new ValueEventListener() {
            private List<Post> thePosts = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                thePosts.clear();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()
                ) {
                    try {
                        Post post = snapshot.getValue(Post.class);
                        thePosts.add(post);
                        Log.d(TAG, "onDataChange: snapshot" + snapshot.toString());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: ex = " +e.toString());
                    }
                }
                listener.onComplete(thePosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllReleviantPosts(final List<String> followingUsers, final GetRelevantPostListener listener) {
        DatabaseReference postsReference = reference.getReference("Posts");
        postsReference.addValueEventListener(new ValueEventListener() {
            private List<Post> thePosts = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                thePosts.clear();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()
                ) {
                    try {
                        Post post = snapshot.getValue(Post.class);
                        if(followingUsers.contains(post.getUserUID())) {
                            thePosts.add(post);
                        }
                        Log.d(TAG, "onDataChange: snapshot" + snapshot.toString());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: ex = " +e.toString());
                    }
                }
                listener.onComplete(thePosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public interface AddPostListener {
        void onComplete();
    }
    public interface GetPostListener {
        void onComplete(List<Post> postList);
    }

    public interface GetRelevantPostListener {
        void onComplete(List<Post> postList);
    }

    public interface GetSpecificUserPostListener {
        void onComplete(List<Post> postList);
    }
    public interface GetLikesPostListener {
        void onComplete(List<Like> postLikes);
    }
    public interface GetUserByUidListener {
        void onComplete(User user);
    }
    public interface GetPostByIdListener {
        void onComplete(Post post);
    }
    public interface GetProfileByUidListener {
        void onComplete(Profile profile);
    }

    public interface GetSinglePostListener {
        void onComplete(Post post);
    }
    public interface IsCurUserFollowOtherListener {
        void onComplete(Boolean aBoolean);
    }

    public interface GetPostsOfSpesificUserListener {
        void onComplete(List<Post> postsList);
    }

    public interface GetUsersListListener {
        void onComplete(List<User> postList);
    }

    public interface GetUsersFollowersListListener {
        void onComplete(List<String> postList);
    }
    public interface GetUsersFolloweringListListener {
        void onComplete(List<String> postList);
    }
    public interface GetCommentsOfPostListener {
        void onComplete(List<Comment> postList);
    }

    public interface AddUserToDBListener {
        void onComplete(User user);
    }
    public interface ChangeAboutOfPostListener {
        void onComplete(String caption);
    }

    public interface AddProfileToDBListener {
        void onComplete();
    }

    public interface AddPostToProfileListener {
        void onComplete();
    }



    public FirebaseDatabase getReference() {
        return reference;
    }

    public void setReference(FirebaseDatabase reference) {
        this.reference = reference;
    }
}
