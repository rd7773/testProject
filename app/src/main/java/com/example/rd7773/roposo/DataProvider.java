package com.example.rd7773.roposo;

/**
 * Created by rd7773 on 3/16/2016.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


public class DataProvider extends ContentProvider {

    public static final Uri CONTENT_URI_STORY_ITEMS = Uri.parse("content://com.example.rd7773.roposo.provider/stories");
    public static final Uri CONTENT_URI_USERS = Uri.parse("content://com.example.rd7773.roposo.provider/users");
    public static final Uri CONTENT_URI_JOINED = Uri.parse("content://com.example.rd7773.roposo.provider/joined");

    public static final String COL_ID = "_id";

    public static final String TABLE_STORIES = "story_table";
    public static final String COL_STORY_ID = "storyId";
    public static final String COL_STORY_TITLE = "title";
    public static final String COL_STORY_IMG_URL = "storyImageUrl";
    public static final String COL_STORY_URL = "storyUrl";
    public static final String COL_STORY_ON = "createdOn";
    public static final String COL_STORY_DESC = "description";
    public static final String COL_STORY_IS_LIKED = "isLiked";
    public static final String COL_STORY_LIKE_COUNT = "likeCount";
    public static final String COL_STORY_COMMENT_COUNT = "commentCount";
    public static final String COL_STORY_USER_ID = "storyUserId";
    
    public static final String TABLE_USER_MASTER = "user_master";
    public static final String COL_USER_NAME = "userName";
    public static final String COL_USER_ID = "userId";
    public static final String COL_USER_IMG_URL = "imageUrl";
    public static final String COL_USER_PROFILE_IMG_URL = "profileUrl";
    public static final String COL_USER_USER_SINCE = "userSince";
    public static final String COL_USER_ABOUT = "about";
    public static final String COL_USER_IS_FOLLOWING = "isFollowing";
    public static final String COL_USER_FOLLOWERS = "followers";
    public static final String COL_USER_FOLLOWING = "following";
    public static final String COL_USER_HANDLE = "handle";
    private static final String TAG = "DataProvider";


    private Database dbHelper;

    private static final int STORY_ALLROWS = 1;
    private static final int STORY_SINGLE_ROW = 2;
    private static final int USERS_ALLROWS = 3;
    private static final int USERS_SINGLE_ROW = 4;
    private static final int JOINED_ROW = 5;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.rd7773.roposo.provider", "stories", STORY_ALLROWS);
        uriMatcher.addURI("com.example.rd7773.roposo.provider", "stories/#", STORY_SINGLE_ROW);
        uriMatcher.addURI("com.example.rd7773.roposo.provider", "users", USERS_ALLROWS);
        uriMatcher.addURI("com.example.rd7773.roposo.provider", "users/#", USERS_SINGLE_ROW);
        uriMatcher.addURI("com.example.rd7773.roposo.provider", "joined", JOINED_ROW);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new Database(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch(uriMatcher.match(uri)) {
            case STORY_ALLROWS:
                qb.setTables(TABLE_STORIES);
                break;

            case STORY_SINGLE_ROW:
                qb.setTables(TABLE_STORIES);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;

            case USERS_ALLROWS:
                qb.setTables(TABLE_USER_MASTER);
                break;

            case USERS_SINGLE_ROW:
                qb.setTables(TABLE_USER_MASTER);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;

            case JOINED_ROW:
                Log.i(TAG, "query: selection : "+selection);
                Cursor c = db.rawQuery("select story_table.* , user_master.* "
                        + " from story_table "
                        + " left join user_master on user_master.userId = story_table.storyUserId "
                        + selection, selectionArgs);

                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id;
        switch(uriMatcher.match(uri)) {
            case STORY_ALLROWS:
                id = db.insertOrThrow(TABLE_STORIES, null, values);
                break;

            case USERS_ALLROWS:
                id = db.insertOrThrow(TABLE_USER_MASTER, null, values);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Uri insertUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(insertUri, null);
        getContext().getContentResolver().notifyChange(CONTENT_URI_JOINED, null);
        return insertUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int count;
        switch(uriMatcher.match(uri)) {
            case STORY_ALLROWS:
                count = db.update(TABLE_STORIES, values, selection, selectionArgs);
                break;

            case STORY_SINGLE_ROW:
                count = db.update(TABLE_STORIES, values, "_id = ?", new String[]{uri.getLastPathSegment()});
                break;

            case USERS_ALLROWS:
                count = db.update(TABLE_USER_MASTER, values, selection, selectionArgs);
                break;

            case USERS_SINGLE_ROW:
                count = db.update(TABLE_USER_MASTER, values, "_id = ?", new String[]{uri.getLastPathSegment()});
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        getContext().getContentResolver().notifyChange(CONTENT_URI_JOINED, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int count;
        switch(uriMatcher.match(uri)) {
            case STORY_ALLROWS:
                count = db.delete(TABLE_STORIES, selection, selectionArgs);
                break;

            case STORY_SINGLE_ROW:
                count = db.delete(TABLE_STORIES, "_id = ?", new String[]{uri.getLastPathSegment()});
                break;

            case USERS_ALLROWS:
                count = db.delete(TABLE_USER_MASTER, selection, selectionArgs);
                break;

            case USERS_SINGLE_ROW:
                count = db.delete(TABLE_USER_MASTER, "_id = ?", new String[]{uri.getLastPathSegment()});
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        getContext().getContentResolver().notifyChange(CONTENT_URI_JOINED, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


}