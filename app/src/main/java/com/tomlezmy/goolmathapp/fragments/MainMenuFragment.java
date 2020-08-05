package com.tomlezmy.goolmathapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.activities.LearnSelectActivity;
import com.tomlezmy.goolmathapp.activities.PracticeSelectActivity;
import com.tomlezmy.goolmathapp.activities.ProgressActivity;
import com.tomlezmy.goolmathapp.interfaces.IButtonFragmentAnswerListener;
import com.tomlezmy.goolmathapp.interfaces.IFragmentChangeListener;
import com.tomlezmy.goolmathapp.interfaces.SendMessage;

public class MainMenuFragment extends Fragment {
    IFragmentChangeListener callBack;

    @Override
    public void onAttach(Context context) {
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
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        Button btnPractice = rootView.findViewById(R.id.btn_practice);
        btnPractice.setOnTouchListener(new ButtonTouchAnimation());
        btnPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onChange("PracticeSelect");
            }
        });
        Button btnLearn = rootView.findViewById(R.id.btn_learn);
        btnLearn.setOnTouchListener(new ButtonTouchAnimation());
        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onChange("LearnSelect");
            }
        });
        Button btnProgress = rootView.findViewById(R.id.btn_progress);
        btnProgress.setOnTouchListener(new ButtonTouchAnimation());
        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onChange("Progress");
//                Intent intent = new Intent(getContext(), ProgressActivity.class);
//                startActivity(intent);
            }
        });
        return rootView;
    }
}
