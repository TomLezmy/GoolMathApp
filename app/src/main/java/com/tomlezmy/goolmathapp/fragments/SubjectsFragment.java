package com.tomlezmy.goolmathapp.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import com.tomlezmy.goolmathapp.R;

import java.util.Locale;


public class SubjectsFragment extends Fragment {

    // Data members
    int cardColorId;
    int titleColorId;
    CardView additionCard;
    CardView subtractionCard;
    CardView multiplicationCard;
    TextView tvMultiplicationCard;
    CardView divisionCard;
    CardView fractionsCard;
    CardView percentagesCard;
    CardView decimal_numbersCard;
    ArrayAdapter<String> adapter;
    String language;
    Typeface typeface;

    public interface OnSelectedSubCategoryListener {
        void onSelectedSubCategory(int categoryId, int subCategoryId);
    }

    OnSelectedSubCategoryListener callback;

    // Constructor
    public SubjectsFragment(int cardColorId, int titleColorId) {
        this.cardColorId = cardColorId;
        this.titleColorId = titleColorId;
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

        /* References to all cards and set On click listener using private class
            named CategoryCardsOnClickListener */
        additionCard = rootView.findViewById(R.id.card_addition_category);
        additionCard.setOnClickListener(new CategoryCardsOnClickListener(0,getResources().getStringArray(R.array.additionSubCategories)));
        subtractionCard = rootView.findViewById(R.id.card_subtraction_category);
        subtractionCard.setOnClickListener(new CategoryCardsOnClickListener(1,getResources().getStringArray(R.array.subtractionSubCategories)));
        multiplicationCard = rootView.findViewById(R.id.card_multiplication_category);
        tvMultiplicationCard = rootView.findViewById(R.id.tv_multiplicationCard);

        // Set unique text size for multiplication Card for english
        this.language = Locale.getDefault().getDisplayLanguage();
        if ( this.language.equalsIgnoreCase("English")) {
            this.typeface = ResourcesCompat.getFont(getContext(), R.font.patua_one);
            tvMultiplicationCard.setTextSize(14f);
        }
        else {
            this.typeface = ResourcesCompat.getFont(getContext(), R.font.hebrew_font);
        }

        multiplicationCard.setOnClickListener(new CategoryCardsOnClickListener(2,getResources().getStringArray(R.array.multiplicationSubCategories)));
        divisionCard = rootView.findViewById(R.id.card_division_category);
        divisionCard.setOnClickListener(new CategoryCardsOnClickListener(3,getResources().getStringArray(R.array.divisionSubCategories)));

        // !!! I need to add the sub categories string array in strings.xml for those categories
        fractionsCard = rootView.findViewById(R.id.card_fractions_category);
        fractionsCard.setOnClickListener(new CategoryCardsOnClickListener(4, getResources().getStringArray(R.array.fractionsSubCategories)));
        percentagesCard = rootView.findViewById(R.id.card_percentages_category);
        percentagesCard.setOnClickListener(new CategoryCardsOnClickListener(5, getResources().getStringArray(R.array.percentsSubCategories)));
        decimal_numbersCard = rootView.findViewById(R.id.card_decimal_numbers_category);
        decimal_numbersCard.setOnClickListener(new CategoryCardsOnClickListener(6, getResources().getStringArray(R.array.decimalsSubCategories)));

        // Set card color
        additionCard.setCardBackgroundColor(this.cardColorId);
        subtractionCard.setCardBackgroundColor(this.cardColorId);
        multiplicationCard.setCardBackgroundColor(this.cardColorId);
        divisionCard.setCardBackgroundColor(this.cardColorId);
        fractionsCard.setCardBackgroundColor(this.cardColorId);
        percentagesCard.setCardBackgroundColor(this.cardColorId);
        decimal_numbersCard.setCardBackgroundColor(this.cardColorId);

        return rootView;
    }

    // Set Card's Style and create list view to show all sub categories each time we click on a category
    void displaySubCategories(final int categoryIndex, String[] subCategoriesArray) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);


        TextView titleTv = new TextView(getContext());
        titleTv.setText(R.string.pick_sub_category_txt);
        titleTv.setTextSize(22f);
        titleTv.setGravity(Gravity.CENTER);
        titleTv.setPadding(20,5,20,5);
        titleTv.setTextColor(this.titleColorId);
        titleTv.setTypeface(this.typeface, Typeface.BOLD);

        builder.setCustomTitle(titleTv);
        adapter = new ArrayAdapter<String>(getContext(), R.layout.sub_category_list_view, subCategoriesArray);
        builder.setAdapter(adapter,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Send back to the relevant activity,
                callback.onSelectedSubCategory(categoryIndex, which);
                dialog.dismiss();
            }
        }).show();
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
