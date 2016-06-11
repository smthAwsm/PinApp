package com.techtask.xps.pinsapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.techtask.xps.pinsapp.R;

/**
 * Created by XPS on 6/11/2016.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.splash_layout);

        if (isLoggedIn())
            startActivity(new Intent(this,MainActivity.class));
        else startActivity(new Intent(this,LoginActivity.class));
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
