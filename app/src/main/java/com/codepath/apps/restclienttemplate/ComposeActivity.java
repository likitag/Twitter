package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    EditText etCompose;
    Button btnTweet;

    TwitterClient client;
    TextView tvCharacterCount;

    TextView tvReply;
    //ImageButton imgBtnTweet;

    int color;


    public static final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = "ComposeActivity";
    MenuItem miActionProgressItem;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);

        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }
    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
       // imgBtnTweet = findViewById(R.id.imgBtnTweet);
        client = TwitterApp.getRestClient(this);
        //color = btnTweet.getCurrentTextColor();
        tvCharacterCount = findViewById(R.id.tvCharacterCountResponse);
        tvCharacterCount.setText(String.valueOf(MAX_TWEET_LENGTH));
        tvReply = findViewById(R.id.tvReply);

        if (getIntent().hasExtra("reply_username")) {
            Intent intent = getIntent();
            String username = intent.getStringExtra("reply_username");
            Log.d("reply intent", username);
            tvReply.setText("@ " + username);

        }
        else {
            tvReply.setVisibility(View.GONE);
        }






        //set click listener on button
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvCharacterCount.setText(charSequence.length() + "/" + MAX_TWEET_LENGTH);

                if (charSequence.length() > MAX_TWEET_LENGTH){

                    btnTweet.setTextColor(Color.parseColor("FF0000"));

                }
                else{
                    btnTweet.setTextColor(color);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }) ;{}
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.
                        isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Sorry your tweet is too long", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();

                //make an API call to Twitter to publish the tweet
               // showProgressBar();
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet says: " + tweet);

                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            //set result code and bundle data for response
                            setResult(RESULT_OK, intent);
                            //closes the activity, pass data to parent
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet", throwable);
                    }
                });
                //hideProgressBar();

            }
        });



    }
}