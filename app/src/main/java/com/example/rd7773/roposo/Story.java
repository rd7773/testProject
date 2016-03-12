package com.example.rd7773.roposo;

/**
 * Created by rd7773 on 3/11/2016.
 */
public class Story {

    String storyId;
    String title;
    String imageUrl, storyUrl;
    int likeCount, commentCount;
    String createdOn;
    boolean isLiked;
    String description;
    String userId;

    public static class Builder{

        private final String storyId;
        private final String title;
        private final String imageUrl;

        private String storyUrl;
        private int likeCount, commentCount;
        private String createdOn;
        private boolean isLiked;
        private String description;
        private String userId;

        public Builder(String storyId, String title, String imageUrl) {
            this.storyId = storyId;
            this.title = title;
            this.imageUrl = imageUrl;
        }

        public Builder storyUrl(String val){
            storyUrl = val;
            return this;
        }

        public Builder likeCount(int val){

            likeCount = val;
            return this;
        }

        public Builder description(String val){
            description = val;
            return this;
        }

        public Builder commentCount(int val){

            commentCount = val;
            return this;
        }

        public Builder createdOn(String val){

            createdOn = val;
            return this;
        }

        public Builder userId(String val){

            userId = val;
            return this;
        }
        public Builder isLiked(boolean val){

            isLiked = val;
            return this;
        }

        public Story build(){

            return new Story(this);

        }

    }

    private Story(Builder builder){

        storyId = builder.storyId;
        title = builder.title;
        imageUrl = builder.imageUrl;
        likeCount = builder.likeCount;
        commentCount = builder.commentCount;
        storyUrl = builder.storyUrl;
        createdOn = builder.createdOn;
        isLiked = builder.isLiked;
        description = builder.description;
        userId = builder.userId;


    }


    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStoryUrl() {
        return storyUrl;
    }

    public void setStoryUrl(String storyUrl) {
        this.storyUrl = storyUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
