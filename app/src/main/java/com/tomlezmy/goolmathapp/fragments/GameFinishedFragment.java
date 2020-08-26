package com.tomlezmy.goolmathapp.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
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

/**
 * This fragment is displayed at the end of a game in {@link com.tomlezmy.goolmathapp.activities.GamePage} and shows the game result
 */
public class GameFinishedFragment extends Fragment {

    Button backButton;
    boolean levelComplete;
    int improvementCounter, deteriorationCounter;
    CategoryProgressData categoryProgressData;
    IResultFragmentListener callback;

    /**
     * Class constructor
     * @param levelComplete True if the level was finished successfully
     * @param improvementCounter Number of improvements in weights
     * @param deteriorationCounter Number of deterioration's in weights
     * @param categoryProgressData The current level progress
     */
    public GameFinishedFragment(boolean levelComplete, int improvementCounter, int deteriorationCounter, CategoryProgressData categoryProgressData) {
        this.levelComplete = levelComplete;
        this.improvementCounter = improvementCounter;
        this.deteriorationCounter = deteriorationCounter;
        this.categoryProgressData = categoryProgressData;
    }

    /**
     * When fragment attaches to the activity, the {@link IResultFragmentListener} is attached to it
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (IResultFragmentListener) context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement IResultFragmentListener interface");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_finished, container, false);
        TextView gameResultText = rootView.findViewById(R.id.game_result_text);
        backButton = rootView.findViewById(R.id.btn_back);
        Button startAgainButton = rootView.findViewById(R.id.btn_start_again);
        Button nextLevelButton = rootView.findViewById(R.id.btn_next_level);
        backButton.setOnTouchListener(new ButtonTouchAnimation());
        nextLevelButton.setOnTouchListener(new ButtonTouchAnimation());
        startAgainButton.setOnTouchListener(new ButtonTouchAnimation());

        String progressMessage = "";
        // Check if first game
        if (categoryProgressData.getTimesPlayed() != 1) {
            if (improvementCounter > deteriorationCounter) {
                progressMessage = getString(R.string.improvementMessage) + "\n";
            }
            else if (improvementCounter < deteriorationCounter) {
                progressMessage = getString(R.string.deteriorationMessage) + "\n";
            }
        }

        if (levelComplete) {
            gameResultText.setText(progressMessage + getString(R.string.successfully_finished_the_level_txt));
        }
        else {
            gameResultText.setText(progressMessage + getString(R.string.level_finished_txt) + "\n" + getString(R.string.you_can_do_better_txt));
        }

        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onPressContinue(false);
            }
        });

        nextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onPressContinue(true);
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