package com.tomlezmy.goolmathapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.hanks.htextview.base.HTextView;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.interfaces.IFragmentChangeListener;

import java.util.Locale;

public class LearnSelectFragment extends Fragment {

    SubjectsFragment subjectsFragment;
    int categorySelected;
    int subCategorySelected;
    HTextView tvQuestion_hanks;
    String secondTitle;
    String language;
    IFragmentChangeListener callBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callBack = (IFragmentChangeListener)context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement IFragmentChangeListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_learn_select, container, false);
        this.tvQuestion_hanks = (HTextView) rootView.findViewById(R.id.tv_hanks);

        this.language = Locale.getDefault().getDisplayLanguage();
        if ( this.language.equalsIgnoreCase("English")) {
            tvQuestion_hanks.setTextSize(35f);
        }
        else {
            tvQuestion_hanks.setTextSize(70f);
            tvQuestion_hanks.setPadding(20,20,20,20);
        }

        secondTitle = getResources().getString(R.string.title_learn_sub_categories_option2);
        tvQuestion_hanks.animateText(secondTitle);

        displaySubjectsForLearn();

        return rootView;
    }

    // Create SubjectFragment in LearnSelectActivity
    public void displaySubjectsForLearn() {
        int cardColorId = ContextCompat.getColor(getContext(), R.color.green_card_option3);
        int titleColorId = ContextCompat.getColor(getContext(), R.color.green_title);
        subjectsFragment = new SubjectsFragment(cardColorId, titleColorId, true);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.subjects_display_layout, subjectsFragment, "Subjects_TAG").commit();
    }

    public void onSubCategory(int subCategoryId) {
        this.subCategorySelected = subCategoryId;

//        Intent intent = new Intent(getContext(), LearnActivity.class);
//        intent.putExtra("category", this.categorySelected);
//        intent.putExtra("sub_category", this.subCategorySelected);
//        startActivity(intent);
    }

    public void onSelectedSubCategory(int categoryId) {
        this.categorySelected = categoryId;
    }

    public int getCategorySelected() {
        return categorySelected;
    }
}
