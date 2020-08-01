package com.tomlezmy.goolmathapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.EVideoIds;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.game.ECategory;

public class LearnActivity extends AppCompatActivity {
    YouTubePlayerView youTubePlayerView;
    String videoId;
    TextView learnTitle;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_learn);

            learnTitle = findViewById(R.id.tv_learn);

            setVideoTitle();

            ImageView settings = findViewById(R.id.settings);
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LearnActivity.this, SettingsActivity.class));
                }
            });

            youTubePlayerView = findViewById(R.id.video_view);
            videoId = EVideoIds.values()[getIntent().getIntExtra("category",0)].getVideoId(getIntent().getIntExtra("sub_category",0));

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0);
//                    youTubePlayer.loadVideo(videoId, 0);
                }
            });

            Button backBtn = findViewById(R.id.back_btn_learn);
            Button nextBtn = findViewById(R.id.next_btn_learn);
            backBtn.setOnTouchListener(new ButtonTouchAnimation());
            nextBtn.setOnTouchListener(new ButtonTouchAnimation());
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentCategory = getIntent().getIntExtra("category",0);
                    int nextSubCategory = getIntent().getIntExtra("sub_category",0) + 1;
                    if (EVideoIds.values()[currentCategory].getAllVideoIds().length == nextSubCategory) {
                        if (EVideoIds.values().length > (currentCategory + 1)) {
                            // Search for subject with videos
                            do {
                                currentCategory++;
                            } while (EVideoIds.values()[currentCategory].getAllVideoIds().length == 0);
                            nextSubCategory = 0;
                        }
                        else {
                            Toast.makeText(LearnActivity.this, getString(R.string.no_more_videos), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    // Reload Activity
                    getIntent().putExtra("category", currentCategory);
                    getIntent().putExtra("sub_category", nextSubCategory);
                    finish();
                    startActivity(getIntent());
                }
            });
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }

    private void  setVideoTitle() {
            int category = getIntent().getIntExtra("category",0);
            int subCategory = getIntent().getIntExtra("sub_category",0);
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


