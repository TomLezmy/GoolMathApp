package com.tomlezmy.goolmathapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.interfaces.IButtonFragmentAnswerListener;

import java.util.ArrayList;
import java.util.List;

public class ButtonsFragment extends Fragment implements View.OnClickListener{
    ArrayList<String> options;
    IButtonFragmentAnswerListener callBack;
    Button[] buttons;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callBack = (IButtonFragmentAnswerListener)context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement MyDialogListener interface");
        }
    }

    public static ButtonsFragment newInstance(List<String> options, boolean isTutorial) {
        ButtonsFragment buttonsFragment = new ButtonsFragment();
        Bundle bundle = new Bundle();

        bundle.putStringArrayList("options", (ArrayList<String>) options);
        bundle.putBoolean("isTutorial", isTutorial);

        buttonsFragment.setArguments(bundle);
        return buttonsFragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buttons, container, false);
        LinearLayout buttonLayout = rootView.findViewById(R.id.buttons_layout);
        options = getArguments().getStringArrayList("options");
        buttons = new Button[options.size()];
        for (int i = 0; i < options.size(); i++) {
            buttons[i] = new Button(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(15, 20, 15, 30);
            lp.weight = 1;
            buttons[i].setLayoutParams(lp);
            buttons[i].setText(options.get(i));
            buttons[i].setOnClickListener(this);
            buttons[i].setBackgroundResource(R.drawable.button_4_design);
            buttons[i].setTextAppearance(R.style.FontNumbers);

            buttons[i].setTextSize(18f);
            buttons[i].setTextColor(Color.WHITE);
            buttons[i].setOnTouchListener(new ButtonTouchAnimation());
            buttonLayout.addView(buttons[i]);
        }
        if (getArguments().getBoolean("isTutorial")) {
            disableButtons();
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        String buttonText = ((Button) v).getText().toString();
        if (!buttonText.contains("/")) {
            callBack.onReturn(Float.parseFloat(buttonText));
        } else {
            String split[] = buttonText.split("/");
            callBack.onReturn(Float.parseFloat(split[0]) / Float.parseFloat(split[1]));
        }
    }

    public void enableButtons() {
        for (Button button : buttons) {
            button.setEnabled(true);
        }
    }

    public void disableButtons() {
        for (Button button : buttons) {
            button.setEnabled(false);
        }
    }
}
