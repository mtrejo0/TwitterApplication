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
        // create a view holder that has ref to item_tweet
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

        // load image
        Glide.with(context)
                .load(tweet.user.profileImageUrl)

                .into(viewHolder.ivProfileImage);

        // set text to how long ago the tweet was made
        viewHolder.tvTime.setText(tweet.timeAgo);

        // set the handle
        viewHolder.tvHandle.setText("@"+tweet.user.screenName);

        // on click listener for if the retweet button was clicked
        viewHolder.btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),ComposeActivity.class);

                // get tweet at that position int the adapter
                int pos = viewHolder.getAdapterPosition();

                Tweet tweet = mTweets.get(pos);

                String user = tweet.user.screenName;

                // populate what the beginning text should be and create new intent

                i.putExtra("@", "@"+user+" ");
                v.getContext().startActivity(i);

//                TimelineActivity.startComposeActivity(user);



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
        public TextView tvHandle;


        public ViewHolder(View itemView)
        {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvBody = itemView.findViewById(R.id.tvBody);
            btnRetweet = itemView.findViewById(R.id.btnRetweet);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvHandle = itemView.findViewById(R.id.tvHandle);

        }

    }
}
