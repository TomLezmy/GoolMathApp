package com.tomlezmy.goolmathapp.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tomlezmy.goolmathapp.FileManager;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.game.CategoryProgressData;
import com.tomlezmy.goolmathapp.game.ECategory;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;
import java.util.Locale;


public class SubjectsFragment extends Fragment implements SubCategoriesFragment.OnSubCategoryListener {
    @Override
    public void onSubCategory(int subCategoryId) {
        callback.onSelectedSubCategory(this.currentCategoryIndex);

    }

    // Data members
    int cardColorId;
    int titleColorId;
    int currentCategoryIndex;
    boolean isCreatedByLearnSelectActivity;
    CardView additionCard;
    CardView subtractionCard;
    CardView multiplicationCard;
    TextView tvMultiplicationCard;
    CardView divisionCard;
    CardView fractionsCard;
    CardView percentagesCard;
    CardView decimal_numbersCard;
    String language;
    Typeface typeface;
    List<String> subCategories;
    SubCategoriesFragment subCategoriesFragment;
    FileManager fileManager;


    // interface
    public interface OnSelectedSubCategoryListener {
        void onSelectedSubCategory(int categoryId);
    }

    OnSelectedSubCategoryListener callback;

    // Constructor
    public SubjectsFragment(int cardColorId, int titleColorId, boolean isCreatedByLearnSelectActivity) {
        this.cardColorId = cardColorId;
        this.titleColorId = titleColorId;
        this.isCreatedByLearnSelectActivity = isCreatedByLearnSelectActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnSelectedSubCategoryListener)context;
        } catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement on OnSelectedSubCategoryListener interface");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_subjects,container,false);

        /* References to all cards */
        additionCard = rootView.findViewById(R.id.card_addition_category);
        subtractionCard = rootView.findViewById(R.id.card_subtraction_category);
        multiplicationCard = rootView.findViewById(R.id.card_multiplication_category);
        divisionCard = rootView.findViewById(R.id.card_division_category);
        fractionsCard = rootView.findViewById(R.id.card_fractions_category);
        percentagesCard = rootView.findViewById(R.id.card_percentages_category);
        decimal_numbersCard = rootView.findViewById(R.id.card_decimal_numbers_category);

        // Set card color
        additionCard.setCardBackgroundColor(this.cardColorId);
        subtractionCard.setCardBackgroundColor(this.cardColorId);
        multiplicationCard.setCardBackgroundColor(this.cardColorId);
        divisionCard.setCardBackgroundColor(this.cardColorId);
        fractionsCard.setCardBackgroundColor(this.cardColorId);
        percentagesCard.setCardBackgroundColor(this.cardColorId);
        decimal_numbersCard.setCardBackgroundColor(this.cardColorId);

        // Set unique text size for multiplication Card for english
        tvMultiplicationCard = rootView.findViewById(R.id.tv_multiplicationCard);
        this.language = Locale.getDefault().getDisplayLanguage();
        if ( this.language.equalsIgnoreCase("English")) {
            this.typeface = ResourcesCompat.getFont(getContext(), R.font.helsinki);
            tvMultiplicationCard.setTextSize(15f);
        }
        else {
            this.typeface = ResourcesCompat.getFont(getContext(), R.font.hebrew_font);
        }

        if (this.isCreatedByLearnSelectActivity) {

            /* Set On click listener using private class named CategoryCardsOnClickListener */
            additionCard.setOnClickListener(new CategoryCardsOnClickListener(0, getResources().getStringArray(R.array.learn_additionSubCategories)));
            multiplicationCard.setOnClickListener(new CategoryCardsOnClickListener(2, getResources().getStringArray(R.array.learn_multiplicationSubCategories)));
            divisionCard.setOnClickListener(new CategoryCardsOnClickListener(3, getResources().getStringArray(R.array.learn_divisionSubCategories)));
            fractionsCard.setOnClickListener(new CategoryCardsOnClickListener(4, getResources().getStringArray(R.array.learn_fractionsSubCategories)));
            percentagesCard.setOnClickListener(new CategoryCardsOnClickListener(5, getResources().getStringArray(R.array.learn_percentsSubCategories)));
            decimal_numbersCard.setOnClickListener(new CategoryCardsOnClickListener(6, getResources().getStringArray(R.array.learn_decimalsSubCategories)));

        }
        else {
            /* Set On click listener using private class named CategoryCardsOnClickListener */
            additionCard.setOnClickListener(new CategoryCardsOnClickListener(0, getResources().getStringArray(R.array.practice_additionSubCategories)));
            subtractionCard.setOnClickListener(new CategoryCardsOnClickListener(1,getResources().getStringArray(R.array.practice_subtractionSubCategories)));
            multiplicationCard.setOnClickListener(new CategoryCardsOnClickListener(2, getResources().getStringArray(R.array.practice_multiplicationSubCategories)));
            divisionCard.setOnClickListener(new CategoryCardsOnClickListener(3, getResources().getStringArray(R.array.practice_divisionSubCategories)));
            fractionsCard.setOnClickListener(new CategoryCardsOnClickListener(4, getResources().getStringArray(R.array.practice_fractionsSubCategories)));
            percentagesCard.setOnClickListener(new CategoryCardsOnClickListener(5, getResources().getStringArray(R.array.practice_percentsSubCategories)));
            decimal_numbersCard.setOnClickListener(new CategoryCardsOnClickListener(6, getResources().getStringArray(R.array.practice_decimalsSubCategories)));

            // Check which categories are open
            fileManager = FileManager.getInstance(getContext());
            Dictionary<ECategory, List<CategoryProgressData>> levelProgressData = fileManager.getUserData().getLevelsProgressData();
            if (!levelProgressData.get(ECategory.SUBTRACTION).get(0).isOpen()) {
                subtractionCard.setEnabled(false);
                subtractionCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            }
            if (!levelProgressData.get(ECategory.MULTIPLICATION).get(0).isOpen()) {
                multiplicationCard.setEnabled(false);
                multiplicationCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            }
            if (!levelProgressData.get(ECategory.DIVISION).get(0).isOpen()) {
                divisionCard.setEnabled(false);
                divisionCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            }
            if (!levelProgressData.get(ECategory.FRACTIONS).get(0).isOpen()) {
                fractionsCard.setEnabled(false);
                fractionsCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            }
            if (!levelProgressData.get(ECategory.PERCENTS).get(0).isOpen()) {
                percentagesCard.setEnabled(false);
                percentagesCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            }
            if (!levelProgressData.get(ECategory.DECIMALS).get(0).isOpen()) {
                decimal_numbersCard.setEnabled(false);
                decimal_numbersCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            }
        }

        return rootView;
    }

    // Set Card's Style and create list view to show all sub categories each time we click on a category
    void displaySubCategories(final int categoryIndex, String[] subCategoriesArray) {
        this.currentCategoryIndex = categoryIndex;
        this.subCategories = Arrays.asList(subCategoriesArray);
        if (isCreatedByLearnSelectActivity) {
            this.subCategoriesFragment = new SubCategoriesFragment(this.subCategories, this.cardColorId);
        }
        else {
            this.subCategoriesFragment = new SubCategoriesFragment(this.subCategories, this.cardColorId, fileManager.getUserData().getLevelsProgressData().get(ECategory.values()[categoryIndex]));
        }
        final FragmentManager fm = getFragmentManager();
        subCategoriesFragment.show(fm, "sub_categories_tag");
        callback.onSelectedSubCategory(categoryIndex);
    }

    // Private class for handling cards on click listener
    private class CategoryCardsOnClickListener implements View.OnClickListener {
        String[] subCategoriesArray;
        int categoryIndex;
        // constructor
        CategoryCardsOnClickListener(int categoryIndex, String[] subCategoriesArray) {
            this.categoryIndex = categoryIndex;
            this.subCategoriesArray = subCategoriesArray;
        }
        @Override
        public void onClick(View v) {
            displaySubCategories(this.categoryIndex,this.subCategoriesArray);

        }
    }
}
