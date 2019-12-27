package com.codepath.apps.restclienttemplate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
// ...

public class ComposeFragment extends DialogFragment {

    private EditText etTweet;
    private Button btnSend;
    private static ComposeFragmentListener composeFragmentListener;
    private static String reTweet;
    private  TextView tvChars;

    public ComposeFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }



    public static ComposeFragment newInstance(ComposeFragmentListener comp) {
        ComposeFragment frag = new ComposeFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        composeFragmentListener = comp;
        return frag;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvChars = view.findViewById(R.id.tvChars);



        // Get field from view
        etTweet = view.findViewById(R.id.etTweet);
        // Show soft keyboard automatically and request focus to field
        etTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        reTweet = getArguments().getString("tweet");

        if(reTweet != null)
        {
            etTweet.setText("@"+reTweet+" ");
        }

        int charCount = etTweet.getText().toString().length();
        int charsLeft = 280 - charCount;

        // change color for appropriate text
        if (charsLeft < 0 )
        {
            tvChars.setTextColor(Color.RED);
        }
        else
        {
            tvChars.setTextColor(Color.GRAY);
        }

        // set text to how many chars are left
        tvChars.setText(charsLeft + " characters Left");

        // text watcher to update the chars left window
        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // get number of chars in text box
                int charCount = etTweet.getText().toString().length();
                int charsLeft = 280 - charCount;

                // change color for appropriate text
                if (charsLeft < 0 )
                {
                    tvChars.setTextColor(Color.RED);
                }
                else
                {
                    tvChars.setTextColor(Color.GRAY);
                }

                // set text to how many chars are left
                tvChars.setText(charsLeft + " characters Left");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSend = view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get text to tweet
                String tweet = etTweet.getText().toString();

                RestClient client  = RestApplication.getRestClient(v.getContext());
                client.sendTweet(tweet, new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                        try {


                            // start new intent and send back the response
                            Tweet tweet = Tweet.fromJSON(response);

                            composeFragmentListener.onFinishTweet(tweet);
                            dismiss();

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

    public interface ComposeFragmentListener {
        void onFinishTweet(Tweet tweet);
    }



}
