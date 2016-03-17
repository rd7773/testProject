package com.example.rd7773.roposo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rd7773 on 3/16/2016.
 */

public class Database extends SQLiteOpenHelper {

    Context con;
    static String DATABASE_NAME = "Roposo.db";
    static int DATABASE_VERSION = 1;


    public static String STORIES_TABLE = "story_table";
    public static String USER_MASTER = "user_master";


    String DATABASE_CREATE_STORIES_TABLE = "CREATE TABLE IF NOT EXISTS " +
            STORIES_TABLE +
            " " +
            "(_id integer primary key autoincrement , "+
            "storyId TEXT, "+
            "title TEXT, "+
            "storyImageUrl TEXT, "+
            "storyUrl TEXT, "+
            "createdOn TEXT, "+
            "description TEXT, "+
            "isLiked NUMBER default 0, "+
            "likeCount NUMBER, "+
            "commentCount NUMBER, "+
            "storyUserId TEXT)";


    String DATABASE_CREATE_USER_MASTER = "CREATE TABLE IF NOT EXISTS " +
            USER_MASTER +
            " " +
            "(_id integer primary key autoincrement , "+
            "userId TEXT, "+
            "userName TEXT, "+
            "imageUrl TEXT, "+
            "profileUrl TEXT, "+
            "userSince NUMBER, "+
            "about TEXT, "+
            "isFollowing NUMBER default 0, "+
            "followers NUMBER, "+
            "following NUMBER, "+
            "handle TEXT)";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.con = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE_STORIES_TABLE);
        db.execSQL(DATABASE_CREATE_USER_MASTER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }



}