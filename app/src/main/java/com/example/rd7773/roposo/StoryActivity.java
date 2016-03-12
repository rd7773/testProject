package com.example.rd7773.roposo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoryActivity extends AppCompatActivity {

    private static final String TAG = "StoryActivity";
    private HashMap<String , UserProfile> usersList;
    private List<Story> storyList;
    RecyclerView listView;
    StoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        listView = (RecyclerView) findViewById(R.id.list1);
        setTitle("");
        storyList = new ArrayList<>();
        usersList = new HashMap<>();

        parseJsonData();
        adapter = new StoryAdapter(usersList,storyList,this);
        listView.setAdapter(adapter);
        listView.setLayoutManager(lm);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("AndroidData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void parseJsonData(){

        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
            int i =0;

            for(i=0;i<jsonArray.length();i++){

                JSONObject jsonMain = jsonArray.getJSONObject(i);

                try{

                    String username = jsonMain.getString("username");
                    String id = jsonMain.getString("id");
                    String image = jsonMain.getString("image");
                    String about = jsonMain.getString("about");
                    String url = jsonMain.getString("url");
                    String handle = jsonMain.getString("handle");
                    int followers = jsonMain.getInt("followers");
                    int following = jsonMain.getInt("following");
                    long createdOn = jsonMain.getLong("createdOn");
                    boolean is_following = jsonMain.getBoolean("is_following");

                    UserProfile userProfile = new UserProfile.Builder(id,username,image)
                                                    .about(about)
                                                    .profileUrl(url)
                                                    .handle(handle)
                                                    .followers(followers)
                                                    .followings(following)
                                                    .userCreatedOn(createdOn)
                                                    .setFollowing(is_following)
                                                    .build();

                    usersList.put(id,userProfile);

                }catch (Exception e){
                    e.printStackTrace();
                    break;
                }




            }


            for(int j = i;j<jsonArray.length();j++){

                JSONObject jsonMain = jsonArray.getJSONObject(j);

                try{

                    String title = jsonMain.getString("title");
                    String id = jsonMain.getString("id");
                    String image = jsonMain.getString("si");
                    String description = jsonMain.getString("description");
                    String url = jsonMain.getString("url");
                    String userId = jsonMain.getString("db");
                    String createdOn = jsonMain.getString("verb");
                    boolean like_flag = jsonMain.getBoolean("like_flag");
                    int likes_count = jsonMain.getInt("likes_count");
                    int comment_count = jsonMain.getInt("comment_count");

                    Story story = new Story.Builder(id,title,image)
                            .storyUrl(url)
                            .description(description)
                            .userId(userId)
                            .commentCount(comment_count)
                            .likeCount(likes_count)
                            .isLiked(like_flag)
                            .createdOn(createdOn)
                            .build();

                    storyList.add(story);

                }catch (Exception e){
                    e.printStackTrace();
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.onActivityResult(requestCode,resultCode,data);
    }
}
