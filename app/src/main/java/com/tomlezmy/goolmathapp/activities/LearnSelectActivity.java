package com.tomlezmy.goolmathapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.SubjectsFragment;

class LearnSelectActivity extends AppCompatActivity {

    SubjectsFragment subjectsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_select);
        displaySubjectsForLearn();
    }

    public void displaySubjectsForLearn() {
        subjectsFragment = new SubjectsFragment(R.color.green_card);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.subjects_display_layout, subjectsFragment, "Subjects_TAG").commit();
    }

}
