package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.PreferenceFragment;

/**
 * This activity sets the user settings
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SettingsFontLocalized);
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().add(android.R.id.content, new PreferenceFragment()).commit();
    }
}