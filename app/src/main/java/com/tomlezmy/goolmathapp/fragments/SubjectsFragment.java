package com.tomlezmy.goolmathapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.tomlezmy.goolmathapp.R;

public class SubjectsFragment extends Fragment {

    // Data members
    int cardsColor;
    CardView additionCard;
    CardView subtractionCard;
    CardView multiplicationCard;
    CardView divisionCard;
    CardView fractionsCard;
    CardView percentagesCard;
    CardView decimal_numbersCard;

    // Constructor
    public SubjectsFragment(int cardsColor) {
        this.cardsColor = cardsColor;
        // ... set cards color dynamically
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subjects,container,false);

        // References to all cards
        additionCard = rootView.findViewById(R.id.card_addition_category);
        subtractionCard = rootView.findViewById(R.id.card_subtraction_category);
        multiplicationCard = rootView.findViewById(R.id.card_multiplication_category);
        divisionCard = rootView.findViewById(R.id.card_division_category);
        fractionsCard = rootView.findViewById(R.id.card_fractions_category);
        percentagesCard = rootView.findViewById(R.id.card_percentages_category);
        decimal_numbersCard = rootView.findViewById(R.id.card_decimal_numbers_category);

        // Set card color
        additionCard.setCardBackgroundColor(this.cardsColor);
        subtractionCard.setCardBackgroundColor(this.cardsColor);
        multiplicationCard.setCardBackgroundColor(this.cardsColor);
        divisionCard.setCardBackgroundColor(this.cardsColor);
        fractionsCard.setCardBackgroundColor(this.cardsColor);
        percentagesCard.setCardBackgroundColor(this.cardsColor);
        decimal_numbersCard.setCardBackgroundColor(this.cardsColor);
//        questionText.setText(question);
//        questionText.setTextAppearance(R.style.FontLocalized);
//        questionText.setTextSize(20f);
        return rootView;
    }


}
