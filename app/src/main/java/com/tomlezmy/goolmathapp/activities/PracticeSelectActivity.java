package com.tomlezmy.goolmathapp.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.hanks.htextview.base.HTextView;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.SubCategoriesFragment;
import com.tomlezmy.goolmathapp.fragments.SubjectsFragment;

import java.util.Locale;

public class PracticeSelectActivity extends AppCompatActivity implements SubjectsFragment.OnSelectedSubCategoryListener, SubCategoriesFragment.OnSubCategoryListener {
    public SubjectsFragment subjectsFragment;
    int categorySelected;
    int subCategorySelected;
    HTextView tvQuestion_hanks;
    String secondTitle;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_select);

        ImageView settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PracticeSelectActivity.this, SettingsActivity.class));
            }
        });

        this.tvQuestion_hanks = (HTextView) findViewById(R.id.tv_hanks);

        this.language = Locale.getDefault().getDisplayLanguage();
        if ( this.language.equalsIgnoreCase("English")) {
            tvQuestion_hanks.setTextSize(33f);
        }
        else {
            tvQuestion_hanks.setTextSize(65f);
            tvQuestion_hanks.setPadding(10,10,10,10);
        }

        secondTitle = getResources().getString(R.string.title_practice_sub_categores_option2);
        tvQuestion_hanks.animateText(secondTitle);


        displaySubjects();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int cardColorId = ContextCompat.getColor(this, R.color.blue_card_option3);
        int titleColorId = ContextCompat.getColor(this, R.color.blue_title);
        subjectsFragment = new SubjectsFragment(cardColorId,titleColorId,false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.subjects_display_layout, subjectsFragment, "Subjects_TAG").commit();
    }

    public void displaySubjects() {
        int cardColorId = ContextCompat.getColor(this, R.color.blue_card_option3);
        int titleColorId = ContextCompat.getColor(this, R.color.blue_title);
        subjectsFragment = new SubjectsFragment(cardColorId,titleColorId,false);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.subjects_display_layout, subjectsFragment, "Subjects_TAG").commit();
    }

    @Override
    public void onSelectedSubCategory(int categoryId) {
        this.categorySelected = categoryId;
        if (categoryId == -1) {
            Intent intent = new Intent(this, GamePage.class);
            intent.putExtra("tutorial",true);
            startActivity(intent);
        }
    }

    @Override
    public void onSubCategory(int subCategoryId) {
        this.subCategorySelected = subCategoryId;
        Intent intent = new Intent(this, GamePage.class);
        intent.putExtra("category", this.categorySelected);
        intent.putExtra("level", this.subCategorySelected + 1);
        startActivity(intent);
    }
}
