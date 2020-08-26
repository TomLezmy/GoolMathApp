package com.tomlezmy.goolmathapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.game.CategoryProgressData;

import java.util.List;

/**
 * An adapter for {@link com.tomlezmy.goolmathapp.fragments.SubCategoriesFragment} to display all progress data in {@link RecyclerView}
 */
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {

    private List<String> subCategories;
    private int colorId;
    private SubCategoriesListener listener;
    List<CategoryProgressData> categoryProgressDataList;

    /**
     * This interface is used to notify the listener when a sub category is clicked
     */
    public interface SubCategoriesListener {
        /**
         * @param position The clicked sub category index
         */
        void onSubCategoryClicked(int position);
    }

    /**
     * Set a listener for sub category click events
     * @param listener The listener to set
     */
    public void setListener(SubCategoriesListener listener) {
        this.listener = listener;
    }

    /**
     * Class constructor
     * @param subCategories A list of categories to display in the recycler
     * @param colorId The tex color of the items
     * @param categoryProgressDataList A list of the progress data for the current category
     */
    // constructor
    public SubCategoryAdapter(List<String> subCategories, int colorId, List<CategoryProgressData> categoryProgressDataList) {
        this.subCategories = subCategories;
        this.colorId = colorId;
        this.categoryProgressDataList = categoryProgressDataList;
    }

    /**
     * A {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} to access items in {@link RecyclerView
     */
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
                        listener.onSubCategoryClicked(getAdapterPosition());
                    }
                }
            });
        }

    }

    /**
     * Inflates the recycler item view and instantiates {@link SubCategoryViewHolder}
     */
    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_cell, parent,false);
        return new SubCategoryViewHolder(view);
    }

    /**
     * Fills the {@link SubCategoryViewHolder} with the current item data
     */
    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        String subCategory = subCategories.get(position);
        holder.nameTv.setText(subCategory);
        if (categoryProgressDataList != null) {
            if (!categoryProgressDataList.get(position).isOpen()) {
                holder.nameTv.setTextColor(Color.GRAY);
                holder.itemView.setEnabled(false);
            }
            else {
                holder.nameTv.setTextColor(colorId);
                holder.itemView.setEnabled(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }


}
