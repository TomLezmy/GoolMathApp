package com.tomlezmy.goolmathapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.SubjectsFragment;

public class PracticeSelectActivity extends AppCompatActivity implements SubjectsFragment.OnSelectedSubCategoryListener {
    SubjectsFragment subjectsFragment;
    int categorySelected;
    int subCategorySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_select);
        displaySubjects();
    }

    public void displaySubjects() {
        int colorId = ContextCompat.getColor(this, R.color.blue_card);
        subjectsFragment = new SubjectsFragment(colorId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.subjects_display_layout, subjectsFragment, "Subjects_TAG").commit();
    }


    @Override
    public void onSelectedSubCategory(int categoryId, int subCategoryId) {
        //Toast.makeText(this, "categoryId=" + " "  + categoryId + " " + "subCategory=" + subCategoryId, Toast.LENGTH_LONG).show();
        this.categorySelected = categoryId;
        this.subCategorySelected = subCategoryId;
        Intent intent = new Intent(this, GamePage.class);
        intent.putExtra("category", categorySelected);
        intent.putExtra("level", subCategorySelected + 1);
        startActivity(intent);
    }
}
