package com.tomlezmy.goolmathapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
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

public class ProgressActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ViewPager viewPager;
    ProgressResultsAdapter progressPagerAdapter;
    ArrayList<ProgressResult> progressResultList;
    FileManager fileManager;
    ECategory category;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ProgressPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

//        recyclerView = findViewById(R.id.recycler);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Build the List
//        progressResultList = new ArrayList<>();
//        progressResultList.add(new ProgressResult("test levl 1", 3,15));
//        progressResultList.add(new ProgressResult("test levl 2", 2,10));
//
//
//        progressPagerAdapter = new ProgressResultsAdapter(progressResultList, this);
//        recyclerView.setAdapter(progressPagerAdapter);
    }


//    void buildProgressResultList() {
//
//        this.fileManager = FileManager.getInstance(this);
//        this.category = ECategory.values()[getArguments().getInt("category")];
//        this.userData = fileManager.getUserData();
//
//        userData.getLevelsProgressData().get(category).get(i).getTimesPlayed()
//
//        getResources().getStringArray(stringArrays[getArguments().getInt("category")])[i]
//
//        userData.getLevelsProgressData().get(category).get(i).getMaxScore()
//
//
//
//        categoryProgressData = fileManager.getUserData().getLevelsProgressData().get(ECategory.values()[category]).get(level - 1);
//        categoryProgressData.setTimesPlayed(categoryProgressData.getTimesPlayed() + 1);
//
//         for Adding category
//
//    }
}