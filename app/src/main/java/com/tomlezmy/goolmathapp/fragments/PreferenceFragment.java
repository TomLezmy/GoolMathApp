package com.tomlezmy.goolmathapp.fragments;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;

import androidx.annotation.Nullable;

import com.tomlezmy.goolmathapp.model.FileManager;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.model.UserData;

/**
 * This fragment displays the app settings
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        final FileManager fileManager = FileManager.getInstance(getContext());
        final UserData userData = fileManager.getUserData();
        EditTextPreference editTextPreferenceName = (EditTextPreference) findPreference("preference_change_name");
        editTextPreferenceName.setText(userData.getFirstName());
        editTextPreferenceName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                userData.setFirstName((String)newValue);
                fileManager.updateUserDataFile();
                return true;
            }
        });
    }
}
