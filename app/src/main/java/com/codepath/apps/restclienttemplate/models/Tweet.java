package com.codepath.apps.restclienttemplate.models;

import android.icu.text.SimpleDateFormat;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.core.net.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {


    public String body;
    public String createdAt;
    public User user;
    public String tweet_URL;
    public Boolean favorited;
    public long id;
    public Boolean retweeted;

    //public String entities;

    //empty constructor needed by the Parceler library
    public Tweet(){

    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        if (jsonObject.has("retweeted")){
            tweet.retweeted = jsonObject.getBoolean("retweeted");

        }
        if (jsonObject.has("favorited")){
            tweet.favorited = jsonObject.getBoolean("favorited");

        }
        if(jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }

        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");

        if(!jsonObject.getJSONObject("entities").has("media")){
            Log.d("TWEET", "No pic");
            tweet.tweet_URL = "none";
        }
        else{
            Log.d("TWEET has pic", jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url"));
            tweet.tweet_URL = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
        }


        return tweet;

    }
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i <jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));

        }
        return tweets;


    }
    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public String getTweet_URL() {
        return tweet_URL;
    }
    public Boolean getFavorited(){return favorited;}
}
