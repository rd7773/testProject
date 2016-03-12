package com.example.rd7773.roposo;

import java.util.ArrayList;

/**
 * Created by rd7773 on 3/11/2016.
 */
public class UserProfile {

    String userId;
    String userName;
    String imageUrl, profileUrl;
    int followers,following;
    long userSince;
    boolean isFollowing;
    String about;
    String handle;


    public static class Builder{

        private final String userId;
        private final String userName;
        private final String imageUrl;

        private String profileUrl;
        private int followers,following;
        private long userSince;
        private boolean isFollowing;
        private String about;
        private String handle;

        public Builder(String userId, String userName, String imageUrl) {
            this.userId = userId;
            this.userName = userName;
            this.imageUrl = imageUrl;
        }

        public Builder profileUrl(String val){
            profileUrl = val;
            return this;
        }

        public Builder followers(int val){

            followers = val;
            return this;
        }

        public Builder about(String val){
            about = val;
            return this;
        }

        public Builder handle(String val){
            handle = val;
            return this;
        }

        public Builder followings(int val){

            following = val;
            return this;
        }

        public Builder userCreatedOn(long val){

            userSince = val;
            return this;
        }

        public Builder setFollowing(boolean val){

            isFollowing = val;
            return this;
        }

        public UserProfile build(){

            return new UserProfile(this);

        }

    }

    private UserProfile(Builder builder){

        userId = builder.userId;
        userName = builder.userName;
        imageUrl = builder.imageUrl;
        followers = builder.followers;
        following = builder.following;
        profileUrl = builder.profileUrl;
        userSince = builder.userSince;
        isFollowing = builder.isFollowing;
        about = builder.about;
        handle = builder.handle;


    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public long getUserSince() {
        return userSince;
    }

    public void setUserSince(long userSince) {
        this.userSince = userSince;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }
}
