package com.tomlezmy.goolmathapp.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tomlezmy.goolmathapp.FileManager;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.game.ECategory;
import com.tomlezmy.goolmathapp.game.UserData;

public class ProgressFragment extends Fragment {

    public static ProgressFragment newInstance(int num) {
        ProgressFragment progressFragment = new ProgressFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("category",num);
        progressFragment.setArguments(bundle);
        return progressFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_progress, container, false);
//        TextView textView = rootView.findViewById(R.id.test);
        int[] stringArrays = new int[] {R.array.practice_additionSubCategories, R.array.practice_subtractionSubCategories, R.array.practice_multiplicationSubCategories, R.array.practice_decimalsSubCategories, R.array.practice_fractionsSubCategories, R.array.practice_percentsSubCategories, R.array.practice_decimalsSubCategories};
//        StringBuilder stringBuilder = new StringBuilder();
        FileManager fileManager = FileManager.getInstance(getContext());
        ECategory category = ECategory.values()[getArguments().getInt("category")];
        UserData userData = fileManager.getUserData();
//        for (int i = 0; i < category.getNumberOfLevels(); i++) {
//            stringBuilder.append(getResources().getStringArray(stringArrays[getArguments().getInt("category")])[i] + " - Times Played : " + userData.getLevelsProgressData().get(category).get(i).getTimesPlayed() + ", Highscore: " + userData.getLevelsProgressData().get(category).get(i).getMaxScore() + "\n");
//        }
//        textView.setText(stringBuilder.toString());


        TableLayout tableLayout = rootView.findViewById(R.id.progress_table_layout);
        // Creating the table headers
        TableRow topTableRow = new TableRow  (getContext());
        topTableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        tableLayout.addView(topTableRow);

        TextView topTextView = new TextView(getContext());
        topTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.4f));
        topTextView.setText("Level");
        topTextView.setTextSize(20f);
        topTextView.setGravity(Gravity.CENTER);
        topTextView.setTypeface(null, Typeface.BOLD);
        topTableRow.addView(topTextView);

        topTextView = new TextView(getContext());
        topTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        topTextView.setText("Highscore");
        topTextView.setTextSize(20f);
        topTextView.setGravity(Gravity.CENTER);
        topTextView.setTypeface(null, Typeface.BOLD);
        topTableRow.addView(topTextView);

        topTextView = new TextView(getContext());
        topTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        topTextView.setText("Times played");
        topTextView.setTextSize(20f);
        topTextView.setGravity(Gravity.CENTER);
        topTextView.setTypeface(null, Typeface.BOLD);
        topTableRow.addView(topTextView);
        for (int i = 0; i < category.getNumberOfLevels(); i++) {
            TableRow tb = new TableRow  (getContext());
            tb.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            tableLayout.addView(tb);

            TextView tv = new TextView(getContext());
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.4f));
            tv.setText(getResources().getStringArray(stringArrays[getArguments().getInt("category")])[i]);
            tv.setTextSize(10f);
            tv.setGravity(Gravity.CENTER);
            tb.addView(tv);

            tv = new TextView(getContext());
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            tv.setText(userData.getLevelsProgressData().get(category).get(i).getTimesPlayed() + "");
            tv.setTextSize(10f);
            tv.setGravity(Gravity.CENTER);
            tb.addView(tv);

            tv = new TextView(getContext());
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            tv.setText(userData.getLevelsProgressData().get(category).get(i).getMaxScore() + "");
            tv.setTextSize(10f);
            tv.setGravity(Gravity.CENTER);
            tb.addView(tv);
        }

        return rootView;
    }
}
