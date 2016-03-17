package com.example.rd7773.roposo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "StoryActivity";
    private List<UserProfile> usersList;
    StoryAdapter adapter;
    StoryCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        usersList = new ArrayList<>();

        parseJsonData();

        if(StaticInstance.isCursorAdapter)
        {
            setContentView(R.layout.activity_story_new);

            ListView listView = (ListView) findViewById(R.id.list1);
            cursorAdapter = new StoryCursorAdapter(this,null,0);
            listView.setAdapter(cursorAdapter);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                listView.setNestedScrollingEnabled(true);
            }

            getSupportLoaderManager().initLoader(0,null,this);

        }else{

            setContentView(R.layout.activity_story);

            LinearLayoutManager lm = new LinearLayoutManager(this);
            RecyclerView listView = (RecyclerView) findViewById(R.id.list1);
            adapter = new StoryAdapter(StaticInstance.usersMap,StaticInstance.storyList,this);
            adapter.setHasStableIds(true);
            listView.setAdapter(adapter);
            listView.setLayoutManager(lm);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
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

                    StaticInstance.usersMap.put(id,userProfile);
                    usersList.add(userProfile);


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

                    StaticInstance.storyList.add(story);

                }catch (Exception e){
                    e.printStackTrace();
                }


            }

            Story.insertStories(StaticInstance.storyList,this);
            UserProfile.insertUsersList(usersList,this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                DataProvider.CONTENT_URI_JOINED,
                null,
                "",
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
