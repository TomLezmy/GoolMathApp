package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.tomlezmy.goolmathapp.R;

import java.util.Locale;

/**
 * This activity explains the app's purpose and gives asset attributes
 */
public class AboutActivity extends AppCompatActivity {
    TextView tvAbout;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvAbout = findViewById(R.id.about_text);

        // Sets text size by locale
        language = Locale.getDefault().getDisplayLanguage();
        if (language.equalsIgnoreCase("English")) {
            tvAbout.setTextSize(20f);
        }
    }
}