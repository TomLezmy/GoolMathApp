package com.tomlezmy.goolmathapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.interfaces.SendMessage;

public class QuestionFragment extends Fragment {

    String question;
    StringBuilder userInput;
    TextView questionText;
    SendMessage sendMessage;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            sendMessage = (SendMessage) context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement SendMessage interface");
        }
    }

    public QuestionFragment(String question) {
        this.question = question;
        userInput = new StringBuilder();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question,container,false);
        questionText = rootView.findViewById(R.id.question_text);
        questionText.setText(question);
        return rootView;
    }

    public void updateUserInput(char addition) {
        if (addition == '-') {
            if (userInput.length() > 0) {
                userInput.deleteCharAt(userInput.length() - 1);
            }
            else {
                return;
            }
        }
        else if (addition == 'E'){
            if (userInput.length() > 0) {
                sendMessage.sendResult(userInput.toString());
            }
            else {
                return;
            }
        }
        else {
            userInput.append(addition);
        }
        questionText.setText(question.substring(0, question.length() - 2) + userInput.toString());
    }
}
