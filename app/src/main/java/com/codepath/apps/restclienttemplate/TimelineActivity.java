package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {


    private RestClient client;
    TwitterAdapter  tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    SwipeRefreshLayout swipeContainer;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // if the option to compose is clicked then start the comnpose activity
        if(item.getItemId() == R.id.miCompose)
        {
            startComposeActivity();
        }


        return super.onOptionsItemSelected(item);
    }


    public void startComposeActivity()
    {
        // start new intent with that request code
        Intent i = new Intent(this,ComposeActivity.class);

        startActivityForResult(i,100);

    }

    public void startComposeActivity(String at)
    {
        // start new intent with and @ sign in front

        Intent i = new Intent(this,ComposeActivity.class);

        i.putExtra("@","@"+at+" ");

        startActivityForResult(i,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // check request code and result code first

        // Use data parameter
        if(requestCode == 100 && resultCode == RESULT_OK)
        {
            // unwrap the tweet that was posted
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra(Tweet.class.getSimpleName()));
            // add to beginning of adapter
            tweets.add(0, tweet);
            // notify adapter and scroll up
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // styling

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#35CDE5")));


        client = RestApplication.getRestClient(this);

        // find the recycler view

        rvTweets = findViewById(R.id.rvTweet);
        // init the arraylist
        tweets = new ArrayList<>();
        // construct the adapter form the data source
        tweetAdapter = new TwitterAdapter(tweets);

        // recycler view setup
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        // set adapter

        rvTweets.setAdapter(tweetAdapter);

        // get tweets to display initially

        populateTimeline();


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);









    }



    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.



        client.getHomeTimeline(new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // clear current adapter
                tweetAdapter.clear();

                // populate the adapter again
                for(int i  = 0 ; i < response.length(); i++)
                {

                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));


                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size()-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                // end refresh icon
                swipeContainer.setRefreshing(false);



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterRefresh",errorResponse.toString());
                throwable.printStackTrace();
                // end refresh icon
                swipeContainer.setRefreshing(false);
            }
        });



    }


    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {


                // iterate through json array

                // for each entry desearlize it
                for(int i  = 0 ; i < response.length(); i++)
                {
                    // convert each obj to tweet model

                    // add tweet model to data source
                    // notify the adapter weve added an item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        // add tweet and notify adapter of change
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size()-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient",response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient",responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient",errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient",errorResponse.toString());
                throwable.printStackTrace();
            }
        });



    }


}
