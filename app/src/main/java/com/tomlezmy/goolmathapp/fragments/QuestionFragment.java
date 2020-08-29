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
    String numOne, numTwo, fractionQuestionPart1, fractionQuestionPart2, fractionQuestionPart3;
    TextView questionText1, questionText2, questionText3;
    boolean isFractionCategory;

    /**
     * Class constructor
     * @param question The current question to display
     */
    public QuestionFragment(String question) {
        if (question.contains("_")) {
            // split fraction question
            String[] separated = question.split("_");
            numOne = separated[0];
            numTwo = separated[1];
            this.isFractionCategory = true;
        }
        else {
            this.question = question;
            this.isFractionCategory = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question,container,false);
        questionText = rootView.findViewById(R.id.question_text);
        questionText1 = rootView.findViewById(R.id.question_text1);
        questionText2 = rootView.findViewById(R.id.question_text2);
        questionText3 = rootView.findViewById(R.id.question_text3);

        if (isFractionCategory) {
            if (numOne.length() == 1 && numTwo.length() == 1) {
                fractionQuestionPart1 = String.format(getResources().getString(R.string.f1_param), numOne,"", numTwo);

            } else if (numOne.length() > 1 && numTwo.length() > 1) {
                fractionQuestionPart1 = String.format(getResources().getString(R.string.f1_param), numOne,"  ", numTwo);
            } else {
                fractionQuestionPart1 = String.format(getResources().getString(R.string.f1_param), numOne," ", numTwo);
            }
            fractionQuestionPart2 = String.format(getResources().getString(R.string.f2_param));
            fractionQuestionPart3 = String.format(getResources().getString(R.string.f3_param));

            questionText1.setText(fractionQuestionPart1);
            questionText2.setText(fractionQuestionPart2);
            questionText3.setText(fractionQuestionPart3);

            questionText1.setVisibility(View.VISIBLE);
            questionText2.setVisibility(View.VISIBLE);
            questionText3.setVisibility(View.VISIBLE);
            questionText.setVisibility(View.GONE);

        } else {
            questionText1.setVisibility(View.GONE);
            questionText2.setVisibility(View.GONE);
            questionText3.setVisibility(View.GONE);
            questionText.setVisibility(View.VISIBLE);
            questionText.setText(question);
        }

        questionText.setTextAppearance(R.style.FontLocalized);
        int questionLen = questionText.length();
        if (questionLen < 11) {
            questionText.setTextSize(17f);
        } else {
            questionText.setTextSize(14f);
        }
        return rootView;
    }
}
