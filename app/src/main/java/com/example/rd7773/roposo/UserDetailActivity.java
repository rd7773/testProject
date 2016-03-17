package com.example.rd7773.roposo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class UserDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    DetailCursorAdapter cursorAdapter;
    RecyclerView listview;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_user_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview = (RecyclerView) findViewById(R.id.list1);

        LinearLayoutManager lm = new LinearLayoutManager(this);

        userId = getIntent().getStringExtra("USER_ID");

        if(StaticInstance.isCursorAdapter){

            cursorAdapter = new DetailCursorAdapter(this,null);
            listview.setAdapter(cursorAdapter);
            getSupportLoaderManager().initLoader(0,null,this);

        }else{

            UserProfile userProfile = StaticInstance.usersMap.get(userId);
            String str1 = getIntent().getStringExtra("LIST");
            ArrayList<Story> storyList = new Gson().fromJson(str1, new TypeToken<ArrayList<Story>>(){}.getType());
            DetailAdapter adapter = new DetailAdapter(userProfile,storyList,this);
            listview.setAdapter(adapter);
            setTitle(userProfile.getUserName());
        }


        listview.setLayoutManager(lm);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                DataProvider.CONTENT_URI_JOINED,
                null,
                " where user_master.userId = '"+userId+"'",
                null,null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
