package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.hanks.htextview.base.HTextView;
import com.tomlezmy.goolmathapp.FileManager;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.adapters.ProgressPagerAdapter;
import com.tomlezmy.goolmathapp.adapters.ProgressResultsAdapter;
import com.tomlezmy.goolmathapp.adapters.SubCategoryAdapter;
import com.tomlezmy.goolmathapp.game.ECategory;
import com.tomlezmy.goolmathapp.game.UserData;
import com.tomlezmy.goolmathapp.model.ProgressResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProgressActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    HTextView titleTv;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        titleTv = (HTextView) findViewById(R.id.tv_hanks);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        titleTv.animateText(getResources().getString(R.string.progress_main_title));
        this.language = Locale.getDefault().getDisplayLanguage();
        if ( this.language.equalsIgnoreCase("English")) {
            titleTv.setTextSize(30f);
        }
        else {
            titleTv.setTextSize(50f);
        }


        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new ProgressPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

    }
}