package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.PreferenceFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().add(android.R.id.content, new PreferenceFragment()).commit();
    }
}