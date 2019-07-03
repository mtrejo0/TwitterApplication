package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.ViewHolder>{


    private List<Tweet> mTweets;
    Context context;

    // pass in the twets to constructor
    public  TwitterAdapter(List<Tweet> tweets)
    {
        mTweets = tweets;
    }

    // for each row inflate the layout

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet,viewGroup,false);

        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }


    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }


    // bind the values based on the position of element

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        // get the data according to position

        Tweet tweet = mTweets.get(i);

        // populate views

        viewHolder.tvUserName.setText(tweet.user.name);
        viewHolder.tvBody.setText(tweet.body);

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(viewHolder.ivProfileImage);

        viewHolder.tvTime.setText(tweet.timeAgo);

        viewHolder.btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ComposeActivity.class);

                int pos = viewHolder.getAdapterPosition();

                Tweet tweet = mTweets.get(pos);

                String user = tweet.user.name;


                i.putExtra("@", "@"+user+" ");
                v.getContext().startActivity(i);



            }
        });



    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    // create view holder class

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public ImageButton btnRetweet;
        public TextView tvTime;


        public ViewHolder(View itemView)
        {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvBody = itemView.findViewById(R.id.tvBody);
            btnRetweet = itemView.findViewById(R.id.btnRetweet);
            tvTime = itemView.findViewById(R.id.tvTime);

        }

    }
}
