package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;
    //User user;
    // the view objects
    TextView tvScreenName;
    TextView tvBody;
    TextView tvTimeStamp;
    ImageView ivProfileImage;
    ImageView ivMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ivMedia = (ImageView) findViewById(R.id.ivMedia);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvScreenName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvTimeStamp.setText(tweet.getCreatedAt());


        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        if (!tweet.getTweet_URL().equals("none")) {
            Glide.with(this).load(tweet.getTweet_URL()).into(ivMedia);
        }
        //ivProfileImage.setVisibility(1);
    }
}