package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.SubjectsFragment;

public class LearnSelectActivity extends AppCompatActivity implements SubjectsFragment.OnSelectedSubCategoryListener {

    SubjectsFragment subjectsFragment;
    int categorySelected;
    int subCategorySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_select);
        displaySubjectsForLearn();
    }

    // Create SubjectFragment in LearnSelectActivity
    public void displaySubjectsForLearn() {
        int cardColorId = ContextCompat.getColor(this, R.color.green_card);
        int titleColorId = ContextCompat.getColor(this, R.color.green_title);
        subjectsFragment = new SubjectsFragment(cardColorId, titleColorId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.subjects_display_layout, subjectsFragment, "Subjects_TAG").commit();
    }

    @Override
    public void onSelectedSubCategory(int categoryId, int subCategoryId) {
//        Toast.makeText(this, "categoryId=" + " "  + categoryId + " " + "subCategory=" + subCategoryId, Toast.LENGTH_LONG).show();
        this.categorySelected = categoryId;
        this.subCategorySelected = subCategoryId;

//        // Test
        Intent intent = new Intent(this, LearnActivity.class);
        startActivity(intent);
    }
}
