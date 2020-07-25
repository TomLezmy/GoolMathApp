package com.tomlezmy.goolmathapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.SubCategoriesFragment;
import com.tomlezmy.goolmathapp.fragments.SubjectsFragment;

public class PracticeSelectActivity extends AppCompatActivity implements SubjectsFragment.OnSelectedSubCategoryListener, SubCategoriesFragment.OnSubCategoryListener {
    public SubjectsFragment subjectsFragment;

    @Override
    public void onSubCategory(int subCategoryId) {
        this.subCategorySelected = subCategorySelected;
        Intent intent = new Intent(this, GamePage.class);
        intent.putExtra("category", this.categorySelected);
        intent.putExtra("level", this.subCategorySelected + 1);
        startActivity(intent);

    }

    int categorySelected;
    int subCategorySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_select);
        displaySubjects();
    }

    public void displaySubjects() {
        int cardColorId = ContextCompat.getColor(this, R.color.blue_card);
        int titleColorId = ContextCompat.getColor(this, R.color.blue_title);
        subjectsFragment = new SubjectsFragment(cardColorId,titleColorId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.subjects_display_layout, subjectsFragment, "Subjects_TAG").commit();
    }

    @Override
    public void onSelectedSubCategory(int categoryId) {
        //Toast.makeText(this, "categoryId=" + " "  + categoryId + " " + "subCategory=" + subCategoryId, Toast.LENGTH_LONG).show();
        this.categorySelected = categoryId;
//        Intent intent = new Intent(this, GamePage.class);
//        intent.putExtra("category", this.categorySelected);
//        intent.putExtra("level", this.subCategorySelected + 1);
//        startActivity(intent);
    }
}
