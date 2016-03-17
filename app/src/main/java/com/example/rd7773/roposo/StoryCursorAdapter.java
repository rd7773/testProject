package com.example.rd7773.roposo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import utils.AppController;
import utils.CircleImageView;
import utils.FeedImageView;

/**
 * Created by rd7773 on 3/16/2016.
 */
public class StoryCursorAdapter extends CursorAdapter {

    private static final String TAG ="StoryCursorAdapter" ;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public StoryCursorAdapter(Activity context, Cursor c, int flags) {
        super(context, c, flags);
        ctx = context;
    }


    private class ViewHolderStory {

        TextView tvTitle , tvDesc , tvLikeCount , tvCommentCount , tvLink ;
        TextView tvUserName , tvCreatedOn , tvFollow , tvLike ;
        CircleImageView profilePic;
        FeedImageView ivFeed;
        RelativeLayout rlFollow, rlLike, rlComment , rlContainer;
        ImageView ivLike;


        public ViewHolderStory(View itemView) {

            profilePic = (CircleImageView) itemView.findViewById(R.id.profilePic);
            tvUserName = (TextView) itemView.findViewById(R.id.name);
            tvCreatedOn = (TextView) itemView.findViewById(R.id.timestamp);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvLikeCount = (TextView) itemView.findViewById(R.id.tvLikeCount);
            tvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDescription);
            tvLink = (TextView) itemView.findViewById(R.id.tvStoryLink);
            tvLike = (TextView) itemView.findViewById(R.id.tvLike);
            tvFollow = (TextView) itemView.findViewById(R.id.tvFollow);
            rlLike = (RelativeLayout) itemView.findViewById(R.id.rlLike);
            rlFollow = (RelativeLayout) itemView.findViewById(R.id.rlFollow);
            rlComment = (RelativeLayout) itemView.findViewById(R.id.rlComment);
            rlContainer = (RelativeLayout) itemView.findViewById(R.id.container);
            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);
            ivFeed = (FeedImageView) itemView.findViewById(R.id.feedImage1);


        }
    }


    Activity ctx;

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.post_item, parent, false);
        ViewHolderStory holder = new ViewHolderStory(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolderStory holder = (ViewHolderStory)view.getTag();
        final int id = cursor.getInt(cursor.getColumnIndex(DataProvider.COL_ID));
        final Story story = Story.fromCursor(cursor);
        final UserProfile userProfile = UserProfile.fromCursor(cursor);

        if(story!=null){

            holder.tvTitle.setText(story.getTitle());
            holder.tvDesc.setText(story.getDescription());
            holder.tvCreatedOn.setText(story.getCreatedOn());
            holder.tvLikeCount.setText(String.valueOf(story.getLikeCount())+" Likes");
            holder.tvCommentCount.setText(String.valueOf(story.getCommentCount())+" Comments");
            holder.tvLink.setText(story.getStoryUrl());

            if(story.isLiked()){
                holder.ivLike.setImageResource(R.drawable.liked);
                holder.tvLike.setText("Liked");
            }else{
                holder.ivLike.setImageResource(R.drawable.like);
                holder.tvLike.setText("Like");
            }

            String imageurl = story.getImageUrl();

            if(imageurl!=null&&imageurl.trim().length()>0){
                holder.ivFeed.setImageUrl(imageurl,imageLoader);
            }else {
                holder.ivFeed.setVisibility(View.GONE);
            }
        }


        if(userProfile!=null){

            holder.tvUserName.setText(userProfile.getUserName());

            String imageurl = userProfile.getImageUrl();
            if(imageurl!=null&&imageurl.trim().length()>0){
                holder.profilePic.setImageUrl(imageurl,imageLoader);
            }else {
                holder.profilePic.setImageResource(R.drawable.profile27);
            }

            if(userProfile.isFollowing){

                holder.tvFollow.setText("Following");
                holder.rlFollow.setActivated(true);
            }else{

                holder.tvFollow.setText("Follow");
                holder.rlFollow.setActivated(false);
            }

        }

        holder.rlFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                values.put(DataProvider.COL_USER_IS_FOLLOWING, userProfile.isFollowing?0:1);
               int count =  ctx.getContentResolver().update(DataProvider.CONTENT_URI_USERS ,
                        values, DataProvider.COL_USER_ID+"='"+userProfile.getUserId()+"'", null);

            }
        });

        holder.rlLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ContentValues values = new ContentValues();
                values.put(DataProvider.COL_STORY_IS_LIKED, story.isLiked()?0:1);
                int count = ctx.getContentResolver().update(DataProvider.CONTENT_URI_STORY_ITEMS,
                        values, DataProvider.COL_STORY_ID+"='"+story.getStoryId()+"'", null);
                /*story.setLiked(story.isLiked()?false:true);

                if(story.isLiked()){
                    holder.ivLike.setImageResource(R.drawable.liked);
                    holder.tvLike.setText("Liked");
                }else{
                    holder.ivLike.setImageResource(R.drawable.like);
                    holder.tvLike.setText("Like");
                }*/
            }
        });

        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent i = new Intent(ctx,UserDetailActivity.class);
                i.putExtra("USER_ID", userProfile.getUserId());
                i.putExtra("NAME", userProfile.getUserName());
                ctx.startActivity(i);


            }
        });

    }
}
