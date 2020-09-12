package com.tomlezmy.goolmathapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
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
import com.tomlezmy.goolmathapp.model.GameRecord;
import com.tomlezmy.goolmathapp.model.RecordDatabase;

import java.util.List;
import java.util.Locale;

/**
 * This fragment displays progress data in all category's
 */
public class ProgressFragmentScreen extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;
    HTextView titleTv;
    String language;
    RecordDatabase recordDatabase;
    Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_progress_screen, container, false);

        titleTv = rootView.findViewById(R.id.tv_hanks);
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

        recordDatabase = RecordDatabase.getInstance(getContext());
        handler = new Handler();

        return rootView;
    }

    /**
     * This method gets the game records of the current category and level from the database and displays them to the user
     * @param categoryIndex The chosen category index
     * @param levelIndex The chosen level index
     */
    public void displayGameRecords(final int categoryIndex, final int levelIndex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<GameRecord> records = recordDatabase.gameRecordDao().getAllRecordsFromLevel(categoryIndex, levelIndex);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showAlert(records);
                    }
                });
            }
        }).start();
    }

    /**
     * This method creates a {@link GameRecordsFragmentDialog} to display the game records
     * @param records A list of game records to display
     */
    public void showAlert(List<GameRecord> records) {
        GameRecordsFragmentDialog gameRecordsFragmentDialog = new GameRecordsFragmentDialog(records);
        gameRecordsFragmentDialog.show(getActivity().getSupportFragmentManager(), "GAME_RECORDS_TAG");
    }
}
