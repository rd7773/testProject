package com.example.rd7773.roposo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.AppController;
import utils.CircleImageView;
import utils.FeedImageView;

/**
 * Created by rd7773 on 3/11/2016.
 */
public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private class ViewHolderStory extends RecyclerView.ViewHolder{

        TextView tvTitle , tvDesc , tvLikeCount , tvCommentCount , tvLink ;
        TextView tvUserName , tvCreatedOn , tvFollow , tvLike ;
        CircleImageView profilePic;
        FeedImageView ivFeed;
        RelativeLayout rlFollow, rlLike, rlComment , rlContainer;
        ImageView ivLike;


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
            tvFollow = (TextView) itemView.findViewById(R.id.tvFollow);
            rlLike = (RelativeLayout) itemView.findViewById(R.id.rlLike);
            rlFollow = (RelativeLayout) itemView.findViewById(R.id.rlFollow);
            rlComment = (RelativeLayout) itemView.findViewById(R.id.rlComment);
            rlContainer = (RelativeLayout) itemView.findViewById(R.id.container);
            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);
            ivFeed = (FeedImageView) itemView.findViewById(R.id.feedImage1);



            rlContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String userId = storyList.get(getAdapterPosition()).getUserId();

                    ArrayList<Story> stories = userStoryList.get(userId);
                    if(stories==null)
                    {
                        stories = new ArrayList<Story>();
                        for(Story story : storyList)
                        {

                            if(story.getUserId().equals(userId))
                                stories.add(story);

                        }

                        userStoryList.put(userId,stories);
                    }

                    UserProfile userProfile = usersList.get(userId);

                    Intent i = new Intent(ctx,UserDetailActivity.class);
                    i.putExtra("USER", new Gson().toJson(userProfile));
                    i.putExtra("LIST", new Gson().toJson(stories));
                    ctx.startActivityForResult(i,0);


                }
            });




        }
    }

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private HashMap<String , UserProfile> usersList;
    private List<Story> storyList;
    Activity ctx;
    private HashMap<String , ArrayList<Story>> userStoryList;

    public StoryAdapter(HashMap<String, UserProfile> usersList, List<Story> storyList, Activity ctx) {
        this.usersList = usersList;
        this.storyList = storyList;
        userStoryList = new HashMap<>();
        this.ctx = ctx;
    }

    public Story getItem(int position) {
        return storyList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.post_item, parent,false);
        return new ViewHolderStory(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final ViewHolderStory holder = (ViewHolderStory)viewHolder;
        Story story = getItem(position);
        final UserProfile userProfile = usersList.get(story.userId);

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

                userProfile.setFollowing(userProfile.isFollowing?false:true);
                /*if(userProfile.isFollowing){

                    holder.tvFollow.setText("Following");
                    holder.rlFollow.setActivated(true);
                }else{

                    holder.tvFollow.setText("Follow");
                    holder.rlFollow.setActivated(false);
                }*/

                notifyDataSetChanged();
            }
        });

        holder.rlLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Story story = storyList.get(position);
                story.setLiked(story.isLiked()?false:true);

                if(story.isLiked()){
                    holder.ivLike.setImageResource(R.drawable.liked);
                    holder.tvLike.setText("Liked");
                }else{
                    holder.ivLike.setImageResource(R.drawable.like);
                    holder.tvLike.setText("Like");
                }
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data!=null){

            String str = data.getStringExtra("USER");
            UserProfile userProfile = new Gson().fromJson(str,UserProfile.class);
            usersList.put(userProfile.getUserId(),userProfile);
            notifyDataSetChanged();
        }

    }

}
