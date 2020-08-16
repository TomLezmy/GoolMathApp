package com.tomlezmy.goolmathapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.adapters.SubCategoryAdapter;
import com.tomlezmy.goolmathapp.game.CategoryProgressData;

import java.util.List;


public class SubCategoriesFragment extends DialogFragment {

    List<String> subCategories;
    RecyclerView recyclerView;
    SubCategoryAdapter subCategoryAdapter;
    int colorId;
    Window window;
    Dialog dialog;
    List<CategoryProgressData> categoryProgressDataList;

    public interface OnSubCategoryListener {
        void onSubCategory(int subCategoryId);
    }

    OnSubCategoryListener callback;



    // constructor
    SubCategoriesFragment(List<String> subCategories, int colorId) {
        this.subCategories = subCategories;
        this.colorId = colorId;
        this.categoryProgressDataList = null;
    }
    SubCategoriesFragment(List<String> subCategories, int colorId, List<CategoryProgressData> categoryProgressDataList) {
        this.subCategories = subCategories;
        this.colorId = colorId;
        this.categoryProgressDataList =categoryProgressDataList;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnSubCategoryListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement on OnSubCategoryListener interface");
        }
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sub_categories,container);
        recyclerView = rootView.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        subCategoryAdapter = new SubCategoryAdapter(this.subCategories, this.colorId, this.categoryProgressDataList);
        recyclerView.setAdapter(subCategoryAdapter);

        this.dialog = this.getDialog();
        this.window = this.getDialog().getWindow();
        this.window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        subCategoryAdapter.setListener(new SubCategoryAdapter.SubCategoriesListener() {
            @Override
            public void onSubCategoryClicked(int position, View view) {
                callback.onSubCategory(position);
                dismiss();
            }
        });
        return rootView;
    }
}
