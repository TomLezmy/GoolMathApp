package com.tomlezmy.goolmathapp.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
    TextView tvSubtractionCard;
    TextView tvPercentageCard;
    CardView divisionCard;
    CardView fractionsCard;
    CardView percentagesCard;
    CardView decimal_numbersCard;
    CardView tutorialCard;
    String language;
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
        tutorialCard = rootView.findViewById(R.id.card_Tutorial);

        // Set card color
        additionCard.setCardBackgroundColor(this.cardColorId);
        subtractionCard.setCardBackgroundColor(this.cardColorId);
        multiplicationCard.setCardBackgroundColor(this.cardColorId);
        divisionCard.setCardBackgroundColor(this.cardColorId);
        fractionsCard.setCardBackgroundColor(this.cardColorId);
        percentagesCard.setCardBackgroundColor(this.cardColorId);
        decimal_numbersCard.setCardBackgroundColor(this.cardColorId);

        tutorialCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.tutorial_card_color));

        // Set unique text size for some Card for english
        tvMultiplicationCard = rootView.findViewById(R.id.tv_multiplicationCard);
        tvSubtractionCard = rootView.findViewById(R.id.tv_subtraction);
        tvPercentageCard = rootView.findViewById(R.id.tv_percentage);
        this.language = Locale.getDefault().getDisplayLanguage();
        if ( this.language.equalsIgnoreCase("English")) {
            tvMultiplicationCard.setTextSize(12.5f);
            tvSubtractionCard.setTextSize(13.5f);
            tvPercentageCard.setTextSize(14);
        }


        if (this.isCreatedByLearnSelectActivity) {

            /* Set On click listener using private class named CategoryCardsOnClickListener */
            additionCard.setOnClickListener(new CategoryCardsOnClickListener(0, getResources().getStringArray(R.array.learn_additionSubCategories)));
            subtractionCard.setEnabled(false);
            subtractionCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            multiplicationCard.setOnClickListener(new CategoryCardsOnClickListener(2, getResources().getStringArray(R.array.learn_multiplicationSubCategories)));
            divisionCard.setOnClickListener(new CategoryCardsOnClickListener(3, getResources().getStringArray(R.array.learn_divisionSubCategories)));
            fractionsCard.setOnClickListener(new CategoryCardsOnClickListener(4, getResources().getStringArray(R.array.learn_fractionsSubCategories)));
            percentagesCard.setOnClickListener(new CategoryCardsOnClickListener(5, getResources().getStringArray(R.array.learn_percentsSubCategories)));
            decimal_numbersCard.setOnClickListener(new CategoryCardsOnClickListener(6, getResources().getStringArray(R.array.learn_decimalsSubCategories)));
            tutorialCard.setVisibility(View.GONE);
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
            tutorialCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onSelectedSubCategory(-1);
                }
            });
            // Check which categories are open
            fileManager = FileManager.getInstance(getContext());
            Dictionary<ECategory, List<CategoryProgressData>> levelProgressData = fileManager.getUserData().getLevelsProgressData();
            if (!levelProgressData.get(ECategory.SUBTRACTION).get(0).isOpen()) {
                //subtractionCard.setEnabled(false);
                subtractionCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            }
            if (!levelProgressData.get(ECategory.MULTIPLICATION).get(0).isOpen()) {
                //multiplicationCard.setEnabled(false);
                multiplicationCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            }
            if (!levelProgressData.get(ECategory.DIVISION).get(0).isOpen()) {
                //divisionCard.setEnabled(false);
                divisionCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            }
            if (!levelProgressData.get(ECategory.FRACTIONS).get(0).isOpen()) {
                //fractionsCard.setEnabled(false);
                fractionsCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            }
            if (!levelProgressData.get(ECategory.PERCENTS).get(0).isOpen()) {
                //percentagesCard.setEnabled(false);
                percentagesCard.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.disabled_card));
            }
            if (!levelProgressData.get(ECategory.DECIMALS).get(0).isOpen()) {
                //decimal_numbersCard.setEnabled(false);
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
        public void onClick(final View v) {
            if (!fileManager.getUserData().getLevelsProgressData().get(ECategory.values()[categoryIndex]).get(0).isOpen()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.subject_closed_title).setMessage(R.string.subject_closed_message);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton(R.string.skip, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((CardView)v).setCardBackgroundColor(cardColorId);
                        fileManager.getUserData().getLevelsProgressData().get(ECategory.values()[categoryIndex]).get(0).setOpen(true);
                        fileManager.updateUserDataFile();
                        displaySubCategories(categoryIndex, subCategoriesArray);
                    }
                });
                builder.create().show();
            }
            else {
                displaySubCategories(this.categoryIndex, this.subCategoriesArray);
            }
        }
    }
}
