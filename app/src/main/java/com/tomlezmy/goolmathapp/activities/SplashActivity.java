package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SpinnerAdapter;

import com.tomlezmy.goolmathapp.FileManager;
import com.tomlezmy.goolmathapp.R;

public class SplashActivity extends AppCompatActivity {

    final int SPLASH_DISPLAY_LENGTH = 3000;

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