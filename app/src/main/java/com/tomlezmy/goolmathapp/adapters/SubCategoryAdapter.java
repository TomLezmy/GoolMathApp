package com.tomlezmy.goolmathapp.adapters;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.game.CategoryProgressData;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {

    private List<String> subCategories;
    private int colorId;
    private SubCategoriesListener listener;
    List<CategoryProgressData> categoryProgressDataList;

    public interface SubCategoriesListener {
        void onSubCategoryClicked(int position, View view);
    }

    public void setListener(SubCategoriesListener listener) {
        this.listener = listener;
    }

    // constructor
    public SubCategoryAdapter(List<String> subCategories, int colorId, List<CategoryProgressData> categoryProgressDataList) {
        this.subCategories = subCategories;
        this.colorId = colorId;
        this.categoryProgressDataList = categoryProgressDataList;
    }

    public class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;

        public SubCategoryViewHolder(View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.sub_category_name_tv);
            nameTv.setTextColor(colorId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onSubCategoryClicked(getAdapterPosition(),view);
                    }
                }
            });
        }

    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_cell, parent,false);
        SubCategoryViewHolder subCategoryViewHolder = new SubCategoryViewHolder(view);
        return subCategoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        String subCategory = subCategories.get(position);
        holder.nameTv.setText(subCategory);
        if (categoryProgressDataList != null) {
            if (!categoryProgressDataList.get(position).isOpen()) {
                holder.nameTv.setTextColor(Color.GRAY);
                holder.itemView.setEnabled(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }


}








//    List<String> subCategories;
//
//    // constructor
//    public SubCategoryAdapter(List<String> subCategories) {
//        this.subCategories = subCategories;
//    }
//
//public class SubCategoryViewHolder extends RecyclerView.ViewHolder {
//    TextView nameTv;
//    // constructor
//    public SubCategoryViewHolder(View itemView) {
//        super(itemView);
//        nameTv = itemView.findViewById(R.id.sub_category_name_tv);
//    }
//
//}
//
//    @NonNull
//    @Override
//    public SubCategoryAdapter.SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_cell, parent,false);
//        SubCategoryAdapter.SubCategoryViewHolder subCategoryViewHolder = new SubCategoryAdapter.SubCategoryViewHolder(view);
//        return subCategoryViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SubCategoryAdapter.SubCategoryViewHolder holder, int position) {
//        String subCategory = subCategories.get(position);
//        holder.nameTv.setText(subCategory);
//    }
//
//    @Override
//    public int getItemCount() {
//        return subCategories.size();
//    }
//
//
//}
