package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);


        final Button button = findViewById(R.id.btnSend);
        final EditText etTweet = findViewById(R.id.etTweet);
        final TextView tvChars = findViewById(R.id.tvChars);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#35CDE5")));




        // check if there is an @
        String retweet = getIntent().getStringExtra("@");

        if(retweet != null)
        {
            etTweet.setText(retweet);
        }

        int charCount = etTweet.getText().toString().length();
        int charsLeft = 280 - charCount;

        tvChars.setText(charsLeft + " Characters Left");


        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int charCount = etTweet.getText().toString().length();
                int charsLeft = 280 - charCount;

                if (charsLeft < 0 )
                {
                    tvChars.setTextColor(Color.RED);
                }
                else
                {
                    tvChars.setTextColor(Color.GRAY);
                }

                tvChars.setText(charsLeft + " Characters Left");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String tweet = etTweet.getText().toString();

                RestClient client  = RestApplication.getRestClient(v.getContext());
                client.sendTweet(tweet, new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("ComposeTweet",response.toString());

                        try {

                            Tweet tweet = Tweet.fromJSON(response);

                            Log.d("OK",tweet.body);

                            Intent data = new Intent();

                            data.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));


                            setResult(RESULT_OK, data);

                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("ComposeTweet",responseString);
                        throwable.printStackTrace();
                    }


                });
            }
        });

    }



}
