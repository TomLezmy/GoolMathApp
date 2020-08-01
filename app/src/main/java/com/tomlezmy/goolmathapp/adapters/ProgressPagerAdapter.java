package com.tomlezmy.goolmathapp.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.fragments.ProgressFragment;
import com.tomlezmy.goolmathapp.game.ECategory;

public class ProgressPagerAdapter extends FragmentStatePagerAdapter {
    final int size = ECategory.values().length;
    Context context;

    public ProgressPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void setContext(Context context) {
        this.context = context;
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
        return context.getResources().getStringArray(R.array.category_names)[position];
    }
}
