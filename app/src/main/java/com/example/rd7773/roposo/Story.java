package com.example.rd7773.roposo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

/**
 * Created by rd7773 on 3/11/2016.
 */
public class Story {

    private static final String TAG = "Story";
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

    public static Story fromCursor(Cursor cursor){


        String storyId = cursor.getString(cursor.getColumnIndex(DataProvider.COL_STORY_ID));
        String title = cursor.getString(cursor.getColumnIndex(DataProvider.COL_STORY_TITLE));
        String imageUrl = cursor.getString(cursor.getColumnIndex(DataProvider.COL_STORY_IMG_URL));
        String storyUrl = cursor.getString(cursor.getColumnIndex(DataProvider.COL_STORY_URL));
        int likeCount = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_STORY_LIKE_COUNT));
        int commentCount = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_STORY_COMMENT_COUNT));
        String createdOn = cursor.getString(cursor.getColumnIndex(DataProvider.COL_STORY_ON));
        boolean isLiked = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_STORY_IS_LIKED))==1?true:false;
        String description = cursor.getString(cursor.getColumnIndex(DataProvider.COL_STORY_DESC));
        String userId = cursor.getString(cursor.getColumnIndex(DataProvider.COL_STORY_USER_ID));

        Story story = new Story.Builder(storyId,title,imageUrl)
                .storyUrl(storyUrl)
                .description(description)
                .userId(userId)
                .commentCount(commentCount)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .createdOn(createdOn)
                .build();

        return story;

    }

    public static void insertStories(List<Story> list, Context ctx){
        Cursor c = null;
       // Database db = new Database(ctx);
        //SQLiteDatabase ropoDB = db.getWritableDatabase();
        int i =0;
        for(Story story : list){
            i++;
            ContentValues values = new ContentValues();
            values.put(DataProvider.COL_STORY_ID,story.getStoryId());
            values.put(DataProvider.COL_STORY_TITLE,story.getTitle());
            values.put(DataProvider.COL_STORY_IMG_URL,story.getImageUrl());
            values.put(DataProvider.COL_STORY_URL,story.getStoryUrl());
            values.put(DataProvider.COL_STORY_LIKE_COUNT,story.getLikeCount());
            values.put(DataProvider.COL_STORY_COMMENT_COUNT,story.getCommentCount());
            values.put(DataProvider.COL_STORY_ON,story.getCreatedOn());
            values.put(DataProvider.COL_STORY_IS_LIKED,story.isLiked()?1:0);
            values.put(DataProvider.COL_STORY_DESC,story.getDescription());
            values.put(DataProvider.COL_STORY_USER_ID,story.getUserId());


            c = ctx.getContentResolver().query(DataProvider.CONTENT_URI_STORY_ITEMS, null, DataProvider.COL_STORY_ID+"='"+story.getStoryId()+"'", null, null);
           // c = ropoDB.rawQuery("select _id from story_table where "+ DataProvider.COL_STORY_ID+"='"+story.getStoryId()+"'",null);
            if(c != null && c.getCount() > 0){
                //ctx.getContentResolver().update(DataProvider.CONTENT_URI_STORY_ITEMS, values, DataProvider.COL_STORY_ID+"='"+story.getStoryId()+"'", null);
            }else
                ctx.getContentResolver().insert(DataProvider.CONTENT_URI_STORY_ITEMS, values);

            c.close();

        }
    }


}
