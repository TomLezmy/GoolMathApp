package com.tomlezmy.goolmathapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.model.ProgressResult;

import java.util.List;

public class ProgressResultsAdapter extends RecyclerView.Adapter<ProgressResultsAdapter.ProgressResultsViewHolder> {

    private List<ProgressResult> progressResults;
    private Context context;


    public ProgressResultsAdapter(List<ProgressResult> progressResults, Context context) {
        this.progressResults = progressResults;
        this.context = context;
    }

    public class ProgressResultsViewHolder extends RecyclerView.ViewHolder {
        TextView levelTv;
        TextView timesPlayTv;
        TextView highScoreTv;

        public ProgressResultsViewHolder(View itemView) {
            super(itemView);

            levelTv = itemView.findViewById(R.id.tv_level);
            timesPlayTv = itemView.findViewById(R.id.tv_times_played);
            highScoreTv = itemView.findViewById(R.id.tv_highScore);
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
        String tv_timesPlayed_text = String.format(context.getResources().getString(R.string.you_played_x_times), timesPlayed);
        holder.timesPlayTv.setText(tv_timesPlayed_text);

        int highScore =  progressResult.getHighScore();
        String tv_highScore_text = String.format(context.getResources().getString(R.string.your_high_score_is_x), highScore);
        holder.highScoreTv.setText(tv_highScore_text);
        if (timesPlayed == 0) {
            ((CardView)(holder.itemView)).setCardBackgroundColor(ContextCompat.getColor(context, R.color.disabled_card));
        }
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
