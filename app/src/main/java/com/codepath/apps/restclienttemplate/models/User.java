package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    // attriubutes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;

    // deserialize the Json

    public static User fromJSON(JSONObject json) throws JSONException {


        User user = new User();


        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url");


        return user;

    }


}
