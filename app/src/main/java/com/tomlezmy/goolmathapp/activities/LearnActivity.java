package com.tomlezmy.goolmathapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.EVideoIds;
import com.tomlezmy.goolmathapp.R;

public class LearnActivity extends AppCompatActivity {
    YouTubePlayerView youTubePlayerView;
    String videoId;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_learn);
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
                        if (EVideoIds.values().length > currentCategory) {
                            // Search for subject with videos
                            do {
                                currentCategory++;
                            } while (EVideoIds.values()[currentCategory].getAllVideoIds().length == 0);
                            nextSubCategory = 0;
                        }
                        else {
                            Toast.makeText(LearnActivity.this, "No more videos", Toast.LENGTH_SHORT).show();
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
}


