package com.tomlezmy.goolmathapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.tomlezmy.goolmathapp.R;

public class LearnActivity extends AppCompatActivity {
    YouTubePlayerView youTubePlayerView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_learn);
            youTubePlayerView = findViewById(R.id.video_view);
            youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
                @Override
                public void onYouTubePlayerEnterFullScreen() {

                }

                @Override
                public void onYouTubePlayerExitFullScreen() {

                }
            });
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String videoId = "mNfOSN6WmOc";
                    youTubePlayer.cueVideo(videoId, 0);
//                    youTubePlayer.loadVideo(videoId, 0);
                }
            });
        }

    @Override
    protected void onPause() {
        super.onPause();
        //youTubePlayerView.release();
    }
}


