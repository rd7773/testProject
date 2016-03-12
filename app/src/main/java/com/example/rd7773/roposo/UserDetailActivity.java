package com.example.rd7773.roposo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDetailActivity extends AppCompatActivity {

    DetailAdapter adapter;
    RecyclerView listview;
    UserProfile userProfile;
    ArrayList<Story> storyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String str = getIntent().getStringExtra("USER");
        userProfile = new Gson().fromJson(str,UserProfile.class);

        String str1 = getIntent().getStringExtra("LIST");
        storyList = new Gson().fromJson(str1, new TypeToken<ArrayList<Story>>(){}.getType());
        LinearLayoutManager lm = new LinearLayoutManager(this);
        adapter = new DetailAdapter(userProfile,storyList,this);
        listview = (RecyclerView) findViewById(R.id.list1);
        setTitle(userProfile.getUserName());
        storyList = new ArrayList<>();

        listview.setAdapter(adapter);
        listview.setLayoutManager(lm);

    }


    @Override
    public void onBackPressed() {

        Intent i = new Intent();
        i.putExtra("USER", new Gson().toJson(userProfile));
        setResult(RESULT_OK,i);
        finish();

    }
}
