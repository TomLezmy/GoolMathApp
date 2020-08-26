package com.tomlezmy.goolmathapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tomlezmy.goolmathapp.model.FileManager;
import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.adapters.ProgressResultsAdapter;
import com.tomlezmy.goolmathapp.game.ECategory;
import com.tomlezmy.goolmathapp.model.UserData;
import com.tomlezmy.goolmathapp.game.ProgressResult;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment displays the progress data in all levels of the current category
 */
public class ProgressFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressResultsAdapter progressResultsAdapter;
    ArrayList<ProgressResult> progressResultList;

    /**
     * This method creates a new instance of {@link ProgressFragment} with parameters in a {@link Bundle}
     * @param categoryIndex The category index
     * @return A new instance of  {@link ProgressFragment}
     */
    public static ProgressFragment newInstance(int categoryIndex) {
        ProgressFragment progressFragment = new ProgressFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("category", categoryIndex);
        progressFragment.setArguments(bundle);
        return progressFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_progress, container, false);
        int[] stringArrays = new int[] {R.array.practice_additionSubCategories, R.array.practice_subtractionSubCategories, R.array.practice_multiplicationSubCategories, R.array.practice_decimalsSubCategories, R.array.practice_fractionsSubCategories, R.array.practice_percentsSubCategories, R.array.practice_decimalsSubCategories};
        FileManager fileManager = FileManager.getInstance(getContext());
        ECategory category = ECategory.values()[getArguments().getInt("category")];
        UserData userData = fileManager.getUserData();

        // Build the List
        progressResultList = new ArrayList<>();
        for (int i = 0; i < category.getNumberOfLevels(); i++) {
            String level = getResources().getStringArray(stringArrays[getArguments().getInt("category")])[i];
            int timesPlayed = userData.getLevelsProgressData().get(category).get(i).getTimesPlayed();
            int highScore = userData.getLevelsProgressData().get(category).get(i).getMaxScore();
            boolean isFinished = userData.getLevelsProgressData().get(category).get(i).isFinished();
            List<Integer> weightList = fileManager.getLevelWeights().get(category).get(i);
            progressResultList.add(new ProgressResult(level,timesPlayed,highScore,isFinished, weightList));
        }
        progressResultsAdapter = new ProgressResultsAdapter(progressResultList, getContext());
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(progressResultsAdapter);
        return rootView;
    }
}
