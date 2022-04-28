package com.acube.jims.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.presentation.Login.View.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boolean urlupdated = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "urlupdated");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (urlupdated){
                    Intent i= new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i); //start new activity
                    finish();
                }else{
                    Intent i= new Intent(SplashActivity.this, SettingsActivity.class);
                    startActivity(i); //start new activity
                    finish();
                }





            }
        }, 1000); //time in milliseconds
    }
}