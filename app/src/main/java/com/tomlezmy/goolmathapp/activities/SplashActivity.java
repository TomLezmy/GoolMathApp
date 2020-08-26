package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tomlezmy.goolmathapp.model.FileManager;
import com.tomlezmy.goolmathapp.R;

/**
 * This activity displays a splash of the app logo while checking if user is new to the app
 */
public class SplashActivity extends AppCompatActivity {

    final int SPLASH_DISPLAY_LENGTH = 1000;

    /**
     * The splash screen is displayed for {@link #SPLASH_DISPLAY_LENGTH} milliseconds while checking for user data files and redirects accordingly
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent;
                if (FileManager.userFileExists(SplashActivity.this)) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                else {
                    intent = new Intent(SplashActivity.this, RegisterActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}