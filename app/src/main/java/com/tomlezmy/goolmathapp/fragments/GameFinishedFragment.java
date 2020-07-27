package com.tomlezmy.goolmathapp.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.game.CategoryProgressData;
import com.tomlezmy.goolmathapp.interfaces.IResultFragmentListener;

public class GameFinishedFragment extends Fragment {

    Button backButton;
    boolean levelComplete;
    int improvementCounter, deteriorationCounter;
    boolean moveToNextLevel = false;
    CategoryProgressData categoryProgressData;
    IResultFragmentListener callback;

    public GameFinishedFragment(boolean levelComplete, int improvementCounter, int deteriorationCounter, CategoryProgressData categoryProgressData) {
        this.levelComplete = levelComplete;
        this.improvementCounter = improvementCounter;
        this.deteriorationCounter = deteriorationCounter;
        this.categoryProgressData = categoryProgressData;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (IResultFragmentListener) context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement IResultFragmentListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_finished, container, false);
        TextView gameResultText = rootView.findViewById(R.id.game_result_text);
        backButton = rootView.findViewById(R.id.btn_back);
        Button continueGameButton = rootView.findViewById(R.id.btn_continue_playing);
        backButton.setOnTouchListener(new ButtonTouchAnimation());
        continueGameButton.setOnTouchListener(new ButtonTouchAnimation());

        // Check if first game
        if (categoryProgressData.getTimesPlayed() != 1) {
            if (improvementCounter > deteriorationCounter) {
                // TODO Add a "your'e improving" message
            }
            else if (improvementCounter < deteriorationCounter) {
                // TODO Add a "maybe go to learn" message
            }
        }

        if (levelComplete) {
            continueGameButton.setText(R.string.next_level_txt);
            gameResultText.setText(R.string.successfully_finished_the_level_txt);
            moveToNextLevel = true;
        }
        else {
            continueGameButton.setText(R.string.start_again_txt);
            gameResultText.setText(getString(R.string.level_finished_txt) + "\n" + getString(R.string.you_can_do_better_txt));
        }

        continueGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onPressContinue(moveToNextLevel);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onPressBack();
            }
        });

        return rootView;
    }
}