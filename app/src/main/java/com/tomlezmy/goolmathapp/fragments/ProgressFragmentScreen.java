package com.tomlezmy.goolmathapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hanks.htextview.base.HTextView;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.adapters.ProgressPagerAdapter;

import java.util.Locale;

public class ProgressFragmentScreen extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    HTextView titleTv;
    String language;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_progress_screen, container, false);

        titleTv = (HTextView) rootView.findViewById(R.id.tv_hanks);
        viewPager = rootView.findViewById(R.id.view_pager);
        tabLayout = rootView.findViewById(R.id.tab_layout);

        titleTv.animateText(getResources().getString(R.string.progress_main_title));
        this.language = Locale.getDefault().getDisplayLanguage();
        if ( this.language.equalsIgnoreCase("English")) {
            titleTv.setTextSize(30f);
        }
        else {
            titleTv.setTextSize(50f);
        }

        ProgressPagerAdapter progressPagerAdapter = new ProgressPagerAdapter(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        progressPagerAdapter.setContext(getContext());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(progressPagerAdapter);

        return rootView;
    }
}
