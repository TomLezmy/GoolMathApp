package com.tomlezmy.goolmathapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.EVideoIds;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.game.ECategory;
import com.tomlezmy.goolmathapp.interfaces.IFragmentChangeListener;

public class LearnFragment extends Fragment {

    YouTubePlayerView youTubePlayerView;
    String videoId;
    TextView learnTitle;
    int category;
    int subCategory;
    IFragmentChangeListener callBack;

    public LearnFragment(int category, int subCategory) {
        this.category = category;
        this.subCategory = subCategory;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callBack = (IFragmentChangeListener)context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement IFragmentChangeListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_learn, container, false);

        learnTitle = rootView.findViewById(R.id.tv_learn);

        setVideoTitle();

        youTubePlayerView = rootView.findViewById(R.id.video_view);
        videoId = EVideoIds.values()[category].getVideoId(subCategory);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0);
            }
        });

        Button backBtn = rootView.findViewById(R.id.back_btn_learn);
        Button nextBtn = rootView.findViewById(R.id.next_btn_learn);
        backBtn.setOnTouchListener(new ButtonTouchAnimation());
        nextBtn.setOnTouchListener(new ButtonTouchAnimation());
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onChange("LearnBack");
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCategory = category;
                int nextSubCategory = subCategory + 1;
                if (EVideoIds.values()[currentCategory].getAllVideoIds().length == nextSubCategory) {
                    if (EVideoIds.values().length > (currentCategory + 1)) {
                        // Search for subject with videos
                        do {
                            currentCategory++;
                        } while (EVideoIds.values()[currentCategory].getAllVideoIds().length == 0);
                        nextSubCategory = 0;
                    }
                    else {
                        Toast.makeText(getContext(), getString(R.string.no_more_videos), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // Reload Activity
                callBack.onChange("ReloadLearn",currentCategory, nextSubCategory);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }

    private void  setVideoTitle() {
        ECategory eCategory = ECategory.values()[category];
        int videoNamesArrayId = 0;
        switch (eCategory) {
            case ADDITION:
                videoNamesArrayId = R.array.learn_additionSubCategories;
                break;
            case MULTIPLICATION:
                videoNamesArrayId = R.array.learn_multiplicationSubCategories;
                break;
            case DIVISION:
                videoNamesArrayId = R.array.learn_divisionSubCategories;
                break;
            case FRACTIONS:
                videoNamesArrayId = R.array.learn_fractionsSubCategories;
                break;
            case PERCENTS:
                videoNamesArrayId = R.array.learn_percentsSubCategories;
                break;
            case DECIMALS:
                videoNamesArrayId = R.array.learn_decimalsSubCategories;
                break;
        }
        learnTitle.setText(getResources().getStringArray(videoNamesArrayId)[subCategory]);
    }
}
