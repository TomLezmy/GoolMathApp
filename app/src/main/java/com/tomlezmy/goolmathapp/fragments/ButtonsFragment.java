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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.ButtonTouchAnimation;
import com.tomlezmy.goolmathapp.interfaces.IButtonFragmentAnswerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment displays the answer options during the game in activity {@link com.tomlezmy.goolmathapp.activities.GamePage}
 */
public class ButtonsFragment extends Fragment implements View.OnClickListener{
    ArrayList<String> options;
    IButtonFragmentAnswerListener callBack;
    Button[] buttons;

    /**
     * When fragment attaches to the activity, the {@link IButtonFragmentAnswerListener} is attached to it
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            callBack = (IButtonFragmentAnswerListener)context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement MyDialogListener interface");
        }
    }

    /**
     * This method creates a new instance of {@link ButtonsFragment} with parameters in a {@link Bundle}
     * @param options A list of answer options to display
     * @param isTutorial True if the current level is a tutorial
     * @return A new instance of {@link ButtonsFragment}
     */
    public static ButtonsFragment newInstance(List<String> options, boolean isTutorial) {
        ButtonsFragment buttonsFragment = new ButtonsFragment();
        Bundle bundle = new Bundle();

        bundle.putStringArrayList("options", (ArrayList<String>) options);
        bundle.putBoolean("isTutorial", isTutorial);

        buttonsFragment.setArguments(bundle);
        return buttonsFragment;
    }

    /**
     * Creates buttons to display answer options
     */
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

            if (options.get(i).contains("\n")) {
                buttons[i].setTextSize(9f);
            }
            else {
                buttons[i].setTextSize(18f);
            }
            buttons[i].setTextColor(Color.WHITE);
            buttons[i].setOnTouchListener(new ButtonTouchAnimation());
            buttonLayout.addView(buttons[i]);
        }
        // If tutorial, don't allow user presses until needed
        if (getArguments().getBoolean("isTutorial")) {
            disableButtons();
        }

        return rootView;
    }

    /**
     * Called when user picks an answer. The method notify's the chosen answer to the listening activity
     */
    @Override
    public void onClick(View v) {
        String buttonText = ((Button) v).getText().toString();
        if (!buttonText.contains("/")) {
            callBack.onReturn(Float.parseFloat(buttonText));
        } else {
            String[] split = buttonText.split("/");
            callBack.onReturn(Float.parseFloat(split[0]) / Float.parseFloat(split[1]));
        }
    }

    /**
     * Sets all answer option buttons to enabled
     */
    public void enableButtons() {
        for (Button button : buttons) {
            button.setEnabled(true);
        }
    }

    /**
     * Sets all answer option buttons to disabled
     */
    public void disableButtons() {
        for (Button button : buttons) {
            button.setEnabled(false);
        }
    }
}
