package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {

    // list of attributes
    public String body;
    // db for user
    public long uid;

    public User user;

    public String createdAt;


    // deserialize the JSON

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");

        Log.d("TWEET",jsonObject.getJSONObject("user").toString());



        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;
    }
}
