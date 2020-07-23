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
import com.tomlezmy.goolmathapp.interfaces.IResultFragmentListener;

public class GameFinishedFragment extends Fragment {

    Button backButton;
    boolean levelComplete;
    boolean moveToNextLevel = false;
    IResultFragmentListener callback;

    public GameFinishedFragment(boolean levelComplete) {
        this.levelComplete = levelComplete;
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
        if (levelComplete) {
            continueGameButton.setText("Next Level");
            gameResultText.setText("Successfully finished the level,\ngood job!");
            moveToNextLevel = true;
        }
        else {
            continueGameButton.setText("Start Again");
            gameResultText.setText("Level finished, nice work but you can do better!\n");
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