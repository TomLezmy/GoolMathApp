package com.tomlezmy.goolmathapp.fragments;

import android.content.Intent;
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
import com.tomlezmy.goolmathapp.activities.GamePage;

import java.util.Locale;

/**
 * This fragment displays all category's in {@link com.tomlezmy.goolmathapp.game.ECategory} to pick a subject for {@link GamePage}
 */
public class PracticeSelectFragment extends Fragment {

    public SubjectsFragment subjectsFragment;
    int categorySelected;
    int subCategorySelected;
    HTextView tvQuestion_hanks;
    String secondTitle;
    String language;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_practice_select, container, false);

        this.tvQuestion_hanks = rootView.findViewById(R.id.tv_hanks);

        this.language = Locale.getDefault().getDisplayLanguage();
        if ( this.language.equalsIgnoreCase("English")) {
            tvQuestion_hanks.setTextSize(33f);
        }
        else {
            tvQuestion_hanks.setTextSize(65f);
            tvQuestion_hanks.setPadding(10,10,10,10);
        }

        secondTitle = getResources().getString(R.string.title_practice_sub_categores_option2);
        tvQuestion_hanks.animateText(secondTitle);

        return rootView;
    }

    /**
     * When resuming fragment, reload {@link SubjectsFragment} to display changes
     */
    @Override
    public void onResume() {
        super.onResume();
        displaySubjects();
    }

    /**
     * Creates and displays the category's in card views using {@link SubjectsFragment}
     */
    public void displaySubjects() {
        int cardColorId = ContextCompat.getColor(getContext(), R.color.blue_card_option3);
        int titleColorId = ContextCompat.getColor(getContext(), R.color.blue_title);
        subjectsFragment = new SubjectsFragment(cardColorId,titleColorId,false);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.subjects_display_layout, subjectsFragment, "Subjects_TAG").commit();
    }

    /**
     * Called when a category is selected by the user
     * @param categoryId The chosen category index, is -1 when level is tutorial level
     */
    public void onSelectedSubCategory(int categoryId) {
        this.categorySelected = categoryId;
        if (categoryId == -1) {
            Intent intent = new Intent(getContext(), GamePage.class);
            intent.putExtra("tutorial",true);
            startActivity(intent);
        }
    }

    /**
     * Called when a sub category is selected by the user
     * @param subCategoryId The chosen sub category index
     */
    public void onSubCategory(int subCategoryId) {
        this.subCategorySelected = subCategoryId;
        Intent intent = new Intent(getContext(), GamePage.class);
        intent.putExtra("category", this.categorySelected);
        intent.putExtra("level", this.subCategorySelected + 1);
        startActivity(intent);
    }
}
