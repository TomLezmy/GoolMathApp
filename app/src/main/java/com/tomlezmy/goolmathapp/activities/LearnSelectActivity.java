package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;

import com.hanks.htextview.base.HTextView;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.SubCategoriesFragment;
import com.tomlezmy.goolmathapp.fragments.SubjectsFragment;

import java.util.Locale;

public class LearnSelectActivity extends AppCompatActivity implements SubjectsFragment.OnSelectedSubCategoryListener, SubCategoriesFragment.OnSubCategoryListener {

    SubjectsFragment subjectsFragment;
    int categorySelected;
    int subCategorySelected;
    HTextView tvQuestion_hanks;
    Typeface typeface;
    String secondTitle;
    String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_select);

        this.tvQuestion_hanks = (HTextView) findViewById(R.id.tv_hanks);


        this.language = Locale.getDefault().getDisplayLanguage();
        if ( this.language.equalsIgnoreCase("English")) {
//            this.typeface = ResourcesCompat.getFont(getBaseContext(), R.font.kalam_regular);
            this.typeface = ResourcesCompat.getFont(getBaseContext(), R.font.shortstack_regular);
            tvQuestion_hanks.setTextSize(35f);
        }
        else {
            this.typeface = ResourcesCompat.getFont(getBaseContext(), R.font.motek);
            tvQuestion_hanks.setTextSize(70f);
            tvQuestion_hanks.setPadding(20,20,20,20);
        }

        secondTitle = getResources().getString(R.string.title_learn_sub_categories_option2);
        tvQuestion_hanks.setTypeface(this.typeface, Typeface.BOLD);
        tvQuestion_hanks.animateText(secondTitle);







        displaySubjectsForLearn();

    }


    @Override
    public void onSubCategory(int subCategoryId) {
        this.subCategorySelected = subCategoryId;
        Intent intent = new Intent(this, LearnActivity.class);
        intent.putExtra("category", this.categorySelected);
        intent.putExtra("sub_category", this.subCategorySelected);
        startActivity(intent);
    }


    // Create SubjectFragment in LearnSelectActivity
    public void displaySubjectsForLearn() {
        int cardColorId = ContextCompat.getColor(this, R.color.green_card_option3);
        int titleColorId = ContextCompat.getColor(this, R.color.green_title);
        subjectsFragment = new SubjectsFragment(cardColorId, titleColorId, true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.subjects_display_layout, subjectsFragment, "Subjects_TAG").commit();
    }

    @Override
    public void onSelectedSubCategory(int categoryId) {
//        Toast.makeText(this, "categoryId=" + " "  + categoryId + " " + "subCategory=" + subCategoryId, Toast.LENGTH_LONG).show();
        this.categorySelected = categoryId;
    }
}
