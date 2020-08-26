package com.tomlezmy.goolmathapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tomlezmy.goolmathapp.R;

/**
 * This fragment displays the questions during the game in activity {@link com.tomlezmy.goolmathapp.activities.GamePage}
 */
public class QuestionFragment extends Fragment {

    String question;
    TextView questionText;

    /**
     * Class constructor
     * @param question The current question to display
     */
    public QuestionFragment(String question) {
        this.question = question;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question,container,false);
        questionText = rootView.findViewById(R.id.question_text);
        questionText.setText(question);
        questionText.setTextAppearance(R.style.FontLocalized);
        int questionLen = questionText.length();
        if (questionLen < 11) {
            questionText.setTextSize(17f);
        } else {
            questionText.setTextSize(14f);
        }
        // For fractions
        if (question.contains("\n")) {
            questionText.setTextSize(14f);
        }
        return rootView;
    }
}
