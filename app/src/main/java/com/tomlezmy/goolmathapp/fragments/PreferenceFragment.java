package com.tomlezmy.goolmathapp.fragments;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;

import androidx.annotation.Nullable;

import com.tomlezmy.goolmathapp.FileManager;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.game.UserData;

public class PreferenceFragment extends android.preference.PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        final FileManager fileManager = FileManager.getInstance(getContext());
        final UserData userData = fileManager.getUserData();
        EditTextPreference editTextPreferenceName = (EditTextPreference) findPreference("preference_change_name");
        editTextPreferenceName.setText(userData.getFirstName());
        EditTextPreference editTextPreferenceLastName = (EditTextPreference) findPreference("preference_change_last_name");
        editTextPreferenceLastName.setText(userData.getLastName());
        editTextPreferenceName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                userData.setFirstName((String)newValue);
                fileManager.updateUserDataFile();
                return true;
            }
        });

        editTextPreferenceLastName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                userData.setLastName((String)newValue);
                fileManager.updateUserDataFile();
                return true;
            }
        });
    }
}
