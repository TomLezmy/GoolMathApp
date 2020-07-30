package com.tomlezmy.goolmathapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tomlezmy.goolmathapp.fragments.ProgressFragment;
import com.tomlezmy.goolmathapp.game.ECategory;

public class ProgressPagerAdapter extends FragmentStatePagerAdapter {
    final int size = ECategory.values().length;

    public ProgressPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ProgressFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return size;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = ECategory.values()[position].toString().toLowerCase();
        title = title.substring(0, 1).toUpperCase() + title.substring(1);
        return title;
    }
}
