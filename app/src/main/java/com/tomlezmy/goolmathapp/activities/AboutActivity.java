package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.tomlezmy.goolmathapp.R;

import java.util.Locale;

public class AboutActivity extends AppCompatActivity {

    TextView tvAbout;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        this.tvAbout = findViewById(R.id.about_text);
        this.language = Locale.getDefault().getDisplayLanguage();
        if ( this.language.equalsIgnoreCase("English")) {
            tvAbout.setTextSize(20f);
        }

    }
}