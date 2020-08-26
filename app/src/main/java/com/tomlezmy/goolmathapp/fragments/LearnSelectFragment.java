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

/**
 * This fragment displays all category's in {@link com.tomlezmy.goolmathapp.game.ECategory} to pick a subject for {@link LearnFragment}
 */
public class LearnSelectFragment extends Fragment {

    SubjectsFragment subjectsFragment;
    int categorySelected;
    HTextView tvQuestion_hanks;
    String secondTitle;
    String language;
    IFragmentChangeListener callBack;

    /**
     * When fragment attaches to the activity, the {@link IFragmentChangeListener} is attached to it
     */
    @Override
    public void onAttach(@NonNull Context context) {
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

    /**
     * Creates and displays the category's in card views using {@link SubjectsFragment}
     */
    public void displaySubjectsForLearn() {
        int cardColorId = ContextCompat.getColor(getContext(), R.color.green_card_option3);
        int titleColorId = ContextCompat.getColor(getContext(), R.color.green_title);
        subjectsFragment = new SubjectsFragment(cardColorId, titleColorId, true);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.subjects_display_layout, subjectsFragment, "Subjects_TAG").commit();
    }

    /**
     * Called when a category is selected by the user
     * @param categoryId The chosen category index
     */
    public void onSelectedSubCategory(int categoryId) {
        this.categorySelected = categoryId;
    }

    public int getCategorySelected() {
        return categorySelected;
    }
}
