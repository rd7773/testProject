package com.example.rd7773.roposo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import utils.AppController;
import utils.CircleImageView;
import utils.CursorRecyclerViewAdapter;
import utils.FeedImageView;
import utils.Utils;

/**
 * Created by rd7773 on 3/17/2016.
 */

public class DetailCursorAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private static final String TAG ="DetailCursorAdapter" ;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public DetailCursorAdapter(Activity context,Cursor cursor) {
        super(context, cursor);
        ctx = context;
    }


    private final int HEADER =0 , STORY =1;

    private class ViewHolderStory extends RecyclerView.ViewHolder{

        TextView tvTitle , tvDesc , tvLikeCount , tvCommentCount , tvLink ;
        TextView tvUserName , tvCreatedOn , tvFollow , tvLike ;
        CircleImageView profilePic;
        FeedImageView ivFeed;
        RelativeLayout rlFollow, rlLike, rlComment , rlContainer;
        ImageView ivLike;
        LinearLayout llAction;


        public ViewHolderStory(View itemView) {
            super(itemView);
            profilePic = (CircleImageView) itemView.findViewById(R.id.profilePic);
            tvUserName = (TextView) itemView.findViewById(R.id.name);
            tvCreatedOn = (TextView) itemView.findViewById(R.id.timestamp);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvLikeCount = (TextView) itemView.findViewById(R.id.tvLikeCount);
            tvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDescription);
            tvLink = (TextView) itemView.findViewById(R.id.tvStoryLink);
            tvLike = (TextView) itemView.findViewById(R.id.tvLike);
            rlLike = (RelativeLayout) itemView.findViewById(R.id.rlLike);
            rlFollow = (RelativeLayout) itemView.findViewById(R.id.rlFollow);
            rlComment = (RelativeLayout) itemView.findViewById(R.id.rlComment);
            rlContainer = (RelativeLayout) itemView.findViewById(R.id.container);
            llAction = (LinearLayout) itemView.findViewById(R.id.llAction);
            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);
            ivFeed = (FeedImageView) itemView.findViewById(R.id.feedImage1);
            rlFollow.setVisibility(View.GONE);
            //llAction.setVisibility(View.GONE);


        }
    }



    private class ViewHolderHeader extends RecyclerView.ViewHolder{

        TextView tvName , tvFollower , tvFollowing , tvAbout , tvUrl , tvHandle ;
        TextView tvCreatedOn , tvFollow ;
        CircleImageView profilePic;
        RelativeLayout rlFollow ;
        LinearLayout llContainer;


        public ViewHolderHeader(View itemView) {
            super(itemView);
            profilePic = (CircleImageView) itemView.findViewById(R.id.ivDP);
            tvCreatedOn = (TextView) itemView.findViewById(R.id.timestamp);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvFollower = (TextView) itemView.findViewById(R.id.tvFollowers);
            tvFollowing = (TextView) itemView.findViewById(R.id.tvFollowing);
            tvAbout = (TextView) itemView.findViewById(R.id.tvAbout);
            tvUrl = (TextView) itemView.findViewById(R.id.tvProfileLink);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvFollow = (TextView) itemView.findViewById(R.id.tvFollow);
            rlFollow = (RelativeLayout) itemView.findViewById(R.id.rlFollow);
            llContainer = (LinearLayout) itemView.findViewById(R.id.llMain);

        }
    }

    Activity ctx;


    @Override
    public RecyclerView.ViewHolder newViewHolder(ViewGroup parent, int viewType) {
        if(viewType==HEADER){
            View v = LayoutInflater.from(ctx).inflate(R.layout.user_header, parent,false);
            return new ViewHolderHeader(v);
        }
        View v = LayoutInflater.from(ctx).inflate(R.layout.post_item, parent, false);
        return new ViewHolderStory(v);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder, int position, Cursor cursor) {
        final Story story = Story.fromCursor(cursor);
        final UserProfile userProfile = UserProfile.fromCursor(cursor);

        if(getItemViewType(position)==HEADER){

            final ViewHolderHeader holder = (ViewHolderHeader) viewHolder;
            if (userProfile != null) {

                holder.tvName.setText(userProfile.getUserName());

                String imageurl = userProfile.getImageUrl();
                if (imageurl != null && imageurl.trim().length() > 0) {
                    holder.profilePic.setImageUrl(imageurl, imageLoader);
                } else {
                    holder.profilePic.setImageResource(R.drawable.profile27);
                }

                holder.tvAbout.setText(userProfile.getAbout());
                holder.tvFollower.setText(Utils.format(userProfile.getFollowers()));
                holder.tvFollowing.setText(Utils.format(userProfile.getFollowing()));
                holder.tvHandle.setText(userProfile.getHandle());
                holder.tvUrl.setText(userProfile.getProfileUrl());

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

                    userProfile.setFollowing(userProfile.isFollowing?false:true);
                    if(userProfile.isFollowing){

                        holder.tvFollow.setText("Following");
                        holder.rlFollow.setActivated(true);
                    }else{

                        holder.tvFollow.setText("Follow");
                        holder.rlFollow.setActivated(false);
                    }
                }
            });

        }else
        {
            ViewHolderStory holder = (ViewHolderStory) viewHolder;

            if (story != null) {

                holder.tvTitle.setText(story.getTitle());
                holder.tvDesc.setText(story.getDescription());
                holder.tvCreatedOn.setText(story.getCreatedOn());
                holder.tvLikeCount.setText(String.valueOf(story.getLikeCount()) + " Likes");
                holder.tvCommentCount.setText(String.valueOf(story.getCommentCount()) + " Comments");
                holder.tvLink.setText(story.getStoryUrl());

                if (story.isLiked()) {
                    holder.ivLike.setImageResource(R.drawable.liked);
                    holder.tvLike.setText("Liked");
                } else {
                    holder.ivLike.setImageResource(R.drawable.like);
                    holder.tvLike.setText("Like");
                }
                String imageurl = story.getImageUrl();

                if (imageurl != null && imageurl.trim().length() > 0) {
                    holder.ivFeed.setImageUrl(imageurl, imageLoader);
                } else {
                    holder.ivFeed.setVisibility(View.GONE);
                }
            }


            if (userProfile != null) {

                holder.tvUserName.setText(userProfile.getUserName());

                String imageurl = userProfile.getImageUrl();
                if (imageurl != null && imageurl.trim().length() > 0) {
                    holder.profilePic.setImageUrl(imageurl, imageLoader);
                } else {
                    holder.profilePic.setImageResource(R.drawable.profile27);
                }

            }

            holder.rlLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ContentValues values = new ContentValues();
                    values.put(DataProvider.COL_STORY_IS_LIKED, story.isLiked()?0:1);
                    int count = ctx.getContentResolver().update(DataProvider.CONTENT_URI_STORY_ITEMS,
                            values, DataProvider.COL_STORY_ID+"='"+story.getStoryId()+"'", null);

                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position==0?HEADER:STORY;
    }



}
