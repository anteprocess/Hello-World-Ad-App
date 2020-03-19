package com.helloworldtestingads;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InterstitialListener;

public class MainActivity extends AppCompatActivity implements InterstitialListener {
    public static final String EXTRA_MESSAGE = "com.helloworldtestingads.MESSAGE";
    public static final String TAG = "Test";
    private final String APP_KEY = "b8ae1a45";
    private final String FALLBACK_USER_ID = "12451000";
    private Button sendButton;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Prepare IronSource sdk and setting up the Interstitial ads
        startIronSourceInitTask();
        sendButton = findViewById(R.id.button);
        editText = findViewById(R.id.editText);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "loadInterstitial");
                IronSource.loadInterstitial();
                sendMessage(editText.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

    /**
     * Called when the user taps the Send button
     */
    public void sendMessage(String msg) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, msg);
        startActivity(intent);
    }

    private void startIronSourceInitTask() {

        // getting advertiser id should be done on a background thread
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return IronSource.getAdvertiserId(MainActivity.this);
            }

            @Override
            protected void onPostExecute(String advertisingId) {
                if (TextUtils.isEmpty(advertisingId)) {
                    advertisingId = FALLBACK_USER_ID;
                }
                // we're using an advertisingId as the 'userId'
                initIronSource(APP_KEY, advertisingId);
            }
        };
        task.execute();
    }

    /**
     * @param appKey
     * @param userId
     */
    private void initIronSource(String appKey, String userId) {
        IronSource.setInterstitialListener(this);
        // set the IronSource user id
        IronSource.setUserId(userId);
        // init the IronSource SDK
        IronSource.init(this, appKey);
    }

    /**
     * Invoked when Interstitial Ad is ready to be shown after load function was called.
     */
    @Override
    public void onInterstitialAdReady() {
        Log.d(TAG, "onInterstitialAdReady");
        IronSource.showInterstitial();
    }

    /**
     * invoked when there is no Interstitial Ad available after calling load function.
     */
    @Override
    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
        Log.d(TAG, "onInterstitialAdLoadFailed");
    }

    /**
     * Invoked when the Interstitial Ad Unit is opened
     */
    @Override
    public void onInterstitialAdOpened() {
        Log.d(TAG, "onInterstitialAdOpened");
    }

    /*
     * Invoked when the ad is closed and the user is about to return to the application.
     */
    @Override
    public void onInterstitialAdClosed() {
        Log.d(TAG, "onInterstitialAdClosed");
    }

    /*
     * Invoked when the ad was opened and shown successfully.
     */
    @Override
    public void onInterstitialAdShowSucceeded() {
        Log.d(TAG, "onInterstitialAdShowSucceeded");
    }

    /**
     * Invoked when Interstitial ad failed to show.
     * // @param error - An object which represents the reason of showInterstitial failure.
     */
    @Override
    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
        Log.d(TAG, "onInterstitialAdShowFailed");
    }

    /*
     * Invoked when the end user clicked on the interstitial ad.
     */
    @Override
    public void onInterstitialAdClicked() {
        Log.d(TAG, "onInterstitialAdClicked");
    }
}
