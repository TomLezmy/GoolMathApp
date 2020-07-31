package com.tomlezmy.goolmathapp.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.game.CategoryProgressData;
import com.tomlezmy.goolmathapp.model.ProgressResult;

import java.util.List;

public class ProgressResultsAdapter extends RecyclerView.Adapter<ProgressResultsAdapter.ProgressResultsViewHolder> {

    private List<ProgressResult> progressResults;
//    private ProgressResultsListener listener;
    private Context context;


    public ProgressResultsAdapter(List<ProgressResult> progressResults, Context context) {
        this.progressResults = progressResults;
        this.context = context;
    }

//    public interface ProgressResultsListener {
//        void onSubCategoryClicked(int position, View view);
//    }

//    public void setListener(ProgressResultsListener listener) {
//        this.listener = listener;
//    }


    public class ProgressResultsViewHolder extends RecyclerView.ViewHolder {
        TextView levelTv;
        TextView timesPlayTv;
        TextView highScoreTv;

        public ProgressResultsViewHolder(View itemView) {
            super(itemView);

            levelTv = itemView.findViewById(R.id.tv_level);
            timesPlayTv = itemView.findViewById(R.id.tv_times_played);
            highScoreTv = itemView.findViewById(R.id.tv_highScore);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (listener != null) {
//                        listener.onSubCategoryClicked(getAdapterPosition(),view);
//                    }
//                }
//            });
        }

    }

    @NonNull
    @Override
    public ProgressResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_results_cell, parent,false);
        ProgressResultsViewHolder progressResultsViewHolder = new ProgressResultsViewHolder(view);
        return progressResultsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressResultsViewHolder holder, int position) {
        ProgressResult progressResult = progressResults.get(position);

        String level =  progressResult.getLevel();
        String tv_level_text = String.format(context.getResources().getString(R.string.in_the_level_x), level);
        holder.levelTv.setText(tv_level_text);

        int timesPlayed =  progressResult.getTimesPlayed();
        String tv_timesPlyed_text = String.format(context.getResources().getString(R.string.you_played_x_times), timesPlayed);
        holder.timesPlayTv.setText(tv_timesPlyed_text);

        int highScore =  progressResult.getHighScore();
        String tv_highScore_text = String.format(context.getResources().getString(R.string.your_high_score_is_x), highScore);
        holder.highScoreTv.setText(tv_highScore_text);
    }

    @Override
    public int getItemCount() {
        return progressResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
