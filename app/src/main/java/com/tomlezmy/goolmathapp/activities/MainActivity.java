package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.ProgressFragment;

public class MainActivity extends AppCompatActivity {

    Button btnPractice;
    Button btnLearn;
    Button btnProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // references
        ImageView settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        btnPractice = findViewById(R.id.btn_practice);
        btnPractice.setOnTouchListener(new ButtonTouchAnimation());
        btnPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PracticeSelectActivity.class);
                startActivity(intent);
            }
        });
        btnLearn = findViewById(R.id.btn_learn);
        btnLearn.setOnTouchListener(new ButtonTouchAnimation());
        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LearnSelectActivity.class);
                startActivity(intent);
            }
        });
        btnProgress = findViewById(R.id.btn_progress);
        btnProgress.setOnTouchListener(new ButtonTouchAnimation());
        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
                startActivity(intent);
            }
        });

    }



}
