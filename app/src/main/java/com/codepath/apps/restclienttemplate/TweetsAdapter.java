package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    TwitterClient client;
    Context context;
    List<Tweet> tweets;
    //TextView tvReply;
    //pass in context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;


    }



    //for each row inflate a layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }
    //bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get data at position
        Tweet tweet = tweets.get(position);
        //bind the tweet with the view holder
        holder.bind(tweet);

    }
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }
    //pass in context and list of tweets





    //define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivMedia;
        TextView tvTimeStamp;
        Button btnReply;
        ImageButton btnFavorite;
        ImageButton btnRetweet;

        //ImageButton btnReply2;
        //itemView represents one row, a tweet
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);

            btnReply = itemView.findViewById(R.id.btnReply);


            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnFavorite.setTag("empty heart");
            client = TwitterApp.getRestClient(context);

            btnRetweet = itemView.findViewById(R.id.btnRetweet);
            btnRetweet.setTag("no retweet");

            itemView.setOnClickListener(this);



            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ComposeActivity.class);
                    intent.putExtra("reply_username", tvScreenName.getText());
                    context.startActivity(intent);


                }
            });

            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = tweets.get(position);
                        if (btnFavorite.getTag().equals("empty heart") && tweet.getFavorited()==false){
                            client.addFavorite(tweet.id, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Headers headers, JSON json) {
                                    Log.i("success favorite", "like");
                                    btnFavorite.setImageResource(R.drawable.ic_vector_heart);
                                    btnFavorite.setTag("full heart");
                                    tweet.favorited = true;

                                }

                                @Override
                                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                    Log.e("failure adding", response, throwable);

                                }
                            });
                        }
                        else {
                            client.removeFavorite(tweet.id, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Headers headers, JSON json) {
                                    btnFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);
                                    btnFavorite.setTag("empty heart");
                                    tweet.favorited = false;


                                }

                                @Override
                                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                    Log.e("failure removing", response, throwable);


                                }
                            });

                        }
                    }

                }
            });

            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = tweets.get(position);

                    }


                }
            });



        }

        public void bind(Tweet tweet){
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            Log.i("TweetsAdapter","Time created at"+tweet.createdAt);
            tvTimeStamp.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.createdAt));
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);

            if (tweet.favorited){
                btnFavorite.setImageResource(R.drawable.ic_vector_heart);

            }
            if (!tweet.favorited){
                btnFavorite.setImageResource(R.drawable.ic_vector_heart_stroke);

            }

            if (!tweet.tweet_URL.equals("none")){
                ivMedia.setVisibility(View.VISIBLE);
                Log.d("adapter", "tweet load image"+tweet.tweet_URL);
                Glide.with(context).load(tweet.tweet_URL).into(ivMedia);


            }
            else {
                ivMedia.setVisibility(View.GONE);
            }




        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet tweet = tweets.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // show the activity
                context.startActivity(intent);
            }

        }
    }
}
