package com.tomlezmy.goolmathapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.interfaces.IButtonFragmentAnswerListener;
import com.tomlezmy.goolmathapp.interfaces.SendMessage;

import java.util.ArrayList;
import java.util.List;

public class ButtonsFragment extends Fragment implements View.OnClickListener{
    ArrayList<String> options;
    IButtonFragmentAnswerListener callBack;
    SendMessage sendMessage;
    Button[] buttons;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callBack = (IButtonFragmentAnswerListener)context;
            sendMessage = (SendMessage) context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement MyDialogListener and SendMessage interface");
        }
    }

    public static ButtonsFragment newInstance(List<String> options) {
        ButtonsFragment buttonsFragment = new ButtonsFragment();
        Bundle bundle = new Bundle();
        if (options != null) {
            bundle.putStringArrayList("options", (ArrayList<String>)options);
        }
        buttonsFragment.setArguments(bundle);
        return buttonsFragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buttons,container,false);
        LinearLayout buttonLayout = rootView.findViewById(R.id.buttons_layout);
        options = getArguments().getStringArrayList("options");
        if (options != null) {
            buttons = new Button[options.size()];
            for (int i = 0; i < options.size(); i++) {
                buttons[i] = new Button(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(15,20,15,30);
                lp.weight = 1;
                buttons[i].setLayoutParams(lp);
                buttons[i].setText(options.get(i));
                buttons[i].setOnClickListener(this);
                buttons[i].setBackgroundResource(R.drawable.button_4_design);
//                Typeface typeface = ResourcesCompat.getFont(this.getContext(),R.font.ganclm);
//                buttons[i].setTypeface(typeface, Typeface.BOLD);
                buttons[i].setTextAppearance(R.style.FontNumbers);

                buttons[i].setTextSize(18f);
                buttons[i].setTextColor(Color.WHITE);
                buttons[i].setOnTouchListener(new ButtonTouchAnimation());
                buttonLayout.addView(buttons[i]);
            }
        }
        else {
            int[] nums = new int[] {R.drawable.btn_delete, R.drawable.btn_zero, R.drawable.btn_one, R.drawable.btn_two, R.drawable.btn_three, R.drawable.btn_four
                    , R.drawable.btn_five, R.drawable.btn_six, R.drawable.btn_seven, R.drawable.btn_eight, R.drawable.btn_nine, R.drawable.btn_enter};
            for (int i = 0; i < 12; i++) {
                Button btn = new Button(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150, 150);
                lp.setMargins(10,10,10,10);
                lp.weight = 1;
                btn.setLayoutParams(lp);
                btn.setBackgroundResource(nums[i]);
                btn.setId(i);
                btn.setOnClickListener(this);
                btn.setTextColor(Color.WHITE);
                buttonLayout.addView(btn);
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {

        if (options != null) {
            String buttonText = ((Button)v).getText().toString();
            if (!buttonText.contains("/")) {
                callBack.onReturn(Float.parseFloat(buttonText));
            }
            else {
                String split[] = buttonText.split("/");
                callBack.onReturn(Float.parseFloat(split[0]) / Float.parseFloat(split[1]));
            }
        }
        else {
            char numToAdd = 'E';
            switch (v.getId()) {
                case 1:
                    numToAdd = '0';
                    break;
                case 2:
                    numToAdd = '1';
                    break;
                case 3:
                    numToAdd = '2';
                    break;
                case 4:
                    numToAdd = '3';
                    break;
                case 5:
                    numToAdd = '4';
                    break;
                case 6:
                    numToAdd = '5';
                    break;
                case 7:
                    numToAdd = '6';
                    break;
                case 8:
                    numToAdd = '7';
                    break;
                case 9:
                    numToAdd = '8';
                    break;
                case 10:
                    numToAdd = '9';
                    break;
                case 0:
                    numToAdd = '-';
                    break;
            }
            sendMessage.sendUserInput(numToAdd);
        }
    }

    public void returnResult(String result) {
        callBack.onReturn(Integer.parseInt(result));
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
